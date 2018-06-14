package com.seclore.sample.dms.user.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.seclore.sample.dms.util.xml.XMLDBService;

/**
 * Servlet implementation class UpdateAppUserServlet
 */
@WebServlet("/updateUser.do") public class UpdateAppUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateAppUserServlet() {
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
        HttpSession session = request.getSession();

        String userId = request.getParameter("userId");
        String name = request.getParameter("name");

        if (XMLDBService.getAppUserById(userId) == null) {
            session.setAttribute("ERROR_MESSAGE", "User '" + userId + "' could not be found!");
            response.sendRedirect("user.do");
            return;
        }

        if (XMLDBService.updateAppUser(userId, name)) {
            session.setAttribute("SUCCESS_MESSAGE", "User '" + userId + "' have been updated successfully.");
        } else {
            session.setAttribute("ERROR_MESSAGE", "User '" + userId + "' have not been updated.");
        }

        response.sendRedirect("user.do");
        return;
    }

}
