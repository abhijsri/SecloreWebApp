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
import com.seclore.sample.dms.util.PortalUtil;
import com.seclore.sample.dms.util.xml.XMLDBService;

/**
 * Servlet implementation class PreAddFileServlet
 */
@WebServlet(name = "preAddFileServlet", urlPatterns = { "/addFile.do" }) public class PreAddFileServlet
        extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PreAddFileServlet() {
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
        LoggerUtil.logDebug("PreAddFileServlet::doPost::START");
        HttpSession session = request.getSession();
        String folderId = request.getParameter("folderId");

        if (folderId == null || folderId.trim().isEmpty()) {
            session.setAttribute("ERROR_MESSAGE", "Please specify a valid folder ID!");
            response.sendRedirect("folderList.do");
            return;
        }

        AppFolder lFolder = XMLDBService.getFolder(folderId);
        if (lFolder == null) {
            session.setAttribute("ERROR_MESSAGE", "The folder for selected file could not be found!");
            response.sendRedirect("folderList.do");
            return;
        }

        AppFile lFile = new AppFile();
        PortalUtil.setRequestAttributes(request, lFile, lFolder);
        LoggerUtil.logDebug("PreAddFileServlet::doPost::END");
        request.getRequestDispatcher("/portal/pages/file.jsp").forward(request, response);
        return;
    }

}
