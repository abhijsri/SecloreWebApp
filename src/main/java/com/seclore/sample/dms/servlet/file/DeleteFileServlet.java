package com.seclore.sample.dms.servlet.file;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.seclore.sample.dms.core.AppFile;
import com.seclore.sample.dms.core.AppFolder;
import com.seclore.sample.dms.util.LoggerUtil;
import com.seclore.sample.dms.util.xml.XMLDBService;

/**
 * Servlet implementation class DeleteFileServlet
 */
@WebServlet(name = "deleteFileServlet", urlPatterns = { "/deleteFile.do" }) public class DeleteFileServlet
        extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteFileServlet() {
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
        LoggerUtil.logDebug("DeleteFileServlet::doPost::START");
        HttpSession session = request.getSession();
        String fileId = request.getParameter("fileId");
        String folderId = request.getParameter("folderId");

        if (folderId == null || folderId.trim().isEmpty()) {
            session.setAttribute("ERROR_MESSAGE", "The folder for selected file could not be found!");
            response.sendRedirect("folderList.do");
            return;
        }

        AppFolder lFolder = XMLDBService.getFolder(folderId);
        if (lFolder == null) {
            session.setAttribute("ERROR_MESSAGE", "The folder for selected file could not be found!");
            response.sendRedirect("folderList.do");
            return;
        }

        if (fileId == null || fileId.trim().isEmpty()) {
            session.setAttribute("ERROR_MESSAGE", "The file you were deleting for could not be found!");
            response.sendRedirect("fileList.do?folderId=" + folderId);
            return;
        }

        AppFile lFile = XMLDBService.getFile(folderId, fileId);
        if (lFile == null) {
            session.setAttribute("ERROR_MESSAGE", "The file you were deleting for could not be found!");
            response.sendRedirect("fileList.do?folderId=" + folderId);
            return;
        }

        if (XMLDBService.deleteFile(folderId, lFile)) {
            LoggerUtil.logInfo("User '" + session.getAttribute("name") + "' deleted the file '" + lFile.getName()
                    + "' from folder '" + lFolder.getName() + "'");
            session.setAttribute("SUCCESS_MESSAGE", "File has been deleted successfully!");
        } else {
            session.setAttribute("ERROR_MESSAGE", "File deletion faild, Please try again!");
        }

        LoggerUtil.logDebug("DeleteFileServlet::doPost::END");
        response.sendRedirect("fileList.do?folderId=" + folderId);
        return;
    }

}
