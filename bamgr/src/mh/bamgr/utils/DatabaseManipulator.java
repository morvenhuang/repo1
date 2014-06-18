package mh.bamgr.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import mh.bamgr.common.MonthlySumObject;
import mh.bamgr.controllers.UserLoginServlet;
import mh.bamgr.models.ExpenseIncomeBean;
import mh.bamgr.models.CategoryBean;
import mh.bamgr.models.UserBean;

public class DatabaseManipulator {

	static Logger logger = Logger.getLogger(DatabaseManipulator.class);

	public static UserBean getUser(String uname) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFromPool.getConnection();
			stmt = conn.prepareStatement("SELECT * FROM users where name=?");
			stmt.setString(1, uname);
			rs = stmt.executeQuery();
			if (rs.next()) {
				do {
					UserBean u = new UserBean();
					u.setId(rs.getInt("id"));
					u.setName(rs.getString("name"));
					u.setPassword(rs.getString("password"));
					u.setDescription(rs.getString("description"));
					return u;
				} while (rs.next());
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			closeResources(rs, stmt, conn);
		}
	}

	private static void closeResources(ResultSet rs, PreparedStatement stmt, Connection conn) {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException ignore) {
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (SQLException ignore) {
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException ignore) {
			}
	}

	public static boolean addUser(UserBean u) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFromPool.getConnection();
			stmt = conn.prepareStatement("INSERT INTO users(name,password,description) values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, u.getName());
			stmt.setString(2, u.getPassword());
			stmt.setString(3, u.getDescription());
			stmt.execute();

			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				u.setId(rs.getInt(1));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources(rs, stmt, conn);
		}
	}

	public static boolean deleteUser(UserBean u) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFromPool.getConnection();
			stmt = conn.prepareStatement("DELETE FROM users WHERE id=?");
			stmt.setInt(1, u.getId());
			stmt.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources(rs, stmt, conn);
		}
	}

	public static boolean updateUser(UserBean u) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFromPool.getConnection();
			stmt = conn.prepareStatement("UPDATE users set name=?,password=?,description=? WHERE id=?");
			stmt.setString(1, u.getName());
			stmt.setString(2, u.getPassword());
			stmt.setString(3, u.getDescription());
			stmt.setInt(4, u.getId());
			stmt.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources(rs, stmt, conn);
		}
	}

	public static List<CategoryBean> getExpenseCategories() {
		return getCategories(false);
	}

	public static List<CategoryBean> getIncomeCategories() {
		return getCategories(true);
	}

	public static List<CategoryBean> getCategories(boolean... isIncome) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<CategoryBean> categories = new ArrayList<CategoryBean>();
		try {
			conn = ConnectionFromPool.getConnection();
			if (isIncome.length == 0) {
				stmt = conn.prepareStatement("SELECT * FROM categories");
			} else if (isIncome.length == 1) {
				stmt = conn.prepareStatement("SELECT * FROM categories where is_income=?");
				stmt.setInt(1, isIncome[0] ? 1 : 0);
			} else {
				// TODO
			}

			rs = stmt.executeQuery();
			if (rs.next()) {
				do {
					CategoryBean t = new CategoryBean();
					t.setId(rs.getInt("id"));
					t.setName(rs.getString("name"));
					t.setDescription(rs.getString("description"));
					t.setIsIncome(rs.getInt("is_income"));
					categories.add(t);
				} while (rs.next());
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			closeResources(rs, stmt, conn);
		}
		return categories;
	}

	public static boolean upsertExpenseIncome(ExpenseIncomeBean s, UserBean u) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFromPool.getConnection();
			if (s.getId() == -1) {
				stmt = conn.prepareStatement("INSERT INTO expense_income(category_id,amount, ei_date, description, user_id) values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
				stmt.setInt(1, s.getCategoryId());
				stmt.setInt(2, s.getAmount());
				stmt.setDate(3, s.getEiDate());
				stmt.setString(4, s.getDescription());
				stmt.setInt(5, u.getId());
				stmt.execute();

				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					s.setId(rs.getInt(1));
				}
			} else {
				stmt = conn.prepareStatement("update expense_income set category_id=?,amount=?, ei_date=?, description=? where id=?");
				stmt.setInt(1, s.getCategoryId());
				stmt.setInt(2, s.getAmount());
				stmt.setDate(3, s.getEiDate());
				stmt.setString(4, s.getDescription());
				stmt.setInt(5, s.getId());
				stmt.execute();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources(rs, stmt, conn);
		}
	}

	public static List<ExpenseIncomeBean> getExpenseIncome(Date fr, Date to, int pageNum, UserBean u) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ExpenseIncomeBean> eis = new ArrayList<ExpenseIncomeBean>();
		try {
			conn = ConnectionFromPool.getConnection();
			stmt = conn
					.prepareStatement("SELECT ei.id, ei.category_id, c.name as category_name, ei.amount, ei.ei_date, ei.description, c.is_income FROM expense_income ei join categories c on ei.category_id=c.id where ei.user_id=? and ei.ei_date between ? and ? order by ei.ei_date limit ?,?");
			stmt.setInt(1, u.getId());
			stmt.setDate(2, fr);
			stmt.setDate(3, to);
			stmt.setInt(4, (pageNum - 1) * ConfigUtil.getNumPerPage());
			stmt.setInt(5, ConfigUtil.getNumPerPage());
			rs = stmt.executeQuery();
			if (rs.next()) {
				do {
					ExpenseIncomeBean ei = new ExpenseIncomeBean();
					ei.setId(rs.getInt("id"));
					ei.setCategoryId(rs.getInt("category_id"));
					ei.setCategoryName(rs.getString("category_name"));
					ei.setDescription(rs.getString("description"));
					ei.setAmount(rs.getInt("amount"));
					ei.setEiDate(rs.getDate("ei_date"));
					ei.setIsIncome(rs.getInt("is_income"));
					eis.add(ei);
				} while (rs.next());
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			closeResources(rs, stmt, conn);
		}
		return eis;
	}
	
	public static boolean removeExpenseIncome(int id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFromPool.getConnection();
			stmt = conn.prepareStatement("DELETE FROM expense_income WHERE id=?");
			stmt.setInt(1, id);
			stmt.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources(rs, stmt, conn);
		}
	}

	public static int getExpenseIncomeCount(Date fr, Date to, UserBean u) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFromPool.getConnection();
			stmt = conn.prepareStatement("SELECT count(*) cnt FROM expense_income where user_id=? and ei_date between ? and ? ");
			stmt.setInt(1, u.getId());
			stmt.setDate(2, fr);
			stmt.setDate(3, to);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("cnt");
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			closeResources(rs, stmt, conn);
		}
	}

	public static List<MonthlySumObject> getMonthlySum(UserBean u) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<MonthlySumObject> ls = new ArrayList<MonthlySumObject>();
		try {
			conn = ConnectionFromPool.getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("select dt, ");
			sb.append("ifnull(sum(case when is_income=1 then amount end),0) income, ");
			sb.append("ifnull(sum(case when is_income=0 then amount end), 0) expense ");
			sb.append("from ");
			sb.append("(");
			sb.append("    select date_format(ei_date,'%Y/%m') dt, amount, is_income from expense_income ei ");
			sb.append("    join categories c ");
			sb.append("    on ei.category_id=c.id and user_id=?");
			sb.append(") t ");
			sb.append("group by dt ");
			stmt = conn.prepareStatement(sb.toString());
			stmt.setInt(1, u.getId());

			rs = stmt.executeQuery();
			if (rs.next()) {
				do {
					MonthlySumObject t = new MonthlySumObject();
					t.setYm(rs.getString("dt"));
					t.setExpense(rs.getInt("expense"));
					t.setIncome(rs.getInt("income"));
					ls.add(t);
				} while (rs.next());
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			closeResources(rs, stmt, conn);
		}
		return ls;
	}

}
