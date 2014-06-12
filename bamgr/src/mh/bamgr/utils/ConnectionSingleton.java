package mh.bamgr.utils;

import java.sql.DriverManager;

import java.sql.Connection;

@Deprecated
public class ConnectionSingleton {
	private static Connection conn;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/bills?user=root&password=root");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		return conn;
	}
}
