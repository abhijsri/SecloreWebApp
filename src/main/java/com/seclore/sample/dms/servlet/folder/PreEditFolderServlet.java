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
 * Servlet implementation class PreEditFolderServlet
 */
@WebServlet(name = "preEditFolderServlet", urlPatterns = { "/editFolder.do" }) public class PreEditFolderServlet
        extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PreEditFolderServlet() {
        super();
        // TODO Auto-generated constructor stub
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
        LoggerUtil.logDebug("PreEditFolderServlet::doPost::START");
        HttpSession session = request.getSession();
        String folderId = request.getParameter("folderId");

        if (folderId == null || folderId.trim().isEmpty()) {
            session.setAttribute("ERROR_MESSAGE", "Please specify a valid folder ID!");
            response.sendRedirect("folderList.do");
            return;
        }

        AppFolder folder = XMLDBService.getFolder(folderId);

        if (folder == null) {
            session.setAttribute("ERROR_MESSAGE", "The folder you were looking for could not be found!");
            response.sendRedirect("folderList.do");
            return;
        }
        request.setAttribute("folder", folder);
        LoggerUtil.logDebug("PreEditFolderServlet::doPost::END");
        request.getRequestDispatcher("/portal/pages/folder.jsp").forward(request, response);
        return;
    }

}
