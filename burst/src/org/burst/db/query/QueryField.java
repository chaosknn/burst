package org.burst.db.query;

import java.util.ArrayList;

import org.burst.db.config.DbConfig;
import org.burst.db.tbl.base.TblBase;
import org.burst.db.tbl.base.TblBaseUtil;


public class QueryField {
	
	public static final int EQUAL = 0;
	public static final int BETWEEN = 1;
	public static final int LESSTHAN = 2;
	public static final int BIGTHAN = 3;
	public static final int LIKE = 4;
	public static final int NOTEQUAL = 6;
	public static final int IN = 7;
	public static final int NOTIN = 8;
	public static final int ISNULL = 9;
	public static final int NOTNULL = 10;
	
	private String fieldName = "";
	private String tableName = "";
	private int fieldType = 0;
	
	private int matchType = 0;
	private Object value = null;
	private Object min = null;
	private Object max = null;
	private boolean inclusiveMin = false;
	private boolean inclusiveMax = false;
	private boolean caseSensitive = true;
	//if mysql dateformat share be "%Y-%m-%d"
	//if sql server, dateformat, 101:mm/dd/yyyy ; 121:yyyy-mm-dd hh:mm:ss[.fff]
	//if oracle or db2 use "yyyy/mm/dd"
	private String dateFormat = "yyyy/MM/dd";
	private ArrayList inList = new ArrayList();
	
	public QueryField(TblBase tbl, String fieldName) throws Exception{
		this.tableName = tbl.getTblName();
		this.fieldName = fieldName;
		fieldType = tbl.getFieldType(fieldName);
	}
	
	public String getPSStr() throws Exception{
		StringBuffer sb = new StringBuffer(" ");
		switch(matchType){
			case EQUAL:
				//sql server, double data, can not user =
				if(fieldType == TblBaseUtil.FIELD_DOUBLE && DbConfig.isSqlserver()){
					sb.append(" abs(");
					sb.append(appendFunction());
					sb.append(" - ");
					sb.append(appendFunction(false));
					sb.append(") < 0.000001 ");
				}else{
					sb.append(appendFunction());
					sb.append(" = ");
					sb.append(appendFunction(false));
				}
				
				break;
			case BETWEEN:
				sb.append(appendFunction());
				sb.append(" between ");
				sb.append(appendFunction(false));
				sb.append(" and ");
				sb.append(appendFunction(false));
				
				break;
			case LESSTHAN:
				sb.append(appendFunction());
				if(inclusiveMin){
					sb.append(" <= ");
				}else{
					sb.append(" < ");
				}
				sb.append(appendFunction(false));
				
				break;
			case BIGTHAN:
				sb.append(appendFunction());
				if(inclusiveMax){
					sb.append(" >= ");
				}else{
					sb.append(" > ");
				}
				sb.append(appendFunction(false));
				
				break;
			case LIKE:
				sb.append(fieldName);
				sb.append(" like ");
				sb.append("?");
				
				break;
				
			case NOTEQUAL:
				//sql server, double data, can not user =
				if(fieldType == TblBaseUtil.FIELD_DOUBLE && DbConfig.isSqlserver()){
					sb.append(" abs(");
					sb.append(appendFunction());
					sb.append(" - ");
					sb.append(appendFunction(false));
					sb.append(") > 0.000001 ");
				}else{
					sb.append(appendFunction());
					sb.append(" <> ");
					sb.append(appendFunction(false));
				}
				
				break;
				
			case IN:
				sb.append(appendFunction());
				sb.append(" in (");
				for(int i=0;i<inList.size();i++){
					if(i>0){
						sb.append(",");
					}
					sb.append(appendFunction(false));
				}
				sb.append(")");
				
				break;
				
			case NOTIN:
				sb.append(appendFunction());
				sb.append(" not in (");
				for(int i=0;i<inList.size();i++){
					if(i>0){
						sb.append(",");
					}
					sb.append(appendFunction(false));
				}
				sb.append(")");
				
				break;
			case ISNULL:
				sb.append(fieldName);
				sb.append(" is null ");
				
				break;
				
			case NOTNULL:
				sb.append(fieldName);
				sb.append(" is not null ");
				
				break;
			
		}
		
		return sb.toString();
	}
	
	private String appendFunction(){
		return appendFunction(true);
	}
	
	//对日期格式或者大小写不敏感的字符串添加函数
	private String appendFunction(boolean isFieldName){
		String fieldWithTbl = tableName + "." + fieldName;
		if(!isFieldName){
			fieldWithTbl = "?";
		}
		if(TblBaseUtil.FIELD_DATE == fieldType){
			boolean isMysql = false;
			boolean isSqlServer = false;
			try{
				isMysql = DbConfig.isMysql();
				isSqlServer = DbConfig.isSqlserver();
			}catch(Exception e){}
			if(isMysql){
				return " date_format(" + fieldWithTbl + ",'" + dateFormat + "')";
			}else if(isSqlServer){
				return " convert(varchar(30)," + fieldWithTbl + "," + dateFormat + ")";
			}else{
				return " to_char(" + fieldWithTbl + ",'" + dateFormat + "')";
			}
		}else if(TblBaseUtil.FIELD_STRING == fieldType){
			if(!caseSensitive){
				fieldWithTbl = "upper(" + fieldWithTbl + ")";
			}
		}
		
		return fieldWithTbl;
	}
	
	public void appendInValue(Object value){
		inList.add(value);
	}
	
	public void clearInValue(){
		inList.clear();
	}
	
	public String getInString(){
		StringBuffer sb = new StringBuffer();

		for(int i=0;i<inList.size();i++){
			if(i>0){
				sb.append(",");
			}
			if(fieldType == TblBaseUtil.FIELD_STRING){
				sb.append("'");
			}
			
			sb.append(inList.get(i));
			
			if(fieldType == TblBaseUtil.FIELD_STRING){
				sb.append("'");
			}
		}

System.out.println("inStr=" + sb.toString());		
		return sb.toString();
	}
	
	public int getPSQMarkCount(){
		switch(matchType){
			case EQUAL:
				return 1;

			case BETWEEN:
				return 2;

			case LESSTHAN:
				return 1;

			case BIGTHAN:
				return 1;
				
			case LIKE:
				return 1;
				
			case NOTEQUAL:
				return 1;
				
			case IN:
				return inList.size();
				
			case NOTIN:
				return inList.size();
				
			case ISNULL:
				return 0;
				
			case NOTNULL:
				return 0;
			
		}
		
		return 0;
	}
	
	public Object getPSValue(int idx){
		switch(matchType){
			case EQUAL:
				return value;

			case BETWEEN:
				if(idx == 0){
					return min;
				}else{
					return max;
				}

			case LESSTHAN:
				return max;
				
			case BIGTHAN:
				return min;
				
			case LIKE:
				return value;
				
			case NOTEQUAL:
				return value;
				
			case IN:
				return inList.get(idx);
				
			case NOTIN:
				return inList.get(idx);
				
			case ISNULL:
				return 0;
				
			case NOTNULL:
				return 0;
			
		}
		
		return 0;
	}
	
	public String getSqlStr(){
		StringBuffer sb = new StringBuffer(" ");
		switch(matchType){
			case EQUAL:
				sb.append(appendFunction());
				sb.append(" = ");
				
				if(fieldType == TblBaseUtil.FIELD_STRING){
					sb.append("'");
					sb.append(value);
					sb.append("'");
				}else{
					sb.append(value);
				}
				
				break;
			case BETWEEN:
				sb.append(appendFunction());
				sb.append(" between ");
				if(fieldType == TblBaseUtil.FIELD_STRING){
					sb.append("'");
					sb.append(min);
					sb.append("'");
				}else{
					sb.append(min);
				}
				sb.append(" and ");
				if(fieldType == TblBaseUtil.FIELD_STRING){
					sb.append("'");
					sb.append(max);
					sb.append("'");
				}else{
					sb.append(max);
				}
				
				break;
			case LESSTHAN:
				sb.append(appendFunction());
				if(inclusiveMin){
					sb.append(" <= ");
				}else{
					sb.append(" < ");
				}
				if(fieldType == TblBaseUtil.FIELD_STRING){
					sb.append("'");
					sb.append(max);
					sb.append("'");
				}else{
					sb.append(max);
				}
				
				break;
			case BIGTHAN:
				sb.append(appendFunction());
				if(inclusiveMax){
					sb.append(" >= ");
				}else{
					sb.append(" > ");
				}
				if(fieldType == TblBaseUtil.FIELD_STRING){
					sb.append("'");
					sb.append(min);
					sb.append("'");
				}else{
					sb.append(min);
				}
				
				break;
			case LIKE:
				sb.append(fieldName);
				sb.append(" like ");
				sb.append("'");
				sb.append(value);
				sb.append("'");
				
				break;
				
			case NOTEQUAL:
				sb.append(appendFunction());
				sb.append(" <> ");
				if(fieldType == TblBaseUtil.FIELD_STRING){
					sb.append("'");
					sb.append(value);
					sb.append("'");
				}else{
					sb.append(value);
				}
				
				break;
				
			case IN:
				sb.append(appendFunction());
				sb.append(" in (");
				sb.append(getInString());
				sb.append(")");
				
				break;
				
			case NOTIN:
				sb.append(appendFunction());
				sb.append(" not in (");
				sb.append(getInString());
				sb.append(")");
				
				break;
				
			case ISNULL:
				sb.append(fieldName);
				sb.append(" is null ");
				
				break;
				
			case NOTNULL:
				sb.append(fieldName);
				sb.append(" is not null ");
				
				break;
		}
		
		return sb.toString();
	}
	
	public int getFieldType() {
		return fieldType;
	}
	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
	public int getMatchType() {
		return matchType;
	}
	public void setMatchType(int matchType) {
		this.matchType = matchType;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Object getMin() {
		return min;
	}
	public void setMin(Object min) {
		this.min = min;
	}
	public Object getMax() {
		return max;
	}
	public void setMax(Object max) {
		this.max = max;
	}
	public boolean isInclusiveMin() {
		return inclusiveMin;
	}
	public void setInclusiveMin(boolean inclusiveMin) {
		this.inclusiveMin = inclusiveMin;
	}
	public boolean isInclusiveMax() {
		return inclusiveMax;
	}
	public void setInclusiveMax(boolean inclusiveMax) {
		this.inclusiveMax = inclusiveMax;
	}
	public boolean isCaseSensitive() {
		return caseSensitive;
	}
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public ArrayList getInList() {
		return inList;
	}

	public void setInList(ArrayList inList) {
		this.inList = inList;
	}

}
