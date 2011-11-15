package org.burst.db.query;

import java.util.ArrayList;

import org.burst.db.tbl.base.TblBase;


public class SingleTblQueryCondition {

	private TblBase tbl;
	
	private ArrayList<QueryField> queryFieldList = new ArrayList<QueryField>();
	private ArrayList<SortField> sortFieldList = new ArrayList<SortField>();
	
	private int limit = 0;
	private boolean limitReached = false;
	
	PageController pc = new PageController();

	public void addQueryField(QueryField queryField){
		queryFieldList.add(queryField);
	}
	
	public void addSortField(SortField sortField){
		sortFieldList.add(sortField);
	}
	
	public static SingleTblQueryCondition getSTQCById(TblBase tbl) throws Exception{
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tbl);
		
		QueryField queryField = new QueryField(tbl, "id");
		queryField.setMatchType(QueryField.EQUAL);
		queryField.setValue(tbl.getInt("id"));
		
		stqc.addQueryField(queryField);
		
		return stqc;
	}
	
	public static SingleTblQueryCondition getSTQCByIdList(TblBase tbl, ArrayList<Integer> idList) throws Exception{
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tbl);
		
		QueryField queryField = new QueryField(tbl, "id");
		queryField.setMatchType(QueryField.IN);
		queryField.setInList(idList);
		
		stqc.addQueryField(queryField);
		
		return stqc;
	}
	
	public TblBase getTbl() {
		return tbl;
	}

	public void setTbl(TblBase tbl) {
		this.tbl = tbl;
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
	
	public PageController getPc() {
		return pc;
	}

	public void setPc(PageController pc) {
		this.pc = pc;
	}

}
