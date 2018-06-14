package com.seclore.sample.dms.servlet.file;

import java.io.IOException;
import java.util.Collection;

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
 * Servlet implementation class FileListServlet
 */
@WebServlet(name = "fileListServlet", urlPatterns = { "/fileList.do" }) public class FileListServlet
        extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileListServlet() {
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
        LoggerUtil.logDebug("FileListServlet::doPost::START");
        HttpSession session = request.getSession();
        String folderId = request.getParameter("folderId");

        if (folderId == null || folderId.trim().isEmpty()) {
            LoggerUtil.logDebug("FileListServlet::doPost::Folder ID not found to displaye the file list");
            session.setAttribute("ERROR_MESSAGE", "Please specify a valid folder ID!");
            response.sendRedirect("folderList.do");
            return;
        }
        AppFolder folder = XMLDBService.getFolder(folderId);
        if (folder == null) {
            session.setAttribute("ERROR_MESSAGE", "The folder for selected file could not be found!");
            response.sendRedirect("folderList.do");
            return;
        }

        Collection<AppFile> fileList = XMLDBService.getFileList(folderId);
        request.setAttribute("folder", folder);
        request.setAttribute("fileList", fileList);
        request.getRequestDispatcher("/portal/pages/fileList.jsp").forward(request, response);
        LoggerUtil.logDebug("FileListServlet::doPost::END");
    }

}
