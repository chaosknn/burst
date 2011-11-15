package org.burst.db.config;

public class AppPathUtil {
	
	private static String appPath = "";
	private static String sep = "";
	
	private static boolean inited = false;
	
	public static void init(boolean bForce) throws Exception{
		if(inited && !bForce){
			return;
		}
		appPath = getAppPath();
		
		//System.out.println("appPath: "+appPath);
		
		inited = true;
	}
	
	public static String getAppPath() {
		if(appPath.length() > 0){
			return appPath;
		}
		AppPathUtil c = new AppPathUtil();
		//System.out.println("getAppPath 0");
		String path = c.getClass().getResource("/").getPath();
		path = path.replace("%20", " ");
		//System.out.println("path=" + path);
		
		int idx = path.indexOf("/com/");
		int idxJar = path.indexOf(".jar");
		//jar file:/E:/workspace/F10/bin/F10.jar
		//class file/E:/workspace/ACore/bin/
		//under tomcat,jar file/D:/tomcat/webapps/A1/WEB-INF/classes/
		if(idxJar >0){
			path = path.substring(6, idx);
			int idxLast = getLastIndex(path, "/");
			path = path.substring(0, idxLast);
		}else if(idx >0){
			path = path.substring(1, idx);
		}else{
			path = path.substring(1);
		}
		
		sep = getFileSeparator();
		if (sep.equals("\\")) {
			path = path.replace("/", "\\");
		}

		//System.out.println("getAppPath 1 path=" + path);
		return path;
	}
	
	public static String getFileSeparator() {
		if (sep.length() > 0) {
			return sep;
		}
		sep = System.getProperty("file.separator");
		return sep;
	}
	
	public static int getLastIndex(String source, String finder){
		if(source == null){
			return -1;
		}
		
		StringBuffer sb = new StringBuffer();
		int len = source.length();
		for(int i=0;i<len;i++){
			sb.append(source.charAt(len-i-1));
		}
		return len - sb.indexOf(finder);
	}
	
}