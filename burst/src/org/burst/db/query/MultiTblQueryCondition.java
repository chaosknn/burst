package org.burst.db.query;

import java.util.ArrayList;
import java.util.HashMap;

import org.burst.db.tbl.base.TblBase;


public class MultiTblQueryCondition {

	private ArrayList<TblBase> tblList = new ArrayList<TblBase>();
	private ArrayList<String> unSelectTblList = new ArrayList<String>();
	
	private ArrayList<QueryField> queryFieldList = new ArrayList<QueryField>();
	private ArrayList<SortField> sortFieldList = new ArrayList<SortField>();
	private ArrayList<TblConnectKeys> connectKeysList = new ArrayList<TblConnectKeys>();
	
	private int limit = 0;
	private boolean limitReached = false;
	
	PageController pc = new PageController();
	
	private HashMap<String,ArrayList<TblBase>> queryResult = new HashMap<String,ArrayList<TblBase>>();
	
	public void addTbl(TblBase tbl){
		tblList.add(tbl);
	}
	
	public void addUnSelectTbl(String tblName){
		unSelectTblList.add(tblName);
	}
	
	public void addQueryField(QueryField queryField){
		queryFieldList.add(queryField);
	}
	
	public void addSortField(String tblName, SortField sortField){
		sortFieldList.add(sortField);
	}
	
	public void addTblConnectKeys(TblConnectKeys tblConnectKeys){
		connectKeysList.add(tblConnectKeys);
	}
	
	public ArrayList<TblBase> getTblList() {
		return tblList;
	}

	public void setTblList(ArrayList<TblBase> tblList) {
		this.tblList = tblList;
	}

	public ArrayList<String> getUnSelectTblList() {
		return unSelectTblList;
	}

	public void setUnSelectTblList(ArrayList<String> unSelectTblList) {
		this.unSelectTblList = unSelectTblList;
	}

	public ArrayList<QueryField> getQueryFieldList() {
		return queryFieldList;
	}

	public void setQueryFieldList(ArrayList<QueryField> queryFieldList) {
		this.queryFieldList = queryFieldList;
	}

	public ArrayList<SortField> getSortFieldList() {
		return sortFieldList;
	}

	public void setSortFieldList(ArrayList<SortField> sortFieldList) {
		this.sortFieldList = sortFieldList;
	}


	public ArrayList<TblConnectKeys> getConnectKeysList() {
		return connectKeysList;
	}

	public void setConnectKeysList(ArrayList<TblConnectKeys> connectKeysList) {
		this.connectKeysList = connectKeysList;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isLimitReached() {
		return limitReached;
	}

	public void setLimitReached(boolean limitReached) {
		this.limitReached = limitReached;
	}

	public HashMap<String, ArrayList<TblBase>> getQueryResult() {
		return queryResult;
	}

	public void setQueryResult(HashMap<String, ArrayList<TblBase>> queryResult) {
		this.queryResult = queryResult;
	}

	public PageController getPc() {
		return pc;
	}

	public void setPc(PageController pc) {
		this.pc = pc;
	}

}
