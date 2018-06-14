package com.seclore.sample.dms.servlet.folder;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seclore.sample.dms.core.AppFolder;
import com.seclore.sample.dms.util.LoggerUtil;
import com.seclore.sample.dms.util.xml.XMLDBService;

/**
 * List all the folders
 */
@WebServlet(name = "folderListServlet", urlPatterns = { "/folderList.do" }) public class FolderListServlet
        extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FolderListServlet() {
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
        LoggerUtil.logDebug("FolderListServlet : doPost : start");

        Collection<AppFolder> folderList = XMLDBService.getFolderList();
        request.setAttribute("folderList", folderList);

        LoggerUtil.logDebug("FolderListServlet : doPost : end");
        request.getRequestDispatcher("/portal/pages/folderList.jsp").forward(request, response);
        return;
    }

}
