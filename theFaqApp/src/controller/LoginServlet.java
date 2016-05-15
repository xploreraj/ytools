package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(description = "validates login and dispatches to create_edit.jsp", urlPatterns = { "/admin", "/logout" })
//@WebServlet("/admin")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI().trim();
		if (requestURI.equalsIgnoreCase("/theFaqApp/logout")) {
			request.removeAttribute("error");
			response.sendRedirect("login.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			String requestURI = request.getRequestURI().trim();
			if (requestURI.equalsIgnoreCase("/theFaqApp/admin")) {
				String password = request.getParameter("password");
				if("123".equals(password)) {
					request.removeAttribute("error");
					request.getRequestDispatcher("/WEB-INF/jsp/create_edit.jsp").forward(request, response);
				}
				else{
					request.setAttribute("error", "Invalid Password!");
					request.getRequestDispatcher("login.jsp").forward(request, response);
				}
			}
		
	}

}
