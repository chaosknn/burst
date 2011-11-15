package org.burst.test.functionTest;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.burst.db.conn.DbBase;
import org.burst.db.init.Startup;
import org.burst.db.query.QueryField;
import org.burst.db.query.SingleTblQueryCondition;
import org.burst.db.query.SortField;
import org.burst.db.tbl.TblStudent;
import org.burst.db.tbl.base.TblBase;
import org.burst.db.tbl.base.TblBaseService;


//test Test9_PageController
public class Test9_PageController {
	
	private static final Logger logger = Logger.getLogger(Test9_PageController.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DbBase db = null;
		try{
			//startup
			Startup.init();
			
			//new DbBase
			db = new DbBase();
			

			//query1(db);
			//query2(db);
			query3(db);
			
			db.commit();
			
			logger.debug("test ok");
		}catch(Exception e){
			if(db != null){
				db.rollback();
			}
			logger.error(e.toString());
			e.printStackTrace();
		}
	}
	
	//query1
	public static void query1(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "score");
		queryField.setMatchType(QueryField.BETWEEN);
		queryField.setMin(20);
		queryField.setMax(80);
		
		stqc.addQueryField(queryField);
		
		//order
		SortField sortField = new SortField(tblStudent.getTblName(), "score", true);
		stqc.addSortField(sortField);
		
		//page controller
		stqc.getPc().setPageSize(10);
		stqc.getPc().setNowPage(3);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		logger.debug("row count = " + stqc.getPc().getRowCount());
		logger.debug("total page = " + stqc.getPc().getTotalPage());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
	
	//query1
	public static void query2(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "score");
		queryField.setMatchType(QueryField.BETWEEN);
		queryField.setMin(20);
		queryField.setMax(80);
		
		stqc.addQueryField(queryField);
		
		//order
		SortField sortField = new SortField(tblStudent.getTblName(), "score", true);
		stqc.addSortField(sortField);
		
		//page controller
		stqc.getPc().setPageSize(10);
		stqc.getPc().setNowPage(7);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		logger.debug("row count = " + stqc.getPc().getRowCount());
		logger.debug("total page = " + stqc.getPc().getTotalPage());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
	
	//query3
	public static void query3(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "score");
		queryField.setMatchType(QueryField.BETWEEN);
		queryField.setMin(20);
		queryField.setMax(80);
		
		stqc.addQueryField(queryField);
		
		//order
		SortField sortField = new SortField(tblStudent.getTblName(), "score", true);
		stqc.addSortField(sortField);
		
		//page controller
		stqc.getPc().setPageSize(10);
		stqc.getPc().setShowAll(true);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		logger.debug("row count = " + stqc.getPc().getRowCount());
		logger.debug("total page = " + stqc.getPc().getTotalPage());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
}
