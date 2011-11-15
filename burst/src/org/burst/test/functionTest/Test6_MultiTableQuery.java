package org.burst.test.functionTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.burst.db.conn.DbBase;
import org.burst.db.init.Startup;
import org.burst.db.query.MultiTblQueryCondition;
import org.burst.db.query.QueryField;
import org.burst.db.query.TblConnectKeys;
import org.burst.db.tbl.TblLesson;
import org.burst.db.tbl.TblStudent;
import org.burst.db.tbl.TblStudentLessons;
import org.burst.db.tbl.base.TblBase;
import org.burst.db.tbl.base.TblBaseService;


//test SingleTableQuery
public class Test6_MultiTableQuery {
	
	private static final Logger logger = Logger.getLogger(Test6_MultiTableQuery.class);

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
			//createTestData(db, "002");
			//createTestData(db, "003");
			//createTestData(db, "004");
			
			//please test by order
			query1(db);

			
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
	
	public static void createTestData(DbBase db, String key) throws Exception{
		TblStudent tblStudent;
		Date now = new Date();
		tblStudent = new TblStudent();
		tblStudent.setUsername("test_" + key);
		tblStudent.setFullname("fullname_" + key);
		tblStudent.setScore(30.1);
		
		tblStudent.setCdate(now);
		tblStudent.setUdate(now);
		tblStudent.setActive(true);
		
		int studentId = TblBaseService.create(db, tblStudent);
		
		//lesson
		TblLesson tblLesson;
		tblLesson = new TblLesson();
		tblLesson.setName("lesson_" + key);
		tblLesson.setDescription("desc_" + key);

		int lessonId = TblBaseService.create(db, tblLesson);
		
		//studentLessons
		TblStudentLessons tblStudentLessons;
		tblStudentLessons = new TblStudentLessons();
		tblStudentLessons.setStudentId(studentId);
		tblStudentLessons.setLessonId(lessonId);
		tblStudentLessons.setScore(89.0);
		tblStudentLessons.setDescription("desc_" + key);

		TblBaseService.create(db, tblStudentLessons);

	}
	
	public static Date addDay(Date date1, int addDay){
		return new Date(date1.getTime() + addDay*3600*24*1000);
	}
	
	//query 3 tables
	public static void query1(DbBase db) throws Exception{
		
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
		queryField.setValue("test_003");
		
		mtqc.addQueryField(queryField);
		
		HashMap<String,ArrayList<TblBase>> tblListMap = TblBaseService.queryByCondition(db, mtqc);
		int size = TblBaseService.getMultiTblQuerySize(tblListMap);
		logger.debug("query finish");
		if(size == 0){
			logger.debug("find no record !");
			return;
		}
		
		ArrayList<TblBase> studentList = tblListMap.get(tblStudent.getTblName());
		ArrayList<TblBase> lessonList = tblListMap.get(tblLesson.getTblName());
		ArrayList<TblBase> studentLessonsList = tblListMap.get(tblStudentLessons.getTblName());
		
		int listSize = studentList.size();
		for(int i=0;i<listSize;i++){
			tblStudent = (TblStudent)studentList.get(i);
			tblLesson = (TblLesson)lessonList.get(i);
			tblStudentLessons = (TblStudentLessons)studentLessonsList.get(i);
			
			logger.debug("username=" + tblStudent.getUsername() + " score=" + tblStudent.getScore());
			logger.debug("lesson name=" + tblLesson.getName());
			logger.debug("score=" + tblStudentLessons.getScore());
		}
	}
	
}
