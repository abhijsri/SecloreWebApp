package com.seclore.sample.dms.servlet.file;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.seclore.sample.dms.core.AppFile;
import com.seclore.sample.dms.core.AppFolder;
import com.seclore.sample.dms.core.FileActivityLog;
import com.seclore.sample.dms.util.LoggerUtil;
import com.seclore.sample.dms.util.PortalUtil;
import com.seclore.sample.dms.util.xml.XMLDBService;
import com.seclore.sample.ps.db.DBConnection;

/**
 * Servlet implementation class for ViewFileLogs
 */
@WebServlet(name = "viewFileLogsServlet", urlPatterns = { "/viewFileLogs.do" }) public class ViewFileLogsServlet
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewFileLogsServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoggerUtil.logDebug("ViewFileLogsServlet::doPost::START");
        HttpSession session = request.getSession();
        String fileId = request.getParameter("fileId");
        long lPSFileId;
        String folderId = request.getParameter("folderId");
        List<FileActivityLog> lliFileActivityLog = null;

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

        //lPSFileId = DBConnection.getFileIdByExtFileId(fileId);

        lliFileActivityLog = DBConnection.getFileLogsByFileId(fileId);

        PortalUtil.setRequestAttributes(request, lliFileActivityLog);
        PortalUtil.setRequestAttributes(request, lFile, lFolder);

        LoggerUtil.logDebug("ViewFileLogsServlet::doPost::END");
        request.getRequestDispatcher("/portal/pages/logs.jsp").forward(request, response);
        return;
    }

}
