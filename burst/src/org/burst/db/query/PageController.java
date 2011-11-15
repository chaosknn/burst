package org.burst.db.query;

public class PageController {

	int pageSize = 20;
	
	boolean showAll = false;
	
	int rowCount = 0;
	int nowPage = 1;
	int totalPage = 1;
	
	public int getStartRow(){
		if(showAll){
			return 1;
		}
		
		return (nowPage-1) * pageSize + 1;
	}
	
	public int getEndRow(){
		if(showAll){
			return rowCount;
		}
		
		int endRow = nowPage * pageSize;
		if(endRow > rowCount){
			endRow = rowCount;
		}
		return endRow;
	}
	
	public void setRowCount(int rowCount){
		
		this.rowCount = rowCount;
		
		totalPage = new Double(rowCount/pageSize).intValue();
		if(rowCount%pageSize > 0){
			totalPage = totalPage + 1;
		}
		
		//if nowPage invalid
		if(nowPage > totalPage){
			nowPage = totalPage;
		}
	}
	
	//forbidden invalid page
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;

		if(this.nowPage < 1){
			this.nowPage = 1;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		if(pageSize < 1){
			this.pageSize = 20;
		}
	}

	public boolean isShowAll() {
		return showAll;
	}

	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	public int getNowPage() {
		return nowPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getRowCount() {
		return rowCount;
	}

	public static void main(String[] args){
		PageController pc = new PageController();
		pc.setRowCount(39);
		
		System.out.println("totalPage=" + pc.getTotalPage());
	}
}
