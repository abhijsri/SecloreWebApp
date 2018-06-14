package com.seclore.sample.dms.log;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seclore.sample.dms.util.LoggerUtil;

/**
 * Servlet implementation class ChangeDebugModeServlet
 */
@WebServlet("/changeloggerlevel.do") public class ChangeLoggerLevelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeLoggerLevelServlet() {
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
        String lstrlevel = request.getParameter("level");
        LoggerUtil.setLoggerLevel(lstrlevel);
        response.sendRedirect("home");
        return;
    }

}
