package org.burst.db.tbl.base;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.burst.db.config.DbConfig;
import org.burst.db.conn.DbBase;
import org.burst.db.query.MultiTblQueryCondition;
import org.burst.db.query.QueryField;
import org.burst.db.query.SingleTblQueryCondition;
import org.burst.db.query.SortField;
import org.burst.db.query.TblConnectKeys;



public class TblBaseServiceMysql {
	private static final Logger logger = Logger.getLogger(TblBaseServiceMysql.class);
	
	public static int create(DbBase db, TblBase tbl) throws Exception {
		
		//logger.debug("----- create IN -----");
		StringBuffer strBuf = new StringBuffer();
		try{
			strBuf.append("insert into ");
			strBuf.append(tbl.getTblName());
			strBuf.append("(\n");
			ArrayList<String> fieldList = tbl.getFieldList();
			ArrayList<Object> valueList = tbl.getValueList();
			for(int i=0; i< fieldList.size();i++){
				//null field
				if(valueList.get(i) == null){
					continue;
				}
				
				strBuf.append(fieldList.get(i));
				strBuf.append(",\n");
			}
			//delete last ","
			strBuf.deleteCharAt(strBuf.length()-2);
			strBuf.append(") values (");
			for(int i=0; i< fieldList.size();i++){
				//null field
				if(valueList.get(i) == null){
					continue;
				}
				strBuf.append("?,");
			}
			strBuf.deleteCharAt(strBuf.length()-1);
			strBuf.append(")");
			
			db.initPS(strBuf.toString());
			
			int j=1;
			for(int i=0; i< fieldList.size();i++){
				//null field
				if(valueList.get(i) == null){
					continue;
				}
				TblBaseUtil.setPSValue(db, j, tbl.getFieldType(i), valueList.get(i));
				j=j+1;
			}
	
			db.executeUpdate();
			
			int id = getLastInsertId(db,tbl);
			tbl.setInt("id", id);
			
			return id;
		}catch(Exception e){
			logger.trace(e);
			throw e;
		}finally{
			strBuf = null;
			//logger.debug("----- create OUT -----");
		}
	}
	
	public static int updateByCondition(DbBase db, TblBase tbl, SingleTblQueryCondition stqc) throws Exception {
		
		//logger.debug("----- updateByCondition IN -----");
		StringBuffer strBuf = new StringBuffer();
		try{
			
			strBuf.append("update ");
			strBuf.append(tbl.getTblName());
			strBuf.append(" set\n");
			ArrayList<String> fieldList = tbl.getFieldList();
			ArrayList<Object> valueList = tbl.getValueList();
			
			ArrayList<Boolean> updateFlagList = tbl.getUpdateFlagList();
	
			for(int i=0; i<fieldList.size();i++){
				
				if(!(Boolean)updateFlagList.get(i)){
					continue;
				}
				
				strBuf.append(fieldList.get(i));
				strBuf.append("=?,\n");
			}
			strBuf.deleteCharAt(strBuf.length()-2);
			
			//queryCondition
			ArrayList<QueryField> queryFieldList = stqc.getQueryFieldList();
			if(queryFieldList != null && queryFieldList.size() > 0){
				strBuf.append(" where \n");
				
				boolean isFirst = true;
				for(int i=0;i<queryFieldList.size();i++){
					
					if(isFirst){
						isFirst = false;
					}else{
						strBuf.append("\n and ");
					}
					
					strBuf.append(queryFieldList.get(i).getPSStr());
				}
			}

			db.initPS(strBuf.toString());
			
			int j=1;
			for(int i=0; i<fieldList.size();i++){
				
				if(!(Boolean)updateFlagList.get(i)){
					continue;
				}
				TblBaseUtil.setPSValue(db, j, tbl.getFieldType(i), valueList.get(i));
				j=j+1;
			}
	
			if(queryFieldList != null && queryFieldList.size() > 0){
				for(int i=0;i<queryFieldList.size();i++){
					
					for(int k=0;k<queryFieldList.get(i).getPSQMarkCount();k++){
						TblBaseUtil.setPSValue(db, j, tbl.getFieldType(i), queryFieldList.get(i).getPSValue(k));
						j=j+1;
					}
				}
			}
	
			return db.executeUpdate();
		}catch(Exception e){
			logger.trace(e);
			throw e;
		}finally{
			strBuf = null;
			//logger.debug("----- updateByCondition OUT -----");
		}	
	}
	
	public static int updateByCondition(DbBase db, TblBase tblUpdate, MultiTblQueryCondition mtqc) throws Exception {
		//logger.debug("----- updateByCondition IN -----");
		StringBuffer strBuf = new StringBuffer();
		try{

			if(mtqc.getTblList().size() < 2){
				throw new Exception("at least need two tables");
			}
			
			//if select from 3 tables , we need at least 2 connect keys
			if(mtqc.getTblList().size() > (mtqc.getConnectKeysList().size() + 1)){
				throw new Exception("not enough table connect keys!");
			}
			
			strBuf.append("update ");
			strBuf.append(tblUpdate.getTblName());
			strBuf.append(" set\n");
			ArrayList<String> fieldList = tblUpdate.getFieldList();
			ArrayList<Object> valueList = tblUpdate.getValueList();
			
			ArrayList<Boolean> updateFlagList = tblUpdate.getUpdateFlagList();
	
			for(int i=0; i<fieldList.size();i++){
				
				if(!(Boolean)updateFlagList.get(i)){
					continue;
				}
				
				strBuf.append(fieldList.get(i));
				strBuf.append("=?,\n");
			}
			strBuf.deleteCharAt(strBuf.length()-2);
			
			//use exists
			strBuf.append("\n where exists (select 1 from \n");
			
			//from table
			TblBase tbl;
			boolean isFirstTbl = true;
			for(int i=0;i<mtqc.getTblList().size();i++){
				tbl = mtqc.getTblList().get(i);
				
				//if same table
				if(tbl.getTblName().equals(tblUpdate.getTblName())){
					continue;
				}
				
				if(isFirstTbl){
					isFirstTbl = false;
				}else{
					strBuf.append(",");
				}
				
				strBuf.append(tbl.getTblName());
			}
			
			strBuf.append("\n where \n");
			
			//table connect keys
			TblConnectKeys tblConnectKeys;
			boolean isFirst = true;
			for(int i=0;i<mtqc.getConnectKeysList().size();i++){
				tblConnectKeys = mtqc.getConnectKeysList().get(i);
				if(isFirst){
					isFirst = false;
				}else{
					strBuf.append("\n and ");
				}
				
				strBuf.append(tblConnectKeys.getTbl1());
				strBuf.append(".");
				strBuf.append(tblConnectKeys.getFieldName1());
				strBuf.append("=");
				strBuf.append(tblConnectKeys.getTbl2());
				strBuf.append(".");
				strBuf.append(tblConnectKeys.getFieldName2());
			}
			
			//queryCondition
			ArrayList<QueryField> queryFieldList = mtqc.getQueryFieldList();
			if(queryFieldList != null && queryFieldList.size() > 0){
				for(int i=0;i<queryFieldList.size();i++){
					
					strBuf.append("\n and ");
					strBuf.append(queryFieldList.get(i).getPSStr());
				}
			}
			
			//close exists ()
			strBuf.append("\n ) ");
			
logger.debug(strBuf.toString());
			db.initPS(strBuf.toString());
			
			int j=1;
			
			//update value
			for(int i=0; i<fieldList.size();i++){
				
				if(!(Boolean)updateFlagList.get(i)){
					continue;
				}
				TblBaseUtil.setPSValue(db, j, tblUpdate.getFieldType(i), valueList.get(i));
				j=j+1;
			}
			
			//query condition
			if(queryFieldList != null && queryFieldList.size() > 0){
				for(int i=0;i<queryFieldList.size();i++){
					
					for(int k=0;k<queryFieldList.get(i).getPSQMarkCount();k++){
						TblBaseUtil.setPSValue(db, j, queryFieldList.get(i).getFieldType(), queryFieldList.get(i).getPSValue(k));
						j=j+1;
					}
				}
			}
	
			return db.executeUpdate();
		}catch(Exception e){
			logger.trace(e);
			throw e;
		}finally{
			strBuf = null;
			//logger.debug("----- removeByCondition OUT -----");
		}	
	}
	
	public static int removeByCondition(DbBase db, SingleTblQueryCondition stqc) throws Exception {
		//logger.debug("----- removeByCondition IN -----");
		StringBuffer strBuf = new StringBuffer();
		try{
			TblBase tblCondition = stqc.getTbl();
			
			strBuf.append("delete from ");
			strBuf.append(tblCondition.getTblName());
			
			//queryCondition
			ArrayList<QueryField> queryFieldList = stqc.getQueryFieldList();
			if(queryFieldList != null && queryFieldList.size() > 0){
				strBuf.append(" where \n");
				
				boolean isFirst = true;
				for(int i=0;i<queryFieldList.size();i++){
					
					if(isFirst){
						isFirst = false;
					}else{
						strBuf.append("\n and ");
					}
					
					strBuf.append(queryFieldList.get(i).getPSStr());
				}
			}
			
			db.initPS(strBuf.toString());
			
			int j=1;
			if(queryFieldList != null && queryFieldList.size() > 0){
				for(int i=0;i<queryFieldList.size();i++){
					
					for(int k=0;k<queryFieldList.get(i).getPSQMarkCount();k++){
						TblBaseUtil.setPSValue(db, j, queryFieldList.get(i).getFieldType(), queryFieldList.get(i).getPSValue(k));
						j=j+1;
					}
				}
			}
	
			return db.executeUpdate();
		}catch(Exception e){
			logger.trace(e);
			throw e;
		}finally{
			strBuf = null;
			//logger.debug("----- removeByCondition OUT -----");
		}	
	}
	
	public static int removeByCondition(DbBase db, TblBase tblRemove, MultiTblQueryCondition mtqc) throws Exception {
		//logger.debug("----- removeByCondition IN -----");
		StringBuffer strBuf = new StringBuffer();
		try{

			if(mtqc.getTblList().size() < 2){
				throw new Exception("at least need two tables");
			}
			
			//if select from 3 tables , we need at least 2 connect keys
			if(mtqc.getTblList().size() > (mtqc.getConnectKeysList().size() + 1)){
				throw new Exception("not enough table connect keys!");
			}
			
			strBuf.append("delete from ");
			strBuf.append(tblRemove.getTblName());
			
			//use exists
			strBuf.append("\n where exists (select 1 from \n");
			
			//from table
			TblBase tbl;
			boolean isFirstTbl = true;
			for(int i=0;i<mtqc.getTblList().size();i++){
				tbl = mtqc.getTblList().get(i);
				
				//if same table
				if(tbl.getTblName().equals(tblRemove.getTblName())){
					continue;
				}
				
				if(isFirstTbl){
					isFirstTbl = false;
				}else{
					strBuf.append(",");
				}
				
				strBuf.append(tbl.getTblName());
			}
			
			strBuf.append("\n where \n");
			
			//table connect keys
			TblConnectKeys tblConnectKeys;
			boolean isFirst = true;
			for(int i=0;i<mtqc.getConnectKeysList().size();i++){
				tblConnectKeys = mtqc.getConnectKeysList().get(i);
				if(isFirst){
					isFirst = false;
				}else{
					strBuf.append("\n and ");
				}
				
				strBuf.append(tblConnectKeys.getTbl1());
				strBuf.append(".");
				strBuf.append(tblConnectKeys.getFieldName1());
				strBuf.append("=");
				strBuf.append(tblConnectKeys.getTbl2());
				strBuf.append(".");
				strBuf.append(tblConnectKeys.getFieldName2());
			}
			
			//queryCondition
			ArrayList<QueryField> queryFieldList = mtqc.getQueryFieldList();
			if(queryFieldList != null && queryFieldList.size() > 0){
				for(int i=0;i<queryFieldList.size();i++){
					
					strBuf.append("\n and ");
					strBuf.append(queryFieldList.get(i).getPSStr());
				}
			}
			
			//close exists ()
			strBuf.append("\n ) ");
			
logger.debug(strBuf.toString());
			db.initPS(strBuf.toString());
			
			int j=1;
			if(queryFieldList != null && queryFieldList.size() > 0){
				for(int i=0;i<queryFieldList.size();i++){
					
					for(int k=0;k<queryFieldList.get(i).getPSQMarkCount();k++){
						TblBaseUtil.setPSValue(db, j, queryFieldList.get(i).getFieldType(), queryFieldList.get(i).getPSValue(k));
						j=j+1;
					}
				}
			}
	
			return db.executeUpdate();
		}catch(Exception e){
			logger.trace(e);
			throw e;
		}finally{
			strBuf = null;
			//logger.debug("----- removeByCondition OUT -----");
		}	
	}
	

	public static ArrayList<TblBase> queryByCondition(DbBase db, SingleTblQueryCondition stqc) throws Exception {
		//logger.debug("----- queryByCondition IN -----");
		ArrayList<TblBase> list = new ArrayList<TblBase>();
		int rowCount = getCountByCondition(db, stqc);
		stqc.getPc().setRowCount(rowCount);
		if(rowCount == 0){
			return list;
		}
		
		StringBuffer strBuf = new StringBuffer();
		ResultSet rs = null;
		try{
			TblBase tblCondition = stqc.getTbl();
			//for select with rownum
			strBuf.append("select ");
			strBuf.append(tblCondition.getTblName());
			strBuf.append(".*");
			strBuf.append(" from ");
			strBuf.append(tblCondition.getTblName());
			
			//queryCondition
			boolean isFirst = true;
			ArrayList<QueryField> queryFieldList = stqc.getQueryFieldList();
			if(queryFieldList != null && queryFieldList.size() > 0){
				strBuf.append(" where \n");
				
				for(int i=0;i<queryFieldList.size();i++){
					
					if(isFirst){
						isFirst = false;
					}else{
						strBuf.append("\n and ");
					}
					
					strBuf.append(queryFieldList.get(i).getPSStr());
				}
			}
			
			//sort, when use limit , sql must have order by
			if(stqc.getSortFieldList().size() > 0){
				strBuf.append(SortField.getSortString(stqc.getSortFieldList()));
			}else{
				strBuf.append(" order by " + tblCondition.getFieldList().get(0) + " asc ");
			}
			
			//limit
			if(!stqc.getPc().isShowAll()){
				//mysql's limit start with 0, not 1
				strBuf.append(" limit " + (stqc.getPc().getStartRow()-1));
				strBuf.append("," + stqc.getPc().getPageSize());
			}
			
logger.debug("sql=" + strBuf.toString());
			db.initPS(strBuf.toString());
			
			int j=1;
			if(queryFieldList != null && queryFieldList.size() > 0){
				for(int i=0;i<queryFieldList.size();i++){
					
					for(int k=0;k<queryFieldList.get(i).getPSQMarkCount();k++){
						TblBaseUtil.setPSValue(db, j, queryFieldList.get(i).getFieldType(), queryFieldList.get(i).getPSValue(k));
						j=j+1;
					}
				}
			}
	
			rs = db.executeQuery();
			
			list = TblBaseService.getTblListFromRS(tblCondition, rs);
			
			rs.close();
			
			//limit reached
			if(list.size() == stqc.getLimit() && stqc.getLimit() > 0){
				stqc.setLimitReached(true);
			}else{
				stqc.setLimitReached(false);
			}
			
			return list;
		}catch(Exception e){
			logger.trace(e);
			throw e;
		}finally{
			strBuf = null;
			db.closeResultSet(rs);
			//logger.debug("----- queryByCondition OUT -----");
		}	
	}
	
	public static HashMap<String,ArrayList<TblBase>> queryByCondition(DbBase db, MultiTblQueryCondition mtqc) throws Exception {
		
		//logger.debug("----- queryByCondition IN -----");
		int rowCount = getCountByCondition(db, mtqc);
		mtqc.getPc().setRowCount(rowCount);
		if(rowCount == 0){
			return new HashMap<String,ArrayList<TblBase>>();
		}
		
		StringBuffer strBuf = new StringBuffer();
		ResultSet rs = null;
		try{
			
			if(mtqc.getTblList().size() < 2){
				throw new Exception("at least need two tables");
			}
			
			//if select from 3 tables , we need at least 2 connect keys
			if(mtqc.getTblList().size() > (mtqc.getConnectKeysList().size() + 1)){
				throw new Exception("not enough table connect keys!");
			}
			
			strBuf.append("select ");
			TblBase tbl;
			boolean isFirstTbl = true;
			for(int i=0;i<mtqc.getTblList().size();i++){
				tbl = mtqc.getTblList().get(i);
				//unSelect table will not select, just act as condition
				if(mtqc.getUnSelectTblList().contains(tbl.getTblName())){
					continue;
				}else{
					
					for(int k=0;k<tbl.getFieldList().size();k++){
						if(isFirstTbl){
							isFirstTbl = false;
						}else{
							strBuf.append(",");
						}
						
						strBuf.append(tbl.getTblName());
						strBuf.append(".");
						strBuf.append(tbl.getFieldList().get(k));
						strBuf.append(" ");
						strBuf.append(tbl.getTblName());
						strBuf.append("_");
						strBuf.append(tbl.getFieldList().get(k));
					}

				}
			}
			
			if(isFirstTbl){
				throw new Exception("no table defined to select");
			}
			
			//from table
			strBuf.append("\n from ");
			isFirstTbl = true;
			for(int i=0;i<mtqc.getTblList().size();i++){
				tbl = mtqc.getTblList().get(i);
				
				if(isFirstTbl){
					isFirstTbl = false;
				}else{
					strBuf.append(",");
				}
				
				strBuf.append(tbl.getTblName());
			}
			
			strBuf.append("\n where \n");
			
			//table connect keys
			TblConnectKeys tblConnectKeys;
			boolean isFirst = true;
			for(int i=0;i<mtqc.getConnectKeysList().size();i++){
				tblConnectKeys = mtqc.getConnectKeysList().get(i);
				if(isFirst){
					isFirst = false;
				}else{
					strBuf.append("\n and ");
				}
				
				strBuf.append(tblConnectKeys.getTbl1());
				strBuf.append(".");
				strBuf.append(tblConnectKeys.getFieldName1());
				strBuf.append("=");
				strBuf.append(tblConnectKeys.getTbl2());
				strBuf.append(".");
				strBuf.append(tblConnectKeys.getFieldName2());
			}
			
			//queryCondition
			ArrayList<QueryField> queryFieldList = mtqc.getQueryFieldList();
			if(queryFieldList != null && queryFieldList.size() > 0){
				for(int i=0;i<queryFieldList.size();i++){
					
					strBuf.append("\n and ");
					strBuf.append(queryFieldList.get(i).getPSStr());
				}
			}
			
			//sort, when use limit , sql must have order by
			if(mtqc.getSortFieldList().size() > 0){
				strBuf.append(SortField.getSortString(mtqc.getSortFieldList()));
			}else{
				strBuf.append(" order by ");
				strBuf.append(mtqc.getTblList().get(0).getTblName());
				strBuf.append("_" + mtqc.getTblList().get(0).getFieldList().get(0));
				strBuf.append(" asc ");
			}
			
			//limit
			if(!mtqc.getPc().isShowAll()){
				//mysql's limit start with 0, not 1
				strBuf.append(" limit " + (mtqc.getPc().getStartRow()-1));
				strBuf.append("," + mtqc.getPc().getPageSize());
			}
			
logger.debug(strBuf.toString());
			db.initPS(strBuf.toString());
			
			int j=1;
			if(queryFieldList != null && queryFieldList.size() > 0){
				for(int i=0;i<queryFieldList.size();i++){
					
					for(int k=0;k<queryFieldList.get(i).getPSQMarkCount();k++){
						TblBaseUtil.setPSValue(db, j, queryFieldList.get(i).getFieldType(), queryFieldList.get(i).getPSValue(k));
						j=j+1;
					}
				}
			}
	
			rs = db.executeQuery();
			
			HashMap<String,ArrayList<TblBase>> tblListMap = TblBaseService.getMultiTblListFromRS(mtqc.getTblList(), mtqc.getUnSelectTblList(), rs);
			
			rs.close();
			
			//limit reached
			int querySize = TblBaseService.getMultiTblQuerySize(tblListMap);
			if(querySize == mtqc.getLimit() && mtqc.getLimit() > 0){
				mtqc.setLimitReached(true);
			}else{
				mtqc.setLimitReached(false);
			}
			
			return tblListMap;
		}catch(Exception e){
			logger.trace(e);
			throw e;
		}finally{
			strBuf = null;
			db.closeResultSet(rs);
			//logger.debug("----- queryByCondition OUT -----");
		}	
	}
	
	public static int getCountByCondition(DbBase db, SingleTblQueryCondition stqc) throws Exception {
		//logger.debug("----- getCountByCondition IN -----");
		StringBuffer strBuf = new StringBuffer();
		ResultSet rs = null;
		try{
			TblBase tblCondition = stqc.getTbl();
			
			strBuf.append("select count(*) from ");
			strBuf.append(tblCondition.getTblName());
			
			//queryCondition
			boolean isFirst = true;
			ArrayList<QueryField> queryFieldList = stqc.getQueryFieldList();
			if(queryFieldList != null && queryFieldList.size() > 0){
				strBuf.append(" where \n");
				
				for(int i=0;i<queryFieldList.size();i++){
					
					if(isFirst){
						isFirst = false;
					}else{
						strBuf.append("\n and ");
					}
					
					strBuf.append(queryFieldList.get(i).getPSStr());
				}
			}

logger.debug("sql=" + strBuf.toString());
			db.initPS(strBuf.toString());
			
			int j=1;
			if(queryFieldList != null && queryFieldList.size() > 0){
				for(int i=0;i<queryFieldList.size();i++){
					
					for(int k=0;k<queryFieldList.get(i).getPSQMarkCount();k++){
						TblBaseUtil.setPSValue(db, j, queryFieldList.get(i).getFieldType(), queryFieldList.get(i).getPSValue(k));
						j=j+1;
					}
				}
			}
	
			rs = db.executeQuery();
			
			rs.next();
			int count = rs.getInt(1);
			
			rs.close();
			
			return count;
		}catch(Exception e){
			logger.trace(e);
			throw e;
		}finally{
			strBuf = null;
			db.closeResultSet(rs);
			//logger.debug("----- getCountByCondition OUT -----");
		}	
	}
	
	public static int getCountByCondition(DbBase db, MultiTblQueryCondition mtqc) throws Exception {
		
		//logger.debug("----- getCountByCondition IN -----");
		StringBuffer strBuf = new StringBuffer();
		ResultSet rs = null;
		try{
			
			if(mtqc.getTblList().size() < 2){
				throw new Exception("at least need two tables");
			}
			
			//if select from 3 tables , we need at least 2 connect keys
			if(mtqc.getTblList().size() > (mtqc.getConnectKeysList().size() + 1)){
				throw new Exception("not enough table connect keys!");
			}
			
			strBuf.append("select count(*) from ");
			
			TblBase tbl;
			boolean isFirstTbl = true;
			for(int i=0;i<mtqc.getTblList().size();i++){
				tbl = mtqc.getTblList().get(i);
				
				if(isFirstTbl){
					isFirstTbl = false;
				}else{
					strBuf.append(",");
				}
				
				strBuf.append(tbl.getTblName());
			}
			
			strBuf.append("\n where \n");
			
			//table connect keys
			TblConnectKeys tblConnectKeys;
			boolean isFirst = true;
			for(int i=0;i<mtqc.getConnectKeysList().size();i++){
				tblConnectKeys = mtqc.getConnectKeysList().get(i);
				if(isFirst){
					isFirst = false;
				}else{
					strBuf.append("\n and ");
				}
				
				strBuf.append(tblConnectKeys.getTbl1());
				strBuf.append(".");
				strBuf.append(tblConnectKeys.getFieldName1());
				strBuf.append("=");
				strBuf.append(tblConnectKeys.getTbl2());
				strBuf.append(".");
				strBuf.append(tblConnectKeys.getFieldName2());
			}
			
			//queryCondition
			ArrayList<QueryField> queryFieldList = mtqc.getQueryFieldList();
			if(queryFieldList != null && queryFieldList.size() > 0){
				for(int i=0;i<queryFieldList.size();i++){
					
					strBuf.append("\n and ");
					strBuf.append(queryFieldList.get(i).getPSStr());
				}
			}
			
logger.debug(strBuf.toString());
			db.initPS(strBuf.toString());
			
			int j=1;
			if(queryFieldList != null && queryFieldList.size() > 0){
				for(int i=0;i<queryFieldList.size();i++){
					
					for(int k=0;k<queryFieldList.get(i).getPSQMarkCount();k++){
						TblBaseUtil.setPSValue(db, j, queryFieldList.get(i).getFieldType(), queryFieldList.get(i).getPSValue(k));
						j=j+1;
					}
				}
			}
	
			rs = db.executeQuery();
			
			rs.next();
			int count = rs.getInt(1);
			
			rs.close();
			
			return count;
		}catch(Exception e){
			logger.trace(e);
			throw e;
		}finally{
			strBuf = null;
			db.closeResultSet(rs);
			//logger.debug("----- getCountByCondition OUT -----");
		}	
	}
	
	//==============================ddl=====================================
	public static String getCreateTblSql(TblBase tbl) throws Exception {
		
		StringBuffer strBuf = new StringBuffer();
		ArrayList<String> fieldList = tbl.getFieldList();
		
		strBuf.append("create table ");
		strBuf.append(tbl.getTblName());
		strBuf.append("\n(\n");
		for(int i=0;i<fieldList.size();i++){
			
			strBuf.append(fieldList.get(i));
			strBuf.append("\t\t");
			strBuf.append(TblBaseUtil.getMysqlColumnTypeDef(tbl.getFieldType(i), tbl.getFieldSize(i)));
			
			if(i==0){
				strBuf.append(" auto_increment primary key ");
			}
			
			if(i<fieldList.size()-1){
				strBuf.append(",\n");
			}else{
				strBuf.append("\n");
			}
		}
		
		strBuf.append(")");
		
		String tblspace = "";
		//patition
		if(DbConfig.usePartition() && tbl.isPartition()){
			
			strBuf.append(" \n ");
			strBuf.append(" partition by range(");
			strBuf.append(tbl.getPartitionField());
			strBuf.append(") \n");
			
			strBuf.append(" (");
			
			long lvalue=0;
			int tblspaceIdx = 0;
			for(int i=0;i<DbConfig.getPartitionCount();i++){
				lvalue = DbConfig.getPartitionSize() * (i+1);
				
				strBuf.append(" partition " + TblBaseUtil.PARTITION_MARK + i);
				
				if(i<DbConfig.getPartitionCount()-1){
					strBuf.append(" values less than (" + lvalue);
					strBuf.append(")");
				}else{
					strBuf.append(" values less than (maxvalue)");
				}
				
				if(DbConfig.getDbTableSpaces().size() > 0){
					tblspaceIdx = i%(DbConfig.getDbTableSpaces().size());
					tblspace = DbConfig.getDbTableSpaces().get(tblspaceIdx);
					strBuf.append(" tablespace " + tblspace);
				}
				
				if(i<DbConfig.getPartitionCount()-1){
					strBuf.append(",\n");
				}
			}
			
			strBuf.append(" ) ");
		}else{
			if(DbConfig.getDbTableSpaces().size() > 0){
				tblspace = DbConfig.getDbTableSpaces().get(0);
				strBuf.append(" tablespace " + tblspace);
			}
		}
		
		return strBuf.toString();
	}
	
	public static String getDropTblSql(TblBase tbl) throws Exception {
		
		StringBuffer strBuf = new StringBuffer();
		
		strBuf.append("drop table ");
		strBuf.append(tbl.getTblName());

		return strBuf.toString();
	}
	
	public static ArrayList<String> getCreateIndexSql(TblBase tbl) throws Exception {

		if(tbl.getIdxFieldList().size() < 1){
			return new ArrayList<String>();
		}
		
		ArrayList<String> sqls = new ArrayList<String>();
		StringBuffer strBuf;
		
		for(int i=0;i<tbl.getIdxFieldList().size();i++){
			strBuf = new StringBuffer();
			strBuf.append("create index ");
			strBuf.append(tbl.getIdxName() + i);
			strBuf.append(" on ");
			strBuf.append(tbl.getTblName());
			strBuf.append("(");
			strBuf.append(tbl.getIdxFieldList().get(i));
			strBuf.append(")");
			
			//new 
			if(DbConfig.getDbIndexSpace() != null && DbConfig.getDbIndexSpace().length() > 0){
				strBuf.append(" tablespace ");
				strBuf.append(DbConfig.getDbIndexSpace());
			}
			
			sqls.add(strBuf.toString());
		}
		
		return sqls;
	}
	
	public static ArrayList<String> getDropIndexSql(TblBase tbl) throws Exception {
		
		if(tbl.getIdxFieldList().size() < 1){
			return new ArrayList<String>();
		}
		
		ArrayList<String> sqls = new ArrayList<String>();
		StringBuffer strBuf;
		
		for(int i=0;i<tbl.getIdxFieldList().size();i++){
			strBuf = new StringBuffer();
			strBuf.append("drop index ");
			strBuf.append(tbl.getIdxName() + i);
			
			sqls.add(strBuf.toString());
		}

		return sqls;
	}
	
	public static int getLastInsertId(DbBase db, TblBase tbl) throws Exception {
		ResultSet rs = null;
		try {
			String strSql = "SELECT LAST_INSERT_ID()";
			rs = db.getResultSet(strSql);
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				throw new Exception("getLastInsertId failed");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			db.closeResultSet(rs);
		}

	}
	
}
