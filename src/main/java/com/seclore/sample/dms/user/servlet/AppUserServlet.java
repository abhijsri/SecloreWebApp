package com.seclore.sample.dms.user.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seclore.sample.dms.core.master.User;
import com.seclore.sample.dms.util.xml.XMLDBService;

/**
 * Servlet implementation class AppUserServlet
 */
@WebServlet("/user.do") public class AppUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AppUserServlet() {
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
        Map<String, User> lmapUsers = XMLDBService.getAppUsers();

        request.setAttribute("users", lmapUsers.values());
        request.getRequestDispatcher("/portal/pages/appUser.jsp").forward(request, response);
    }

}
