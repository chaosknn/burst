package org.burst.test.functionTest;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.burst.db.conn.DbBase;
import org.burst.db.init.Startup;
import org.burst.db.query.QueryField;
import org.burst.db.query.SingleTblQueryCondition;
import org.burst.db.tbl.TblStudent;
import org.burst.db.tbl.base.TblBase;
import org.burst.db.tbl.base.TblBaseService;


//test SingleTableQuery
public class Test4_SingleTableQuery_Null {
	
	private static final Logger logger = Logger.getLogger(Test4_SingleTableQuery_Null.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DbBase db = null;
		try{
			//startup
			Startup.init();
			
			//new DbBase
			db = new DbBase();
			
			//first createTestData, update some fields to null
			//updateFieldToNull(db);
			
			//please test by order
			//queryIsNull(db);
			//queryIsNull2(db);
			queryNotNull(db);
			
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
	
	//query by username "test001_1", then update
	public static void updateFieldToNull(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "username");
		queryField.setMatchType(QueryField.EQUAL);
		queryField.setValue("test001_1");
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		if(tblList.size() > 0){
			tblStudent = (TblStudent)tblList.get(0);
			
			tblStudent.setFullname(null);
			tblStudent.setScore(null);
			tblStudent.setUdate(null);
			tblStudent.setActive(null);
			
			tblStudent.setUpdateflag("fullname", true);
			tblStudent.setUpdateflag("score", true);
			tblStudent.setUpdateflag("udate", true);
			tblStudent.setUpdateflag("active", true);
			
			TblBaseService.updateById(db, tblStudent);
			
			//read again
			logger.debug("..........update ok...........");
		}
	}
	
	
	//query by score, matchType:ISNULL
	public static void queryIsNull(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "fullname");
		queryField.setMatchType(QueryField.ISNULL);
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore() + " fullname=" + tblStudent.getFullname());
		}
	}
	
	//query by udate, matchType:ISNULL
	public static void queryIsNull2(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "udate");
		queryField.setMatchType(QueryField.ISNULL);
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore() + " fullname=" + tblStudent.getFullname());
		}
	}
	
	//query by udate, matchType:NOTNULL
	public static void queryNotNull(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "udate");
		queryField.setMatchType(QueryField.NOTNULL);
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore() + " fullname=" + tblStudent.getFullname());
		}
	}
	
}
