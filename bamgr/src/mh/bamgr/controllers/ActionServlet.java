package mh.bamgr.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import mh.bamgr.common.AjaxResult;
import mh.bamgr.common.MonthlySumObject;
import mh.bamgr.models.CategoryBean;
import mh.bamgr.models.ExpenseIncomeBean;
import mh.bamgr.models.UserBean;
import mh.bamgr.utils.DatabaseManipulator;

/**
 * Servlet implementation class ActionServlet
 */
@WebServlet("/actionservlet")
public class ActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ActionServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		UserBean user = (UserBean) session.getAttribute("user");
		
		String at = request.getParameter("actiontype");
		
		if ("save".equals(at)) {
			String jsonData = request.getParameter("jsondata");
			Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();
			ExpenseIncomeBean data = gson.fromJson(jsonData, ExpenseIncomeBean.class);
			AjaxResult ar;
			if (DatabaseManipulator.upsertExpenseIncome(data, user)) {
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
		} else if("remove".equals(at)) {
			String id = request.getParameter("id");
			AjaxResult ar;
			if (DatabaseManipulator.removeExpenseIncome(Integer.parseInt(id))) {
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
		}else if("retrieve".equals(at)) {
			String fr = request.getParameter("fr");
			String to = request.getParameter("to");
			int pageNum = Integer.parseInt(request.getParameter("page"));
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			try {
		 
				Date dtfr = formatter.parse(fr);
				Date dtto = formatter.parse(to);
				
				List<ExpenseIncomeBean> ls = DatabaseManipulator.getExpenseIncome(new java.sql.Date(dtfr.getTime()), new java.sql.Date(dtto.getTime()), pageNum, user);
				Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();
				String data = gson.toJson(ls, new TypeToken<List<ExpenseIncomeBean>>(){}.getType());
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(data); 
		 
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if("init".equals(at)) {
			List<CategoryBean> ls = DatabaseManipulator.getCategories();
			Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();
			String data = gson.toJson(ls, new TypeToken<List<CategoryBean>>(){}.getType());
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(data); 
		} else if("monthlysumchart".equals(at)) {
			List<MonthlySumObject> ls = DatabaseManipulator.getMonthlySum(user);
			Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy").create();
			String data = gson.toJson(ls, new TypeToken<List<MonthlySumObject>>(){}.getType());
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(data); 
		} else if("retrievecount".equals(at)) {
			String fr = request.getParameter("fr");
			String to = request.getParameter("to");
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			try {		 
				Date dtfr = formatter.parse(fr);
				Date dtto = formatter.parse(to);
				response.setContentType("text/plain");
				response.setCharacterEncoding("UTF-8");
				int data = DatabaseManipulator.getExpenseIncomeCount(new java.sql.Date(dtfr.getTime()), new java.sql.Date(dtto.getTime()), user);
				response.getWriter().write(Integer.toString(data)); 
		 
			} catch (ParseException e) {
				e.printStackTrace();
			}
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
