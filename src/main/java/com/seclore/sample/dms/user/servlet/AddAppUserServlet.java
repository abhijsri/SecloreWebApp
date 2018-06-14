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
 * Servlet implementation class AddAppUserServlet
 */
@WebServlet("/addUser.do") public class AddAppUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddAppUserServlet() {
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

        if (XMLDBService.getAppUserById(userId) != null) {
            session.setAttribute("ERROR_MESSAGE",
                    "Another user already exist in the system with the same ext id/emailid.");
            response.sendRedirect("user.do");
            return;
        }

        if (XMLDBService.addNewAppUser(userId, name)) {
            session.setAttribute("SUCCESS_MESSAGE", "New user '" + userId + "' have been added successfully.");
        } else {
            session.setAttribute("ERROR_MESSAGE", "New user '" + userId + "' have not been added.");
        }

        response.sendRedirect("user.do");
        return;
    }

}
