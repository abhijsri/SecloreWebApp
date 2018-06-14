package com.seclore.sample.dms.servlet.file;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.seclore.fs.helper.exception.FSHelperException;
import com.seclore.fs.helper.library.FSHelperLibrary;
import com.seclore.sample.dms.constant.Constants;
import com.seclore.sample.dms.core.AppFile;
import com.seclore.sample.dms.core.AppFolder;
import com.seclore.sample.dms.core.Classification;
import com.seclore.sample.dms.core.Owner;
import com.seclore.sample.dms.core.UserRight;
import com.seclore.sample.dms.core.master.User;
import com.seclore.sample.dms.util.Global;
import com.seclore.sample.dms.util.LoggerUtil;
import com.seclore.sample.dms.util.PortalUtil;
import com.seclore.sample.dms.util.xml.XMLDBService;

/**
 * Servlet implementation class SaveFileServlet
 */
@WebServlet(name = "saveFileServlet", urlPatterns = { "/saveFile.do" }) @MultipartConfig(maxFileSize =
        (Constants.MAX_FILE_SIZE + 2) * 1024 * 1024) public class SaveFileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveFileServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoggerUtil.logDebug("SaveFileServlet::doPost::START");
        HttpSession session = request.getSession();

        // First read the file, if file size will be more than MAX_FILE_SIZE, then part and all other parameter will be null.
        Part part = null;
        boolean isError = false;
        String errorMessage = "";
        try {
            part = request.getPart("file");
        } catch (IOException ioException) {
            LoggerUtil.logError("SaveFileServlet::doPost::" + ioException.getMessage(), ioException);
            // Request aborted
            if (ioException.getMessage()
                    .contains("org.apache.tomcat.util.http.fileupload.FileUploadBase$IOFileUploadException")) {
                return;
            } else {
                isError = true;
                errorMessage = ioException.getMessage();
            }
        } catch (IllegalStateException iex) {
            LoggerUtil.logError("SaveFileServlet::doPost::" + iex.getMessage(), iex);
            isError = true;
            // file size exceeded
            if (iex.getMessage()
                    .contains("org.apache.tomcat.util.http.fileupload.FileUploadBase$FileSizeLimitExceededException")) {
                errorMessage = "The file exceeds its maximum permitted size of " + Constants.MAX_FILE_SIZE + "MB";
            } else {
                errorMessage = iex.getMessage();
            }
        }

        if (isError) {
            session.setAttribute("ERROR_MESSAGE", errorMessage);
            response.sendRedirect("folderList.do");
            return;
        }

        String folderId = request.getParameter("folderId");
        if (folderId == null || folderId.trim().isEmpty()) {
            LoggerUtil.logDebug("SaveFileServlet::doPost::Folder ID not found to add the file");
            session.setAttribute("ERROR_MESSAGE", "Please specify a valid folder ID!");
            response.sendRedirect("folderList.do");
            return;
        }
        AppFolder lFolder = XMLDBService.getFolder(folderId);
        if (lFolder == null) {
            session.setAttribute("ERROR_MESSAGE", "The folder for the file could not be found!");
            response.sendRedirect("folderList.do");
            return;
        }

        String fileId = request.getParameter("fileId");
        String usersRightsJson = request.getParameter("usersRightsJson");
        String ownerEmailId = request.getParameter("ownerEmaiId");
        String classificationId = request.getParameter("classification");

        Set<UserRight> newUserRights = parseUserRights(usersRightsJson);

        Owner lOwner = null;
        if (ownerEmailId != null && !ownerEmailId.trim().isEmpty()) {
            User user = XMLDBService.getAppUserById(ownerEmailId);
            lOwner = new Owner();
            lOwner.setEmailId(ownerEmailId.trim());
            lOwner.setName(user.getName());
        }

        Classification lClassification = null;
        if (classificationId != null && !classificationId.trim().isEmpty()) {
            try {
                Integer.parseInt(classificationId);
                lClassification = XMLDBService.getClassification(classificationId);
            } catch (NumberFormatException nfe) { /*Default Classification*/}
        }

        AppFile lNewAppFile = new AppFile();
        lNewAppFile.setUserRightList(newUserRights);
        lNewAppFile.setOwner(lOwner);
        lNewAppFile.setClassification(lClassification);

        // New file
        if (fileId == null || fileId.trim().isEmpty()) {
            String filename = getFileName(part);
            LoggerUtil.logDebug("SaveFileServlet::doPost::File Name: " + filename);

            if (filename == null || filename.isEmpty()) {
                PortalUtil.setRequestAttributes(request, lNewAppFile, lFolder);
                request.setAttribute("ERROR_MESSAGE", "Please select the file!");
                request.getRequestDispatcher("/portal/pages/file.jsp").forward(request, response);
                return;
            }

            String folderPath = Global.getAppDataDir() + File.separator + folderId;

            File lFileFolder = new File(folderPath);
            if (!lFileFolder.exists()) {
                lFileFolder.mkdirs();
            }

            String filePath = folderPath + File.separator + filename;

            // ------- Rename file if already exist -START -------- //
            File lFile = new File(filePath);
            int no = 2;
            // File extension including .
            String fileExt = filename.substring(filename.lastIndexOf("."));
            // File name without extension
            String fileNameWOExt = filename.substring(0, filename.lastIndexOf("."));
            while (lFile.exists()) {
                filename = fileNameWOExt + " (" + no + ")" + fileExt;
                filePath = folderPath + File.separator + filename;
                lFile = new File(filePath);
                no++;
            }
            // ------- Rename file if already exist -END --------- //

            // Write file on disk
            try {
                part.write(filePath);
            } catch (IOException exception) {
                LoggerUtil.logError("SaveFileServlet::doPost::Failed to write the file on disk for user '" + session
                        .getAttribute("name") + "'", exception);
                PortalUtil.setRequestAttributes(request, lNewAppFile, lFolder);
                request.setAttribute("ERROR_MESSAGE", "The file has not been uploaded, Please try again!");
                request.getRequestDispatcher("/portal/pages/file.jsp").forward(request, response);
                return;
            }

            try {
                // In this sample application we do not allow to upload protected file
                // But in actual DMS or VDR integration id depends on business requirements.
                boolean isProtected = FSHelperLibrary.isProtectedFile(filePath);
                if (isProtected) {
                    File file = new File(filePath);
                    try {
                        file.delete();
                    } catch (Exception ex) {
                    }
                    PortalUtil.setRequestAttributes(request, lNewAppFile, lFolder);
                    LoggerUtil.logError("SaveFileServlet::doPost::User '" + session.getAttribute("name")
                            + "' tried to add already protected file in to '" + lFolder.getName() + "' folder.");
                    request.setAttribute("ERROR_MESSAGE",
                            "The file is already protected, Please upload unprotected file!");
                    request.getRequestDispatcher("/portal/pages/file.jsp").forward(request, response);
                    return;
                }
            } catch (FSHelperException e) {
                LoggerUtil.logError("SaveFileServlet::doPost::Error while checking isProtectedFile", e);
                PortalUtil.setRequestAttributes(request, lNewAppFile, lFolder);
                request.setAttribute("ERROR_MESSAGE", "The file has not been uploaded, Please try again!");
                request.getRequestDispatcher("/portal/pages/file.jsp").forward(request, response);
                return;
            }

            fileId = UUID.randomUUID().toString();
            lNewAppFile.setId(fileId);
            lNewAppFile.setName(filename);
            lNewAppFile.setContentType(part.getContentType());

            if (XMLDBService.addNewFile(folderId, lNewAppFile)) {
                LoggerUtil.logInfo("SaveFileServlet::doPost::User '" + session.getAttribute("name")
                        + "' succesfully added the file'" + lNewAppFile.getName() + "' in to '" + lFolder.getName()
                        + "' folder.");
                session.setAttribute("SUCCESS_MESSAGE", "The file has been uploaded successfully!");
            } else {
                LoggerUtil.logInfo(
                        "SaveFileServlet::doPost::User '" + session.getAttribute("name") + "' failed to add the file'"
                                + lNewAppFile.getName() + "' in to '" + lFolder.getName() + "' folder.");
                PortalUtil.setRequestAttributes(request, lNewAppFile, lFolder);
                request.setAttribute("ERROR_MESSAGE", "The file has not been uploaded, Please try again!");
                request.getRequestDispatcher("/portal/pages/file.jsp").forward(request, response);
                return;
            }

        }
        // Edit existing file details
        else {
            AppFile lAppFile = XMLDBService.getFile(folderId, fileId);
            if (lAppFile == null) {
                session.setAttribute("ERROR_MESSAGE", "The file you were looking for could not be found!");
                response.sendRedirect("fileList.do?folderId=" + folderId);
                return;
            }
            lNewAppFile.setId(lAppFile.getId());
            lNewAppFile.setName(lAppFile.getName());
            lNewAppFile.setContentType(lAppFile.getContentType());
            if (XMLDBService.updateFile(folderId, lNewAppFile)) {
                LoggerUtil.logInfo("SaveFileServlet::doPost::User '" + session.getAttribute("name")
                        + "' uptade the access rights to the file'" + lAppFile.getName() + "' in to '" + lFolder
                        .getName() + "' folder.");
                session.setAttribute("SUCCESS_MESSAGE", "The file has been updated successfully!");
            } else {
                PortalUtil.setRequestAttributes(request, lAppFile, lFolder);
                request.setAttribute("ERROR_MESSAGE", "The file has not been updated, Please try again!");
                request.getRequestDispatcher("/portal/pages/file.jsp").forward(request, response);
                return;
            }
        }

        LoggerUtil.logDebug("SaveFileServlet::doPost::END");
        response.sendRedirect("fileList.do?folderId=" + folderId);
        return;

    }

    /**
     * Extract file name from Part
     *
     * @param part
     * @return File Name
     */
    private String getFileName(Part part) {
        String filename = "";
        if (part == null) {
            return filename;
        }
        String cd = part.getHeader("Content-Disposition");

        if (cd == null) {
            return filename;
        }

        for (String content : cd.split(";")) {
            if (content.trim().startsWith("filename")) {
                filename = content.substring(content.indexOf("=") + 1);
                filename = filename.trim().replace("\"", "");
                int i = filename.lastIndexOf(File.separator);
                if (i > -1) {
                    filename = filename.substring(i + 1);
                }
                break;
            }
        }
        return filename;
    }

    /**
     * @param jsonString Example :- "{\"usersRights\":[{\"userId\":\"2\",\"usageRights\":\"1\"},{\"userId\":\"1\",\"usageRights\":\"2\"}]}";
     * @return Set<UserRight>
     */
    private static Set<UserRight> parseUserRights(String jsonString) {
        Set<UserRight> lsetUserRight = new LinkedHashSet<UserRight>();

        if (jsonString == null || jsonString.trim().isEmpty()) {
            return lsetUserRight;
        }

        Object jsonElement = new JsonParser().parse(jsonString);
        if (jsonElement == null || jsonElement instanceof JsonNull) {
            return lsetUserRight;
        }
        JsonObject jsonObject = (JsonObject) jsonElement;
        Type listType = new TypeToken<Set<UserRight>>() {
        }.getType();
        Gson gson = new Gson();
        lsetUserRight = gson.fromJson(jsonObject.get("usersRights"), listType);

        return lsetUserRight;
    }
}
