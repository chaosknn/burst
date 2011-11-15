package org.burst.test.functionTest;

import org.apache.log4j.Logger;
import org.burst.db.conn.DbBase;
import org.burst.db.def.TblDefService;
import org.burst.db.init.Startup;


public class Test1_TblCreateDrop {

	private static final Logger logger = Logger.getLogger(Test1_TblCreateDrop.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DbBase db = null;
		try{
			//startup
			Startup.init();
			
			//new DbBase
			db = new DbBase();
			
			//call, please test by order
			createAllTbl(db);
			//dropAllTbl(db);
			
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
	
	public static void createAllTbl(DbBase db) throws Exception{
		TblDefService.createAllTbl(db);
	}
	
	public static void dropAllTbl(DbBase db) throws Exception{
		TblDefService.dropAllTbl(db);
	}

}
