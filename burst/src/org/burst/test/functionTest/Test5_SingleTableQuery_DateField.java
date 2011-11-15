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
import org.burst.tools.DateUtil;


//test SingleTableQuery
public class Test5_SingleTableQuery_DateField {
	
	private static final Logger logger = Logger.getLogger(Test5_SingleTableQuery_DateField.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DbBase db = null;
		try{
			//startup
			Startup.init();
			
			//new DbBase
			db = new DbBase();
			
		
			//please test by order
			//queryBetween(db);
			//queryBigThan(db);
			//queryLessThan(db);
			queryEqual(db);
			
			
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
	
	public static Date addDay(Date date1, int addDay){
		return new Date(date1.getTime() + addDay*3600*24*1000);
	}
	
	//query by cdate, matchType:between
	public static void queryBetween(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "cdate");
		queryField.setMatchType(QueryField.BETWEEN);
		
		Date now = new Date();
		queryField.setMin(now);
		queryField.setMax(addDay(now,3));
		
		//if mysql dateformat share be "%Y-%m-%d"
		//if sql server, dateformat, 101:mm/dd/yyyy ; 121:yyyy-mm-dd hh:mm:ss[.fff]
		//if oracle or db2 use "yyyy/mm/dd"
		queryField.setDateFormat("101");
		//queryField.setDateFormat("yyyy/mm/dd");
		//queryField.setDateFormat("%Y-%m-%d");
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " cdate=" + DateUtil.getDateString(tblStudent.getCdate(),"yyyy/MM/dd"));
		}
	}
	
	//query by cdate, matchType:QueryField.BIGTHAN
	public static void queryBigThan(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "cdate");
		queryField.setMatchType(QueryField.BIGTHAN);
		
		Date now = new Date();
		queryField.setMin(addDay(now,3));
		//if mysql dateformat share be "%Y-%m-%d"
		//if sql server, dateformat, 101:mm/dd/yyyy ; 121:yyyy-mm-dd hh:mm:ss[.fff]
		//if oracle or db2 use "yyyy/mm/dd"
		queryField.setDateFormat("101");
		//queryField.setDateFormat("yyyy/mm/dd");
		//queryField.setDateFormat("%Y-%m-%d");
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " cdate=" + DateUtil.getDateString(tblStudent.getCdate(),"yyyy/MM/dd"));
		}
	}
	
	//query by cdate, matchType:QueryField.LESSTHAN
	public static void queryLessThan(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "cdate");
		queryField.setMatchType(QueryField.LESSTHAN);
		
		Date now = new Date();
		queryField.setMax(addDay(now,2));
		//if mysql dateformat share be "%Y-%m-%d"
		//if sql server, dateformat, 101:mm/dd/yyyy ; 121:yyyy-mm-dd hh:mm:ss[.fff]
		//if oracle or db2 use "yyyy/mm/dd"
		queryField.setDateFormat("101");
		//queryField.setDateFormat("yyyy/mm/dd");
		//queryField.setDateFormat("%Y-%m-%d");
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " cdate=" + DateUtil.getDateString(tblStudent.getCdate(),"yyyy/MM/dd"));
		}
	}
	
	//query by cdate, matchType:QueryField.EQUAL
	public static void queryEqual(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "cdate");
		queryField.setMatchType(QueryField.EQUAL);
		
		Date now = new Date();
		queryField.setValue(addDay(now,2));
		//if mysql dateformat share be "%Y-%m-%d"
		//if sql server, dateformat, 101:mm/dd/yyyy ; 121:yyyy-mm-dd hh:mm:ss[.fff]
		//if oracle or db2 use "yyyy/mm/dd"
		queryField.setDateFormat("101");
		//queryField.setDateFormat("yyyy/mm/dd");
		//queryField.setDateFormat("%Y-%m-%d");
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		for(int i=0;i<tblList.size();i++){
			tblStudent = (TblStudent)tblList.get(i);
			logger.debug("username=" + tblStudent.getUsername() + " cdate=" + DateUtil.getDateString(tblStudent.getCdate(),"yyyy/MM/dd"));
		}
	}
}
