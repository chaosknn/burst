package org.burst.test.functionTest;

import java.util.Date;

import org.apache.log4j.Logger;
import org.burst.db.conn.DbBase;
import org.burst.db.init.Startup;
import org.burst.db.query.MultiTblQueryCondition;
import org.burst.db.query.QueryField;
import org.burst.db.query.TblConnectKeys;
import org.burst.db.tbl.TblLesson;
import org.burst.db.tbl.TblStudent;
import org.burst.db.tbl.TblStudentLessons;
import org.burst.db.tbl.base.TblBaseService;


//test SingleTableQuery
public class Test8_UpdateDeleteByMultiTable {
	
	private static final Logger logger = Logger.getLogger(Test8_UpdateDeleteByMultiTable.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DbBase db = null;
		try{
			//startup
			Startup.init();
			
			//new DbBase
			db = new DbBase();
			
			
			//please test by order
			//updateByMultiTable(db);
			removeByMultiTable(db);
			
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
	
	//updateByMultiTable
	public static void updateByMultiTable(DbBase db) throws Exception{
		//define update tbl
		TblStudent tblStudentUpdate = new TblStudent();
		tblStudentUpdate.setFullname("fullname001- updatedByMulti");
		tblStudentUpdate.setScore(88.88);
		tblStudentUpdate.setUdate(new Date());
		tblStudentUpdate.setUpdateflag("fullname", true);
		tblStudentUpdate.setUpdateflag("score", true);
		tblStudentUpdate.setUpdateflag("udate", true);
		
		//define query condition
		TblStudent tblStudent = new TblStudent();
		TblLesson tblLesson = new TblLesson();
		TblStudentLessons tblStudentLessons = new TblStudentLessons();
		
		MultiTblQueryCondition mtqc = new MultiTblQueryCondition();
		mtqc.addTbl(tblStudent);
		mtqc.addTbl(tblLesson);
		mtqc.addTbl(tblStudentLessons);
		
		//connectKeys
		TblConnectKeys connKeys1 = new TblConnectKeys(tblStudent.getTblName(),"id", tblStudentLessons.getTblName(), "studentId");
		TblConnectKeys connKeys2 = new TblConnectKeys(tblLesson.getTblName(),"id", tblStudentLessons.getTblName(), "lessonId");
		mtqc.addTblConnectKeys(connKeys1);
		mtqc.addTblConnectKeys(connKeys2);
		
		QueryField queryField = new QueryField(tblStudent, "username");
		queryField.setMatchType(QueryField.EQUAL);
		queryField.setValue("test_002");
		
		mtqc.addQueryField(queryField);
		
		int i = TblBaseService.updateByCondition(db, tblStudentUpdate, mtqc);
		logger.debug("update finish, " + i + " records updated");
		
	}
	
	//deleteByMultiTable
	public static void removeByMultiTable(DbBase db) throws Exception{
		
		//define query condition
		TblStudent tblStudent = new TblStudent();
		TblLesson tblLesson = new TblLesson();
		TblStudentLessons tblStudentLessons = new TblStudentLessons();
		
		MultiTblQueryCondition mtqc = new MultiTblQueryCondition();
		mtqc.addTbl(tblStudent);
		mtqc.addTbl(tblLesson);
		mtqc.addTbl(tblStudentLessons);
		
		//connectKeys
		TblConnectKeys connKeys1 = new TblConnectKeys(tblStudent.getTblName(),"id", tblStudentLessons.getTblName(), "studentId");
		TblConnectKeys connKeys2 = new TblConnectKeys(tblLesson.getTblName(),"id", tblStudentLessons.getTblName(), "lessonId");
		mtqc.addTblConnectKeys(connKeys1);
		mtqc.addTblConnectKeys(connKeys2);
		
		QueryField queryField = new QueryField(tblStudent, "username");
		queryField.setMatchType(QueryField.EQUAL);
		queryField.setValue("test_002");
		
		mtqc.addQueryField(queryField);
		
		int i = TblBaseService.removeByCondition(db, tblStudent, mtqc);
		logger.debug("remove finish, " + i + " records removed");
		
	}
	
}
