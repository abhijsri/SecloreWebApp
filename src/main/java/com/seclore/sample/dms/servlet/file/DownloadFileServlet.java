package com.seclore.sample.dms.servlet.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.seclore.fs.helper.library.FSHelperLibrary;
import com.seclore.sample.dms.config.HotFolderConfig;
import com.seclore.sample.dms.constant.Constants;
import com.seclore.sample.dms.core.AppFile;
import com.seclore.sample.dms.core.AppFolder;
import com.seclore.sample.dms.util.Global;
import com.seclore.sample.dms.util.LoggerUtil;
import com.seclore.sample.dms.util.SecloreProtectUtil;
import com.seclore.sample.dms.util.XMLUtil;
import com.seclore.sample.dms.util.xml.XMLDBService;

/**
 * Servlet implementation class DownloadFileServlet
 * Handle file download request
 *
 * @author harindra.chaudhary
 */
@WebServlet(name = "downloadFileServlet", urlPatterns = { "/downloadFile.do" }) public class DownloadFileServlet
        extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TEMP_FOLDER_PATH = Global.getAppRealPath() + File.separator + "SECLORE_TEMP_FOLDER";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadFileServlet() {
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
        LoggerUtil.logDebug("DownloadFileServlet::doPost::START");
        HttpSession session = request.getSession();
        String folderId = request.getParameter("folderId");
        String fileId = request.getParameter("fileId");
        boolean lbJustWrapped = false;

        // ======== File and Folder Validations - START ======== //
        if (folderId == null || folderId.trim().isEmpty()) {
            session.setAttribute("ERROR_MESSAGE", "The folder for selected file could not be found!");
            response.sendRedirect("folderList.do");
            return;
        }

        AppFolder lAppFolder = XMLDBService.getFolder(folderId);
        if (lAppFolder == null) {
            session.setAttribute("ERROR_MESSAGE", "The folder for selected file could not be found!");
            response.sendRedirect("folderList.do");
            return;
        }

        if (fileId == null || fileId.trim().isEmpty()) {
            session.setAttribute("ERROR_MESSAGE", "The file you were looking for could not be found!");
            response.sendRedirect("fileList.do?folderId=" + folderId);
            return;
        }

        AppFile lAppFile = XMLDBService.getFile(folderId, fileId);
        if (lAppFile == null) {
            session.setAttribute("ERROR_MESSAGE", "The file you were looking for could not be found!");
            response.sendRedirect("fileList.do?folderId=" + folderId);
            return;
        }
        // ======== File and Folder Validations - END ======== //

        String fileName = lAppFile.getName();
        String originalFilePath = Global.getAppDataDir() + File.separator + folderId + File.separator + fileName;
        File lfile = new File(originalFilePath);
        if (!lfile.exists()) {
            session.setAttribute("ERROR_MESSAGE", "The file you were looking for could not be found!");
            response.sendRedirect("fileList.do?folderId=" + folderId);
            return;
        }

        InputStream fileInputStream = null;
        try {
            // Get file data as ByteStream
            fileInputStream = new FileInputStream(originalFilePath);

            // ********************************************************* //
            // ****** Seclore File Protection integration - START ****** //
            // ********************************************************* //

            boolean isSupportedFile = FSHelperLibrary.isSupportedFile(fileName);

            // If folder is Seclore IRM Enabled and file is supported by Seclore WSClient
            if (lAppFolder.getIrmEnabled() & isSupportedFile) {
                lbJustWrapped = lAppFolder.getHtmlWrapEnabled();

                // ****** WRITE TEMP FILE ****** //
                // Create Temporary folder if not exist
                createTempFolder(TEMP_FOLDER_PATH);
                String fileExt = fileName.substring(fileName.lastIndexOf("."));
                String tempFileName =
                        fileName.substring(0, fileName.lastIndexOf(".")) + "_" + new Date().getTime() + fileExt;
                String tempFilePath = TEMP_FOLDER_PATH + File.separator + tempFileName;
                try {
                    SecloreProtectUtil.writeTempFile(fileInputStream, tempFilePath);
                } finally {
                    try {
                        fileInputStream.close();
                        fileInputStream = null;
                    } catch (IOException ioExp) {
                    }
                }

                Gson lGson = new Gson();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(Constants.KEY_HF_EXTN_REF_ID, lAppFolder.getId());
                String lstrExtnRefData = lGson.toJson(jsonObject);

                String lstrExtnHFid = HotFolderConfig.getExternalHotFolderId();
                // === Construct Protection details XML
                String protectionDetailsXML = "<hot-folder-extn-reference>" + "<extn-reference>" +
                        //"<extn-ref-id>"+XMLUtil.escapeForXML(lAppFolder.getId()) +"</extn-ref-id>"+
                        //"<extn-ref-name>"+XMLUtil.escapeForXML(lAppFolder.getName())+"</extn-ref-name>"+
                        "<extn-ref-id>" + XMLUtil.escapeForXML(lstrExtnHFid) + "</extn-ref-id>" + "<extn-ref-name>"
                        + XMLUtil.escapeForXML("") + "</extn-ref-name>" + "<extn-ref-data>" + XMLUtil.escapeForXML("")
                        + "</extn-ref-data>" + "<extn-app-id>" + XMLUtil.escapeForXML("") + "</extn-app-id>"
                        + "</extn-reference>" + "</hot-folder-extn-reference>" +

                        "<file-extn-reference>" + "<extn-reference>" + "<extn-ref-id>" + XMLUtil
                        .escapeForXML(lAppFile.getId()) + "</extn-ref-id>" + "<extn-ref-name>" + XMLUtil
                        .escapeForXML(lAppFile.getName()) + "</extn-ref-name>" + "<extn-ref-data>" + XMLUtil
                        .escapeForXML(lstrExtnRefData) + "</extn-ref-data>" + "<extn-app-id>" + XMLUtil.escapeForXML("")
                        + "</extn-app-id>" + "</extn-reference>" + "</file-extn-reference>";

                // ****** Protect TEMP File and Get protected content  ****** //
                String displayFileName = lAppFile.getName();
                fileInputStream = SecloreProtectUtil.protectFile(tempFilePath, displayFileName, protectionDetailsXML,
                        lAppFolder.getHtmlWrapEnabled());
            }

            // ******************************************************* //
            // ****** Seclore File Protection integration - END ****** //
            // ******************************************************* //

            response.setContentType((lbJustWrapped == true ? "text/html" : lAppFile.getContentType()));
            response.setContentLength(fileInputStream.available());
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + lAppFile.getName() + (lbJustWrapped == true ? ".html" : "") + "\"");

            ServletOutputStream servletOutputStream = response.getOutputStream();
            byte[] bufferData = new byte[1024];
            int lRead = 0;

            while ((lRead = fileInputStream.read(bufferData)) != -1) {
                servletOutputStream.write(bufferData, 0, lRead);
            }

            try {
                servletOutputStream.flush();
            } catch (IOException ioExp) {
            }

        } catch (Exception lEx) {
            LoggerUtil.logError("Error occured while downloading the file", lEx);
            session.setAttribute("ERROR_MESSAGE",
                    "Error occured while downloading the file- '" + lEx.getMessage() + "'");
            LoggerUtil.logDebug("DownloadFileServlet::doPost::END");
            response.sendRedirect("fileList.do?folderId=" + folderId);
            return;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                    fileInputStream = null;
                } catch (IOException ioExp) {
                    // Ignore
                }
            }
        }

        LoggerUtil.logDebug("DownloadFileServlet::doPost::END");
    }

    /**
     * Create Temporary Folder if not exist
     *
     * @param pTempFolderPath
     */
    private void createTempFolder(String pTempFolderPath) {
        File tempFolder = new File(pTempFolderPath);
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }
    }
}
