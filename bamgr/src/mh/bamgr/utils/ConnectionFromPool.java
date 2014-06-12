package mh.bamgr.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ConnectionFromPool {
	private static DataSource ds;

	static {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/bamgrdb");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static Connection getConnection(){
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
