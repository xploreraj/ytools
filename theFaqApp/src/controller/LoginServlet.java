package controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(description = "validates login and dispatches to create_edit.jsp", urlPatterns = { "/admin", "/logout" })
//@WebServlet("/admin")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI().trim();
		HttpSession session = request.getSession(false);
		//we are allowing "/admin" access the admin area if session is valid- maxInactiveInterval= 3600 seconds
		boolean sessionIsValid = false;
		sessionIsValid = isSessionValid(session, "isAdmin", "true");
		if (requestURI.equalsIgnoreCase("/theFaqApp/logout") || !sessionIsValid) {
			if(sessionIsValid) 
				session.invalidate();
			request.removeAttribute("errorMessage");
			response.sendRedirect("login.jsp");
		}
		else
			doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		boolean sessionIsValid = isSessionValid(session, "isAdmin", "true");
		
		String requestURI = request.getRequestURI().trim();
		
		if (requestURI.equalsIgnoreCase("/theFaqApp/admin")) {
			String password = request.getParameter("password");
			
			if(sessionIsValid || "123".equals(password)) {
				if (!sessionIsValid) {
					session = request.getSession(true);
					session.setAttribute("isAdmin", "true");
					session.setMaxInactiveInterval(3600);
				}
				request.removeAttribute("errorMessage");
				request.getRequestDispatcher("/WEB-INF/jsp/create_edit.jsp").forward(request, response);
			}
			else{
				if(sessionIsValid)
					session.invalidate();
				request.setAttribute("errorMessage", "Invalid Password!");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		}
		
	}
	
	/**
	 * Check whether HttpSession exists and is valid
	 * @return valid or invalid - boolean
	 */
	private boolean isSessionValid(HttpSession session, String key, String value) {
		boolean sessionIsValid = false;
		try{
			if(session!=null) {
				if((""+value).equals(session.getAttribute(key)))
					sessionIsValid = true;
			}
		}
		catch(IllegalStateException e) {
			//session already invalidated
		}
		return sessionIsValid;
	}

}
