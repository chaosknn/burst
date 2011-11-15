package org.burst.db.tbl.base;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

import org.burst.db.config.DbConfig;
import org.burst.db.conn.DbBase;


public class TblBaseUtil {
	
	public static final String SEQUENCE_MARK = "SEQ_";
	public static final String INDEX_MARK = "IDX_";
	public static final String PARTITION_MARK = "PAR_";
	public static final String LPT_MARK = "(";
	public static final String RPT_MARK = ")";
	public static final String SEP = ",";
	
	//we use nvarchar2
	public static final String VARCHAR_NAME = "NVARCHAR2";
	
	//only use int,double,timestamp,varchar,blob five types
	//in oracle£¬db2 there are no bool field £¬we use int field 1,0 to save bool
	public static final int FIELD_INT = 1;
	public static final int FIELD_DOUBLE = 2;
	public static final int FIELD_DATE = 3;
	public static final int FIELD_STRING = 4;
	public static final int FIELD_BOOL = 5;
	public static final int FIELD_BLOB = 6;
	
	//field type index
	public static final int FIELD_TYPE_IDX = 1;
	
	//max length
	public static final int MAX_LENGTH = 1000;
	
	public static final String ORACLE_INT = "int";
	public static final String ORACLE_DOUBLE = "number";
	public static final String ORACLE_DATE = "timestamp";
	public static final String ORACLE_STRING = "nvarchar2";
	public static final String ORACLE_BLOB = "blob";
	
	public static final String DB2_INT = "int";
	public static final String DB2_DOUBLE = "double";
	public static final String DB2_DATE = "timestamp";
	public static final String DB2_STRING = "varchar";
	public static final String DB2_BLOB = "blob";
	
	//sql server can't use timestamp, but datetime
	//sql server has no blob field, we use image
	public static final String SQLSERVER_INT = "int";
	public static final String SQLSERVER_DOUBLE = "real";
	public static final String SQLSERVER_DATE = "datetime";
	public static final String SQLSERVER_STRING = "nvarchar";
	public static final String SQLSERVER_BLOB = "image";
	
	//mysql can't use timestamp, but datetime
	public static final String MYSQL_INT = "int";
	public static final String MYSQL_DOUBLE = "real";
	public static final String MYSQL_DATE = "datetime";
	public static final String MYSQL_STRING = "nvarchar";
	public static final String MYSQL_BLOB = "blob";
	
	public static void setPSValue(DbBase db, int idx, int type, Object value) throws Exception{
		if(FIELD_INT == type){
			if(value != null){
				db.setPSInt(idx, (Integer)value);
			}else{
				db.setPSNull(idx, Types.INTEGER);
			}
		}else if(FIELD_DOUBLE == type){
			
			if(value != null){
				if(value instanceof Integer){
					db.setPSDouble(idx, 0.0 + (Integer)value);
				}else if(value instanceof Float){
					db.setPSDouble(idx, 0.0 + (Float)value);
				}else{
					db.setPSDouble(idx, (Double)value);
				}
			}else{
				db.setPSNull(idx, Types.DOUBLE);
			}
		}else if(FIELD_DATE == type){
			if(value != null){
				db.setPSDate(idx, (Date)value);
			}else{
				db.setPSNull(idx, Types.TIMESTAMP);
			}
		}else if(FIELD_STRING == type){
			
			if(value != null){
				db.setPSString(idx, (String)value);
			}else{
				db.setPSNull(idx, Types.VARCHAR);
			}
		}else if(FIELD_BOOL == type){
			if(value != null){
				db.setPSInt(idx, boolToInt((Boolean)value));
			}else{
				db.setPSNull(idx, Types.INTEGER);
			}
		}else if(FIELD_BLOB == type){
			
			if(value != null){
				db.setPSBlob(idx, (MyBlob)value);
			}else{
				db.setPSNull(idx, Types.BLOB);
			}
		}else{
			throw new Exception("value type not valid");
		}
	}
	
	//default, multiTable=false
	public static void setTblValue(ResultSet rs, TblBase tbl, int idx) throws Exception{
		setTblValue(rs, tbl, idx, false);
	}
	
	public static void setTblValue(ResultSet rs, TblBase tbl, int idx, boolean multiTable) throws Exception{
		int type = tbl.getFieldType(idx);
		String fieldName = tbl.getFieldName(idx);
		String sqlFieldName = fieldName;
		if(multiTable){
			sqlFieldName = tbl.getTblName() + "_" + fieldName;
		}
		
		if(FIELD_INT == type){
			tbl.setInt(fieldName, rs.getInt(sqlFieldName));
			//simple data, such as int, if field in db is null,tbl.setInt will return 0
			//we need to use wasNull after rs.getInt
			if(rs.wasNull()){
				tbl.setInt(fieldName, null);
			}
		}else if(FIELD_DOUBLE == type){
			tbl.setDouble(fieldName, rs.getDouble(sqlFieldName));
			if(rs.wasNull()){
				tbl.setDouble(fieldName, null);
			}
		}else if(FIELD_DATE == type){
			tbl.setDate(fieldName, timestampToUtilDate(rs.getTimestamp(sqlFieldName)));
		}else if(FIELD_STRING == type){
			tbl.setString(fieldName, rs.getString(sqlFieldName));
		}else if(FIELD_BOOL == type){
			tbl.setBool(fieldName, intToBool(rs.getInt(sqlFieldName)));
			if(rs.wasNull()){
				tbl.setBool(fieldName, null);
			}
		}else if(FIELD_BLOB == type){
			Blob blob = rs.getBlob(sqlFieldName);
			if(blob == null){
				tbl.setBlob(fieldName, null);
			}else{
				MyBlob mblob = new MyBlob();
				InputStream in = rs.getBinaryStream(sqlFieldName);
	
				byte[] b = readStreamBytes(in);
				mblob.setValue(b);
				tbl.setBlob(fieldName, mblob);
			}
		}else{
			throw new Exception("value type not valid");
		}
	}
	
	public static byte[] readStreamBytes(InputStream in) throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    int b=0;
	    while( (b = in.read())!=-1)
	        baos.write(b);
	    return baos.toByteArray();
	}
	
	public static String getOracleColumnTypeDef(int type, int size) throws Exception{
		if(FIELD_INT == type){
			return ORACLE_INT;
		}else if(FIELD_DOUBLE == type){
			return ORACLE_DOUBLE;
		}else if(FIELD_DATE == type){
			return ORACLE_DATE;
		}else if(FIELD_STRING == type){
			return ORACLE_STRING + "(" + size + ")";
		}else if(FIELD_BOOL == type){
			return ORACLE_INT;
		}else if(FIELD_BLOB == type){
			return ORACLE_BLOB;
		}else{
			throw new Exception("value type not valid");
		}
	}
	

	public static String getDb2ColumnTypeDef(int type, int size) throws Exception{
		if(FIELD_INT == type){
			return DB2_INT;
		}else if(FIELD_DOUBLE == type){
			return DB2_DOUBLE;
		}else if(FIELD_DATE == type){
			return DB2_DATE;
		}else if(FIELD_STRING == type){
			return DB2_STRING + "(" + size + ")";
		}else if(FIELD_BOOL == type){
			return DB2_INT;
		}else if(FIELD_BLOB == type){
			return DB2_BLOB;
		}else{
			throw new Exception("value type not valid");
		}
	}
	
	public static String getSqlserverColumnTypeDef(int type, int size) throws Exception{
		if(FIELD_INT == type){
			return SQLSERVER_INT;
		}else if(FIELD_DOUBLE == type){
			return SQLSERVER_DOUBLE;
		}else if(FIELD_DATE == type){
			return SQLSERVER_DATE;
		}else if(FIELD_STRING == type){
			return SQLSERVER_STRING + "(" + size + ")";
		}else if(FIELD_BOOL == type){
			return SQLSERVER_INT;
		}else if(FIELD_BLOB == type){
			return SQLSERVER_BLOB;
		}else{
			throw new Exception("value type not valid");
		}
	}
	
	public static String getMysqlColumnTypeDef(int type, int size) throws Exception{
		if(FIELD_INT == type){
			return MYSQL_INT;
		}else if(FIELD_DOUBLE == type){
			return MYSQL_DOUBLE;
		}else if(FIELD_DATE == type){
			return MYSQL_DATE;
		}else if(FIELD_STRING == type){
			return MYSQL_STRING + "(" + size + ")";
		}else if(FIELD_BOOL == type){
			return MYSQL_INT;
		}else if(FIELD_BLOB == type){
			return MYSQL_BLOB;
		}else{
			throw new Exception("value type not valid");
		}
	}
	
	public static Boolean intToBool(Integer value){
		if(value== null){
			return null;
		}
		
		return value==1;
	}
	
	public static Integer boolToInt(Boolean value){
		if(value== null){
			return null;
		}
		
		if(value){
			return 1;
		}
		return 0;
	}
	
	//string to list
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
	
	public static java.util.Date toUtilDate(java.sql.Date date){
		if(date==null){
			return null;
		}
		return new java.util.Date(date.getTime());
	}
	
	public static java.sql.Date toSqlDate(java.util.Date date){
		if(date==null){
			return null;
		}
		return new java.sql.Date(date.getTime());
	}
	
	public static java.util.Date timestampToUtilDate(Timestamp ts){
		if(ts==null){
			return null;
		}
		return new java.util.Date(ts.getTime());
	}
	
	public static Timestamp utilDateToTimestamp(java.util.Date date){
		if(date==null){
			return null;
		}
		return new Timestamp(date.getTime());
	}
	
	//strListToIntList
	public static ArrayList<Integer> strListToIntList(ArrayList<String> strList) throws Exception{
		ArrayList<Integer> v = new ArrayList<Integer>();
		if(strList == null || strList.size() < 1){
			return v;
		}
		
		for(int i=0;i<strList.size();i++){
			v.add(Integer.parseInt((String)strList.get(i)));
		}
		
		return v;
	}
	
	//list to string
	public static String ListToStr(ArrayList<String> v, String sep){
		String result = "";
		if(v == null || v.size() == 0){
			return result;
		}
		
		for(int i=0;i<v.size();i++){
			result += v.get(i);
			if(i<v.size()-1){
				result += sep;
			}
		}
		return result;
	}
	
	public static int getPartitionIdx(TblBase tbl, int colValue){
		int c = (int)Math.floor(colValue/DbConfig.getPartitionSize());
		
		if(c >= DbConfig.getPartitionCount()){
			return DbConfig.getPartitionCount()-1;
		}else if(c>=0){
			return c;
		}else{
			return -1;
		}
	}
	
	public static String getTblNameWithPartition(TblBase tbl) throws Exception{
		
		return tbl.getTblName();
		
		/*
		if(!DbConfig.usePartition()){
			return tbl.getTblName();
		}
		
		if(!tbl.isPartition()){
			return tbl.getTblName();
		}
		
		int idx = tbl.getFieldIdx(tbl.getPartition_col());
		if(tbl.getValueList().get(idx) == null){
			return tbl.getTblName();
		}else{
			Integer value = (Integer)tbl.getValueList().get(idx);
			if(value == null || value == 0){
				return tbl.getTblName();
			}
			return tbl.getTblName() + " partition(" + PARTITION_MARK + getPartitionIdx(tbl,value) + ")";
		}*/
	}
	
	//hide table names, and table field names
	public static String hashString(String str){
		if(!DbConfig.hideDbName()){
			return str;
		}
		
		if(str == null || str.length() ==0){
			return str;
		}
		
		StringBuffer sb = new StringBuffer();
		char c;
		for(int i=0;i<str.length();i++){
			c = str.charAt(i);
			if(c>='0' && c<='9'){
				c=(char)('9'-(c-'0'));
			}
			if(c>='a' && c<='z'){
				c=(char)('z'-(c-'a'));
			}
			if(c>='A' && c<='Z'){
				c=(char)('Z'-(c-'A'));
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	//unhash string
	public static String unhashString(String str){
		return hashString(str);
	}

	
}
