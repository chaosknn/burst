package org.burst.db.config;

import java.util.ArrayList;
import java.util.Properties;

public class DbConfig {

	public static final String DB_PROPERTIES = "db.properties";
	private static Properties prop = null;
	private static boolean inited = false;
	
	public static final String DB_ORACLE = "oracle";
	public static final String DB_DB2 = "db2";
	public static final String DB_SQLSERVER = "sqlserver";
	public static final String DB_MYSQL = "mysql";
	
	public static final String DB_TYPE = "db.type";
	public static final String DB_NAME = "db.name";
	public static final String DB_USER = "db.user";
	public static final String DB_PASSWORD = "db.password";
	public static final String DB_SCHEMA = "db.schema";
	public static final String DB_DRIVER = "db.jdbc.driver";
	public static final String DB_CONNECT_STRING = "db.connect.string";
	public static final String DB_POOL_MINSIZE = "db.pool.minsize";
	public static final String DB_POOL_MAXSIZE = "db.pool.maxsize";
	public static final String DB_POOL_ADDSIZE = "db.pool.addsize";
	
	public static final String DB_PARTITION_USE = "db.partition.use";
	public static final String DB_PARTITION_SIZE = "db.partition.size";
	public static final String DB_PARTITION_COUNT = "db.partition.count";
	
	public static final String DB_HIDENAME = "db.hideName";
	public static final String DB_TABLESPACES = "db.tableSpaces";
	public static final String DB_INDEXSPACE = "db.indexSpace";
	
	public static final String LOG4J_PROPERTIES = "log4j.properties";
	
	public static void init(boolean bForce) throws Exception{
		if(inited && !bForce){
			return;
		}
		prop = PropertiesUtil.loadProp(DB_PROPERTIES);
		inited = true;

	}
	
	public static String getProperty(String key) throws Exception{
		init(false);
		return prop.getProperty(key);
	}
	
	public static String getDbName() throws Exception{
		return getProperty(DB_NAME);
	}
	
	public static String getDbUser() throws Exception{
		return getProperty(DB_USER);
	}
	
	public static String getDbPassword() throws Exception{
		return getProperty(DB_PASSWORD);
	}
	
	public static String getDbSchema() throws Exception{
		return getProperty(DB_SCHEMA);
	}
	
	public static String getDbDriver() throws Exception{
		return getProperty(DB_DRIVER);
	}
	
	public static int getDbPoolMinSize() throws Exception{
		return Integer.parseInt(getProperty(DB_POOL_MINSIZE));
	}
	
	public static int getDbPoolMaxSize() throws Exception{
		return Integer.parseInt(getProperty(DB_POOL_MAXSIZE));
	}
	
	public static int getDbPoolAddSize() throws Exception{
		return Integer.parseInt(getProperty(DB_POOL_ADDSIZE));
	}
	
	public static String getDbConnectString() throws Exception{
		return getProperty(DB_CONNECT_STRING);
	}
	
	public static ArrayList<String> getDbTableSpaces() throws Exception{
		String str = getProperty(DB_TABLESPACES);
		return strToList(str, ",");
	}
	
	public static String getDbIndexSpace() throws Exception{
		return getProperty(DB_INDEXSPACE);
	}
	
	public static boolean usePartition(){
		try{
			return "true".equalsIgnoreCase(getProperty(DB_PARTITION_USE));
		}catch(Exception e){
			return true;
		}
	}
	
	public static int getPartitionSize(){
		try{
			return Integer.parseInt(getProperty(DB_PARTITION_SIZE));
		}catch(Exception e){
			return 100000;
		}
	}
	
	public static int getPartitionCount(){
		try{
			return Integer.parseInt(getProperty(DB_PARTITION_COUNT));
		}catch(Exception e){
			return 10;
		}
	}
	
	public static boolean hideDbName() {
		try{
			return !"false".equalsIgnoreCase(getProperty(DB_HIDENAME));
		}catch(Exception e){
			return true;
		}
	}
	
	public static String getLog4jProperties() throws Exception{
		return getProperty(LOG4J_PROPERTIES);
	}
	
	public static boolean isDb2() throws Exception{
		return DB_DB2.equalsIgnoreCase(getDbType());
	}
	
	public static boolean isOracle() throws Exception{
		return DB_ORACLE.equalsIgnoreCase(getDbType());
	}
	
	public static boolean isSqlserver() throws Exception{
		return DB_SQLSERVER.equalsIgnoreCase(getDbType());
	}
	
	public static boolean isMysql() throws Exception{
		return DB_MYSQL.equalsIgnoreCase(getDbType());
	}
	
	public static String getDbType() throws Exception{
		return getProperty(DB_TYPE);
	}
	
	public static ArrayList<String> strToList(String str, String sep){
		ArrayList<String> v = new ArrayList<String>();
		if(str == null || str.length() < 1){
			return v;
		}
		while(str.length() > 0) {
			String strItem = "";
			int idx = str.indexOf(sep);
			if(idx < 0){
				strItem = str;
				v.add(strItem);
				str = "";
			}else{
				strItem = str.substring(0,idx);
				v.add(strItem);
				str = str.substring(idx + sep.length());
			}
		}
		
		return v;
	}
}