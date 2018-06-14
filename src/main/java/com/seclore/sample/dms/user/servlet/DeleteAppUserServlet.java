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
 * Servlet implementation class DeleteAppUserServlet
 */
@WebServlet("/deleteUser.do") public class DeleteAppUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteAppUserServlet() {
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

        if (XMLDBService.getAppUserById(userId) == null) {
            session.setAttribute("ERROR_MESSAGE", "User '" + userId + "' could not be found!");
            response.sendRedirect("user.do");
            return;
        }

        if (XMLDBService.deleteAppUser(userId)) {
            session.setAttribute("SUCCESS_MESSAGE", "User '" + userId + "' have been deleted successfully.");
        } else {
            session.setAttribute("ERROR_MESSAGE", "User '" + userId + "' have not been deleted.");
        }

        response.sendRedirect("user.do");
        return;
    }

}
