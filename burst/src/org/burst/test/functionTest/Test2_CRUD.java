package org.burst.test.functionTest;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.burst.db.conn.DbBase;
import org.burst.db.init.Startup;
import org.burst.db.query.QueryField;
import org.burst.db.query.SingleTblQueryCondition;
import org.burst.db.tbl.TblStudent;
import org.burst.db.tbl.base.MyBlob;
import org.burst.db.tbl.base.TblBase;
import org.burst.db.tbl.base.TblBaseService;
import org.burst.tools.DateUtil;


//test create,read,update,delete
public class Test2_CRUD {
	
	private static final Logger logger = Logger.getLogger(Test2_CRUD.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DbBase db = null;
		try{
			//startup
			Startup.init();
			
			//new DbBase
			db = new DbBase();
			
			//please test by c,r,u,d order
			//createTest(db);
			//readTest(db);
			//updateTest(db);
			deleteTest(db);
			
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
	
	public static void createTest(DbBase db) throws Exception{
		TblStudent tblStudent = new TblStudent();
		tblStudent.setUsername("test001");
		tblStudent.setFullname("fullname001");
		tblStudent.setScore(98.6);
		tblStudent.setCdate(new Date());
		tblStudent.setUdate(new Date());
		tblStudent.setActive(true);
		
		MyBlob myBlob = new MyBlob();
		myBlob.setValue("just for test");
		tblStudent.setImage(myBlob);
		
		TblBaseService.create(db, tblStudent);
	}
	
	//query by username "test001"
	public static void readTest(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "username");
		queryField.setMatchType(QueryField.EQUAL);
		queryField.setValue("test001");
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		if(tblList.size() > 0){
			tblStudent = (TblStudent)tblList.get(0);
			logger.debug("username=" + tblStudent.getUsername());
			logger.debug("fullname=" + tblStudent.getFullname());
			logger.debug("score=" + tblStudent.getScore());
			logger.debug("cate=" + DateUtil.getDateString(tblStudent.getCdate(), "yyyy/MM/dd"));
			logger.debug("udate=" + DateUtil.getDateString(tblStudent.getUdate(), "yyyy/MM/dd"));
			logger.debug("active=" + tblStudent.isActive());
			logger.debug("image=" + tblStudent.getImage().readString());
		}
	}
	
	//query by username "test001", then update
	public static void updateTest(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "username");
		queryField.setMatchType(QueryField.EQUAL);
		queryField.setValue("test001");
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		if(tblList.size() > 0){
			tblStudent = (TblStudent)tblList.get(0);
			logger.debug("..........before update...........");
			logger.debug("username=" + tblStudent.getUsername());
			logger.debug("fullname=" + tblStudent.getFullname());
			logger.debug("score=" + tblStudent.getScore());
			logger.debug("cate=" + DateUtil.getDateString(tblStudent.getCdate(), "yyyy/MM/dd"));
			logger.debug("udate=" + DateUtil.getDateString(tblStudent.getUdate(), "yyyy/MM/dd"));
			logger.debug("active=" + tblStudent.isActive());
			logger.debug("image=" + tblStudent.getImage().readString());
			
			tblStudent.setUsername("test001");
			tblStudent.setFullname("fullname001- updated");
			tblStudent.setScore(78.6);
			tblStudent.setCdate(new Date());
			tblStudent.setUdate(new Date());
			tblStudent.setActive(false);
			
			MyBlob myBlob = new MyBlob();
			myBlob.setValue("just for test- updated");
			tblStudent.setImage(myBlob);
			
			tblStudent.setUpdateflagAll(true);
			
			TblBaseService.updateById(db, tblStudent);
			
			//read again
			logger.debug("..........after update...........");
			readTest(db);
		}
	}
	
	//query by username "test001", then delete
	public static void deleteTest(DbBase db) throws Exception{
		
		TblStudent tblStudent = new TblStudent();
		
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tblStudent);
		
		QueryField queryField = new QueryField(tblStudent, "username");
		queryField.setMatchType(QueryField.EQUAL);
		queryField.setValue("test001");
		
		stqc.addQueryField(queryField);
		
		ArrayList<TblBase> tblList = TblBaseService.queryByCondition(db, stqc);
		logger.debug("tblList size = " + tblList.size());
		
		if(tblList.size() > 0){
			tblStudent = (TblStudent)tblList.get(0);
			
			TblBaseService.removeById(db, tblStudent);
			
			//read again
			logger.debug("..........after delete...........");
			readTest(db);
		}
	}
	
}
