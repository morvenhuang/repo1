package mh.bamgr.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mh.bamgr.models.UserBean;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

	private ServletContext context;

	/**
	 * Default constructor.
	 */
	public AuthenticationFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		

		String uri = req.getRequestURI();

		HttpSession session = req.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;

		if (uri.endsWith("login.jsp")) {
			chain.doFilter(req, res);
		} else if (uri.endsWith("userdetail.jsp")) {
			chain.doFilter(req, res);
		} else if (uri.endsWith("userloginservlet")) {
			if (req.getParameter("name") != null && req.getParameter("password") != null) {
				chain.doFilter(req, res);
			} else {
				//res.sendRedirect(req.getServletContext().getContextPath() + "/login.jsp");
				sendRedirect(req, res, req.getServletContext().getContextPath() + "/login.jsp");
			}
		} else if (uri.endsWith("userdetailservlet")) {
			if (req.getParameter("name") != null && req.getParameter("password") != null) {
				chain.doFilter(req, res);
			} else {
				//res.sendRedirect(req.getServletContext().getContextPath() + "/userdetail.jsp");
				sendRedirect(req, res, req.getServletContext().getContextPath() + "/login.jsp");
			}
		} else {
			if (user == null) {
				//res.sendRedirect(req.getServletContext().getContextPath() + "/login.jsp");
				sendRedirect(req, res, req.getServletContext().getContextPath() + "/login.jsp");
			} else {
				chain.doFilter(req, res);
			}
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
	}

	private boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	private void sendRedirect(HttpServletRequest req, HttpServletResponse res, String path) throws IOException {
		if (isAjax(req)) {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			res.setHeader("Location", res.encodeRedirectURL(path));
			res.flushBuffer();
		} else {
			res.sendRedirect(path);
		}
	}

}
