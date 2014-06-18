package mh.bamgr.utils;

import java.util.ResourceBundle;

public class ConfigUtil {
	private static int numPerPage = 0;
	static{
		ResourceBundle resource = ResourceBundle.getBundle("common");
		String n=resource.getString("pagination.per.page");
		numPerPage = Integer.parseInt(n);
	}
	public static int getNumPerPage() {
		return numPerPage;
	}
	
	
}
