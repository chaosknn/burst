package org.burst.db.tbl.base;

import java.util.ArrayList;
import java.util.Date;

public abstract class TblBase {
	private String tblNameDef = "";
	private String tblFieldDef = "";
	private String tblIdxDef = "";
	private String tblPartitionDef = "";
	
	private String tblName = "";
	private String sequenceName = "";
	private String idxName = "";
	private ArrayList<String> idxFieldList = null;
	
	//new
	private String partitionField = "";
	private boolean isPartition = false;
	
	//the first field must be "id"
	private ArrayList<String> fieldList = new ArrayList<String>();
	private ArrayList<Integer> fieldTypeList = new ArrayList<Integer>();
	private ArrayList<Integer> fieldSizeList = new ArrayList<Integer>();
	private ArrayList<Object> valueList = new ArrayList<Object>();
	private ArrayList<Boolean> updateFlagList = new ArrayList<Boolean>();
	
	public TblBase(String tblNameDef, String tblFieldDef, String tblIdxDef, String tblPartitionDef){
		this.tblNameDef = tblNameDef;
		this.tblFieldDef = tblFieldDef;
		this.tblIdxDef = tblIdxDef;
		this.tblPartitionDef = tblPartitionDef;
		
		init();
	}
	
	public abstract TblBase newTbl();
	
	public void init(){
		tblName = tblNameDef;
		idxFieldList = TblBaseUtil.strToList(tblIdxDef, ";");
		
		sequenceName = TblBaseUtil.SEQUENCE_MARK + tblName;
		idxName = TblBaseUtil.INDEX_MARK + tblName;
		
		//fields
		ArrayList<String> tmpFieldList = TblBaseUtil.strToList(tblFieldDef, TblBaseUtil.SEP);
		String strField;
		String fieldName;
		String fieldTypeAndSize;
		int fieldType;
		String fieldSize;
		int idx;

		for(int i=0;i<tmpFieldList.size();i++){
			strField = (String)tmpFieldList.get(i);
			idx = strField.indexOf(TblBaseUtil.LPT_MARK);
			
			fieldName = strField.substring(0, idx);
			fieldList.add(fieldName);
			
			fieldTypeAndSize = strField.substring(idx);
			
			//get field type,position is fixed
			fieldType = Integer.parseInt(fieldTypeAndSize.substring(TblBaseUtil.FIELD_TYPE_IDX,TblBaseUtil.FIELD_TYPE_IDX+1));
			fieldTypeList.add(fieldType);
			if(fieldType == TblBaseUtil.FIELD_STRING){
				fieldSize = fieldTypeAndSize.substring(TblBaseUtil.FIELD_TYPE_IDX+2,fieldTypeAndSize.length()-1);
				fieldSizeList.add(Integer.parseInt(fieldSize));
			}else{
				//int,double,date,bool will not set length
				fieldSizeList.add(0);
			}
			
			updateFlagList.add(false);
			valueList.add(null);
			
		}
		
		//partitions
		if(tblPartitionDef == null || tblPartitionDef.trim().length()==0){
			isPartition = false;
			return;
		}
		partitionField = tblPartitionDef;
		isPartition = true;
	}
	
	public boolean hasField(String fieldName) throws Exception{
		return getFieldIdx(fieldName) >= 0;
	}
	
	public int getFieldIdx(String fieldName) throws Exception{
		return fieldList.indexOf(fieldName);
	}
	
	public String getFieldName(int idx) throws Exception{
		return (String)fieldList.get(idx);
	}
	
	public int getFieldType(int idx) throws Exception{
		return (Integer)fieldTypeList.get(idx);
	}
	
	public int getFieldType(String fieldName) throws Exception{
		return getFieldType(getFieldIdx(fieldName));
	}
	
	public int getFieldSize(int idx) throws Exception{
		return (Integer)fieldSizeList.get(idx);
	}
	
	public int getFieldSize(String fieldName) throws Exception{
		return getFieldSize(getFieldIdx(fieldName));
	}
	
	public void setBool(String fieldName, Boolean value) throws Exception{
		setBool(fieldName, value, false, false);
	}
	
	public void setInt(String fieldName, Integer value) throws Exception{
		setInt(fieldName, value, false, false);
	}
	
	public void setDouble(String fieldName, Double value) throws Exception{
		setDouble(fieldName, value, false, false);
	}
	
	public void setDate(String fieldName, Date value) throws Exception{
		setDate(fieldName, value, false, false);
	}
	
	public void setString(String fieldName, String value) throws Exception{
		setString(fieldName, value, false, false);
	}
	
	public void setBlob(String fieldName, MyBlob value) throws Exception{
		setBlob(fieldName, value, false, false);
	}
	
	public void setBool(String fieldName, Boolean value, boolean setUpdFlag, boolean updFlag) throws Exception{
		int idx = getFieldIdx(fieldName);
		if(idx<0){
			throw new Exception("no such field:" + fieldName);
		}
		
		if(TblBaseUtil.FIELD_BOOL != getFieldType(idx)){
			throw new Exception("field is not bool:" + fieldName);
		}
		
		valueList.set(idx, value);
		if(setUpdFlag){
			updateFlagList.set(idx, updFlag);
		}
	}
	
	public void setInt(String fieldName, Integer value, boolean setUpdFlag, boolean updFlag) throws Exception{
		int idx = getFieldIdx(fieldName);
		if(idx<0){
			throw new Exception("no such field:" + fieldName);
		}
		
		if(TblBaseUtil.FIELD_INT != getFieldType(idx)){
			throw new Exception("field is not int:" + fieldName);
		}
		
		valueList.set(idx, value);
		if(setUpdFlag){
			updateFlagList.set(idx, updFlag);
		}
	}
	
	public void setDouble(String fieldName, Double value, boolean setUpdFlag, boolean updFlag) throws Exception{
		int idx = getFieldIdx(fieldName);
		if(idx<0){
			throw new Exception("no such field:" + fieldName);
		}
		
		if(TblBaseUtil.FIELD_DOUBLE != getFieldType(idx)){
			throw new Exception("field is not double:" + fieldName);
		}
		
		valueList.set(idx, value);
		if(setUpdFlag){
			updateFlagList.set(idx, updFlag);
		}
	}
	
	public void setDate(String fieldName, Date value, boolean setUpdFlag, boolean updFlag) throws Exception{
		int idx = getFieldIdx(fieldName);
		if(idx<0){
			throw new Exception("no such field:" + fieldName);
		}
		
		if(TblBaseUtil.FIELD_DATE != getFieldType(idx)){
			throw new Exception("field is not date:" + fieldName);
		}
		
		valueList.set(idx, value);
		if(setUpdFlag){
			updateFlagList.set(idx, updFlag);
		}
	}
	
	public void setString(String fieldName, String value, boolean setUpdFlag, boolean updFlag) throws Exception{
		int idx = getFieldIdx(fieldName);
		if(idx<0){
			throw new Exception("no such field:" + fieldName);
		}
		
		if(TblBaseUtil.FIELD_STRING != getFieldType(idx)){
			throw new Exception("field is not string:" + fieldName);
		}
		
		valueList.set(idx, value);
		if(setUpdFlag){
			updateFlagList.set(idx, updFlag);
		}
	}
	

	public void setBlob(String fieldName, MyBlob value, boolean setUpdFlag, boolean updFlag) throws Exception{
		int idx = getFieldIdx(fieldName);
		if(idx<0){
			throw new Exception("no such field:" + fieldName);
		}
		
		if(TblBaseUtil.FIELD_BLOB != getFieldType(idx)){
			throw new Exception("field is not blob:" + fieldName);
		}
		
		valueList.set(idx, value);
		if(setUpdFlag){
			updateFlagList.set(idx, updFlag);
		}
	}
	
	//set all flag, except id
	public void setUpdateflagAll(boolean flag) throws Exception{
		for(int idx=1; idx < fieldList.size(); idx++){
			updateFlagList.set(idx, flag);
		}
	}
	
	public void setUpdateflag(String fieldName, boolean flag) throws Exception{
		int idx = getFieldIdx(fieldName);
		if(idx<0){
			throw new Exception("no such field:" + fieldName);
		}
		updateFlagList.set(idx, flag);
	}
	
	//maybe null
	public Boolean getBool(String fieldName) throws Exception{
		
		int idx = getFieldIdx(fieldName);
		if(TblBaseUtil.FIELD_BOOL != getFieldType(idx)){
			throw new Exception("field is not bool:" + fieldName);
		}

		if(valueList.get(idx) == null){
			return null;
		}
		
		return (Boolean)valueList.get(idx);
	}
	
	public MyBlob getBlob(String fieldName) throws Exception{
		
		int idx = getFieldIdx(fieldName);
		if(TblBaseUtil.FIELD_BLOB != getFieldType(idx)){
			throw new Exception("field is not blob:" + fieldName);
		}

		if(valueList.get(idx) == null){
			return null;
		}
		
		return (MyBlob)valueList.get(idx);
	}
	
	//maybe null
	public Integer getInt(String fieldName) throws Exception{
		
		int idx = getFieldIdx(fieldName);
		if(TblBaseUtil.FIELD_INT != getFieldType(idx)){
			throw new Exception("field is not int:" + fieldName);
		}

		if(valueList.get(idx) == null){
			return null;
		}
		
		return (Integer)valueList.get(idx);
	}
	
	//maybe null
	public Double getDouble(String fieldName) throws Exception{
		
		int idx = getFieldIdx(fieldName);
		if(TblBaseUtil.FIELD_DOUBLE != getFieldType(idx)){
			throw new Exception("field is not double:" + fieldName);
		}

		if(valueList.get(idx) == null){
			return null;
		}
		
		return (Double)valueList.get(idx);
	}
	
	public Date getDate(String fieldName) throws Exception{
		
		int idx = getFieldIdx(fieldName);
		if(TblBaseUtil.FIELD_DATE != getFieldType(idx)){
			throw new Exception("field is not date:" + fieldName);
		}
		
		if(valueList.get(idx) == null){
			return null;
		}

		return (Date)valueList.get(idx);
	}
	
	public String getString(String fieldName) throws Exception{
		int idx = getFieldIdx(fieldName);
		if(TblBaseUtil.FIELD_STRING != getFieldType(idx)){
			throw new Exception("field is not string:" + fieldName);
		}
		
		if(valueList.get(idx) == null){
			return null;
		}

		return (String)valueList.get(idx);
	}

	
	
	public String getTblName() {
		return tblName;
	}
	public void setTblName(String tblName) {
		this.tblName = tblName;
	}
	public String getSequenceName() {
		return sequenceName;
	}
	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}
	public ArrayList<String> getFieldList() {
		return fieldList;
	}
	public void setFieldList(ArrayList<String> fieldList) {
		this.fieldList = fieldList;
	}

	public ArrayList<Object> getValueList() {
		return valueList;
	}

	public void setValueList(ArrayList<Object> valueList) {
		this.valueList = valueList;
	}

	public ArrayList<Boolean> getUpdateFlagList() {
		return updateFlagList;
	}

	public void setUpdateFlagList(ArrayList<Boolean> updateFlagList) {
		this.updateFlagList = updateFlagList;
	}

	public ArrayList<Integer> getFieldTypeList() {
		return fieldTypeList;
	}

	public void setFieldTypeList(ArrayList<Integer> fieldTypeList) {
		this.fieldTypeList = fieldTypeList;
	}

	public String getIdxName() {
		return idxName;
	}

	public void setIdxName(String idxName) {
		this.idxName = idxName;
	}

	public ArrayList<Integer> getFieldSizeList() {
		return fieldSizeList;
	}

	public void setFieldSizeList(ArrayList<Integer> fieldSizeList) {
		this.fieldSizeList = fieldSizeList;
	}

	public String getPartitionField() {
		return partitionField;
	}

	public void setPartitionField(String partitionField) {
		this.partitionField = partitionField;
	}

	public String getTblNameDef() {
		return tblNameDef;
	}

	public void setTblNameDef(String tblNameDef) {
		this.tblNameDef = tblNameDef;
	}

	public String getTblFieldDef() {
		return tblFieldDef;
	}

	public void setTblFieldDef(String tblFieldDef) {
		this.tblFieldDef = tblFieldDef;
	}

	public String getTblIdxDef() {
		return tblIdxDef;
	}

	public void setTblIdxDef(String tblIdxDef) {
		this.tblIdxDef = tblIdxDef;
	}
	
	public ArrayList<String> getIdxFieldList() {
		return idxFieldList;
	}

	public void setIdxFieldList(ArrayList<String> idxFieldList) {
		this.idxFieldList = idxFieldList;
	}

	public String getTblPartitionDef() {
		return tblPartitionDef;
	}

	public void setTblPartitionDef(String tblPartitionDef) {
		this.tblPartitionDef = tblPartitionDef;
	}

	public boolean isPartition() {
		return isPartition;
	}

	public void setPartition(boolean isPartition) {
		this.isPartition = isPartition;
	}

}
