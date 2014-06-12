package mh.bamgr.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mh.bamgr.models.UserBean;
import mh.bamgr.utils.DatabaseManipulator;
import mh.bamgr.utils.MD5;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class UserDetailServlet
 */
@WebServlet("/userdetailservlet")
public class UserDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(UserDetailServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserDetailServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String editType = request.getParameter("edittype");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String description = request.getParameter("description");
		if ("new".equals(editType)) {
			UserBean u = new UserBean();
			u.setName(name);
			u.setPassword(MD5.getHexHashString(password));
			u.setDescription(description);
			
			//
			if (DatabaseManipulator.addUser(u)) {
				logger.warn("New user was added.");
				request.getRequestDispatcher("succeed.jsp").forward(request, response);
			} else {
				logger.warn("Error when adding new user.");
				request.getRequestDispatcher("error.jsp").forward(request, response);
			}
		} else {

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
