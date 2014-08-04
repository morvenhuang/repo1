package mh.bamgr.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mh.bamgr.common.AjaxResult;
import mh.bamgr.models.CategoryBean;
import mh.bamgr.models.ExpenseIncomeBean;
import mh.bamgr.utils.DatabaseManipulator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class CategoryServlet
 */
@WebServlet("/categoryservlet")
public class CategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CategoryServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String at = request.getParameter("actiontype");
		if ("init".equals(at)) {
			List<CategoryBean> ls = DatabaseManipulator.getCategories();
			Gson gson = new GsonBuilder().create();
			String data = gson.toJson(ls, new TypeToken<List<CategoryBean>>() {
			}.getType());
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(data);
		} else if ("save".equals(at)) {
			String jsonData = request.getParameter("jsondata");
			Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();
			CategoryBean data = gson.fromJson(jsonData, CategoryBean.class);
			AjaxResult ar;
			if (DatabaseManipulator.upsertCategory(data)) {
				ar = new AjaxResult();
				ar.setStatus("success");
				ar.setMessage("");
				ar.setExtra(Integer.toString(data.getId()));
			} else {
				ar = new AjaxResult();
				ar.setStatus("error");
				ar.setMessage("Something wrong when manipulating database.");
			}
			//
			String j = new Gson().toJson(ar);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(j);
		}else if("remove".equals(at)) {
			String id = request.getParameter("id");
			AjaxResult ar;
			if (DatabaseManipulator.removeCategory(Integer.parseInt(id))) {
				ar = new AjaxResult();
				ar.setStatus("success");
				ar.setMessage("");
			} else {
				ar = new AjaxResult();
				ar.setStatus("error");
				ar.setMessage("Something wrong when manipulating database.");
			}
			//
			String j = new Gson().toJson(ar);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(j); 
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
