package org.burst.test.functionTest;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.burst.db.conn.DbBase;
import org.burst.db.init.Startup;
import org.burst.db.query.QueryField;
import org.burst.db.query.SingleTblQueryCondition;
import org.burst.db.tbl.TblStudent;
import org.burst.db.tbl.base.TblBase;
import org.burst.db.tbl.base.TblBaseService;


//test SingleTableQuery
public class Test3_SingleTableQuery {
	
	private static final Logger logger = Logger.getLogger(Test3_SingleTableQuery.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DbBase db = null;
		try{
			//startup
			Startup.init();
			
			//new DbBase
			db = new DbBase();
			
			//first createTestData, 
			//don't create same data, use different parameters
			//createTestData(db,"001", 100);
			
			//please test by order
			//queryBetween(db);
			//queryBigThan(db);
			//queryLessThan(db);
			//queryEqual(db);
			//queryEqual2(db);
			//queryEqual3(db);
			//queryLike(db);
			//queryIn(db);
			
			//sql server can not use query in (float, float)
			//queryIn2(db);
			//queryNotIn(db);
			//queryNotEqual(db);
			
			queryMultiFields(db);
			
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
	
	public static void createTestData(DbBase db, String key, int count) throws Exception{
		TblStudent tblStudent;
		Date now = new Date();
		for(int i=0;i<count;i++){
			tblStudent = new TblStudent();
			tblStudent.setUsername("test" + key + "_" + i);
			tblStudent.setFullname("fullname" + key + "_" + i);
			tblStudent.setScore(0.1 + i);
			
			tblStudent.setCdate(addDay(now,i%10));
			tblStudent.setUdate(addDay(now,i%20));
			tblStudent.setActive(i%2==0);
			
			TblBaseService.create(db, tblStudent);
		}
		
	}
	
	public static Date addDay(Date date1, int addDay){
		return new Date(date1.getTime() + addDay*3600*24*1000);
	}
	
	//query by score, matchType:between
	public static void queryBetween(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "score");
		queryField.setMatchType(QueryField.BETWEEN);
		queryField.setMin(20);
		queryField.setMax(30);
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
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
		
		//limit
		stqc.setLimit(10);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
		}
	}
	
}
