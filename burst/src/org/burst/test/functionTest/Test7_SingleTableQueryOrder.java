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
import org.burst.tools.DateUtil;


//test SingleTableQuery
public class Test7_SingleTableQueryOrder {
	
	private static final Logger logger = Logger.getLogger(Test7_SingleTableQueryOrder.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DbBase db = null;
		try{
			//startup
			Startup.init();
			
			//new DbBase
			db = new DbBase();
			
			
			//please test by order
			queryOrder1(db);
			
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
	               
	//query order1
	public static void queryOrder1(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "score");
		queryField.setMatchType(QueryField.BETWEEN);
		queryField.setMin(10);
		queryField.setMax(100);
		
		stqc.addQueryField(queryField);
		
		SortField sortField = new SortField(tblStudent.getTblName(), "cdate", false);
		stqc.addSortField(sortField);
		sortField = new SortField(tblStudent.getTblName(), "score", false);
		stqc.addSortField(sortField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore() + " cdate=" + DateUtil.getDateString(tblStudent.getCdate(),"yyyy/MM/dd"));
		}
	}
	
	//query by score, matchType:QueryField.BIGTHAN
	public static void queryBigThan(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "score");
		queryField.setMatchType(QueryField.BIGTHAN);
		queryField.setMin(80);
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
	
	//query by score, matchType:QueryField.LESSTHAN
	public static void queryLessThan(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "score");
		queryField.setMatchType(QueryField.LESSTHAN);
		queryField.setMax(20);
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
	
	//query by score, matchType:QueryField.EQUAL
	public static void queryEqual(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "score");
		queryField.setMatchType(QueryField.EQUAL);
		queryField.setValue(20.1);
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
	
	//query by active, matchType:QueryField.EQUAL
	public static void queryEqual2(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "active");
		queryField.setMatchType(QueryField.EQUAL);
		queryField.setValue(true);
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
	
	//query by username, matchType:QueryField.EQUAL, CaseSensitive=false
	public static void queryEqual3(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "username");
		queryField.setMatchType(QueryField.EQUAL);
		queryField.setValue("TEst001_1");
		queryField.setCaseSensitive(false);
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
	
	//query by username, matchType:QueryField.LIKE
	public static void queryLike(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "username");
		queryField.setMatchType(QueryField.LIKE);
		queryField.setValue("%1");
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
	
	//query by username, matchType:QueryField.IN
	public static void queryIn(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "username");
		queryField.setMatchType(QueryField.IN);
		queryField.appendInValue("test001_1");
		queryField.appendInValue("test001_2");
		queryField.appendInValue("test001_3");
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
	
	//query by score, matchType:QueryField.IN
	public static void queryIn2(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "score");
		queryField.setMatchType(QueryField.IN);
		queryField.appendInValue(10.1);
		queryField.appendInValue(12.1);
		queryField.appendInValue(20.1);
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
	
	//query by score, matchType:QueryField.NOTIN
	public static void queryNotIn(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "score");
		queryField.setMatchType(QueryField.NOTIN);
		queryField.appendInValue(10.1);
		queryField.appendInValue(12.1);
		queryField.appendInValue(20.1);
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
	
	//query by score, matchType:QueryField.NOTEQUAL
	public static void queryNotEqual(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "score");
		queryField.setMatchType(QueryField.NOTEQUAL);
		queryField.setValue(10.1);
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
	
	//query by multi query fields
	public static void queryMultiFields(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "username");
		queryField.setMatchType(QueryField.LIKE);
		queryField.setValue("%1");
		
		stqc.addQueryField(queryField);
		
		//another queryField
		queryField = new QueryField(tblStudent, "score");
		queryField.setMatchType(QueryField.LESSTHAN);
		queryField.setMax(40);
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
	
}
