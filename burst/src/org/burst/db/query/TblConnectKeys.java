package org.burst.db.query;

public class TblConnectKeys {
	
	private String tbl1 = "";
	private String fieldName1 = "";
	private String tbl2 = "";
	private String fieldName2 = "";
	
	public TblConnectKeys(String tbl1, String fieldName1, String tbl2, String fieldName2){
		this.tbl1 = tbl1;
		this.fieldName1 = fieldName1;
		this.tbl2 = tbl2;
		this.fieldName2 = fieldName2;
	}
	
	public String getTbl1() {
		return tbl1;
	}
	public void setTbl1(String tbl1) {
		this.tbl1 = tbl1;
	}
	public String getFieldName1() {
		return fieldName1;
	}
	public void setFieldName1(String fieldName1) {
		this.fieldName1 = fieldName1;
	}
	public String getTbl2() {
		return tbl2;
	}
	public void setTbl2(String tbl2) {
		this.tbl2 = tbl2;
	}
	public String getFieldName2() {
		return fieldName2;
	}
	public void setFieldName2(String fieldName2) {
		this.fieldName2 = fieldName2;
	}
}
