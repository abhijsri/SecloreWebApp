package com.seclore.sample.dms.servlet.folder;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.seclore.sample.dms.core.AppFolder;
import com.seclore.sample.dms.util.LoggerUtil;
import com.seclore.sample.dms.util.xml.XMLDBService;

/**
 * Delete selected folder
 */
@WebServlet(name = "deleteFolderServlet", urlPatterns = { "/deleteFolder.do" }) public class DeleteFolderServlet
        extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteFolderServlet() {
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
        LoggerUtil.logDebug("DeleteFolderServlet::doPost::START");
        HttpSession session = request.getSession();
        String folderId = request.getParameter("folderId");

        if (folderId == null || folderId.trim().isEmpty()) {
            session.setAttribute("ERROR_MESSAGE", "The folder you were looking for could not be found!");
            response.sendRedirect("folderList.do");
            return;
        }

        AppFolder lFolder = XMLDBService.getFolder(folderId);

        if (lFolder == null) {
            session.setAttribute("ERROR_MESSAGE", "The folder you were looking for could not be found!");
            response.sendRedirect("folderList.do");
            return;
        }

        if (XMLDBService.deleteFolder(lFolder)) {
            LoggerUtil.logInfo(
                    "User '" + session.getAttribute("name") + "' deleted the folder '" + lFolder.getName() + "'");
            session.setAttribute("SUCCESS_MESSAGE", "Folder has been deleted successfully!");
        } else {
            request.setAttribute("ERROR_MESSAGE", "Folder deletion faild, Please try again!");
        }

        LoggerUtil.logDebug("DeleteFolderServlet::doPost::END");
        response.sendRedirect("folderList.do");
        return;
    }

}
