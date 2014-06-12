package mh.bamgr.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import mh.bamgr.models.CategoryBean;
import mh.bamgr.models.UserBean;
import mh.bamgr.utils.DatabaseManipulator;
import mh.bamgr.utils.MD5;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/userloginservlet")
public class UserLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(UserLoginServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserLoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		session.setMaxInactiveInterval(80);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		String uname = request.getParameter("name");
		String upassword = request.getParameter("password");
		UserBean u = null;
		if (user != null && uname == null) {
			u = user;
		} else {
			u = DatabaseManipulator.getUser(uname);
		}
		if (u != null && u.getPassword().equals(MD5.getHexHashString(upassword))) {
			logger.warn("Sign in succeed.");
			request.getSession().setAttribute("user", u);
			//
			request.getRequestDispatcher("main.jsp").forward(request, response);
		} else {
			logger.warn("Error when sign in.");
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
