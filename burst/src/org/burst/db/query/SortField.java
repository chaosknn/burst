package org.burst.db.query;

import java.util.ArrayList;

public class SortField {

	private String tableName = "";
	private String fieldName = "";
	private boolean isAsc = true;
	
	public SortField(String tableName, String fieldName, boolean isAsc){
		this.tableName = tableName;
		this.fieldName = fieldName;
		this.isAsc = isAsc;
	}
	
	//getSortString
	public static String getSortString(ArrayList<SortField> sortFieldList) throws Exception{
		StringBuffer strBuf = new StringBuffer();
		
		if(sortFieldList==null || sortFieldList.size()==0){
			return "";
		}
		
		strBuf.append(" order by ");
		for(int i=0;i<sortFieldList.size();i++){
			if(i>0){
				strBuf.append(",");
			}
			strBuf.append(sortFieldList.get(i).getTableName());
			strBuf.append(".");
			strBuf.append(sortFieldList.get(i).getFieldName());
			if(sortFieldList.get(i).isAsc()){
				strBuf.append(" asc ");
			}else{
				strBuf.append(" desc ");
			}
		}
		
		return strBuf.toString();
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

	public boolean isAsc() {
		return isAsc;
	}

	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}

}
