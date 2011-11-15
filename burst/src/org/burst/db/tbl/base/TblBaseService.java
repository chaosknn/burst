package org.burst.db.tbl.base;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.burst.db.config.DbConfig;
import org.burst.db.conn.DbBase;
import org.burst.db.query.MultiTblQueryCondition;
import org.burst.db.query.SingleTblQueryCondition;


public class TblBaseService {
	private static final Logger logger = Logger.getLogger(TblBaseService.class);
	
	//==============================create=====================================
	//return create record's id
	public static int create(DbBase db, TblBase tbl) throws Exception {
		
		if(DbConfig.isOracle()){
			return TblBaseServiceOracle.create(db, tbl);
		}else if(DbConfig.isDb2()){
			return TblBaseServiceDb2.create(db, tbl);
		}else if(DbConfig.isSqlserver()){
			return TblBaseServiceSqlserver.create(db, tbl);
		}else if(DbConfig.isMysql()){
			return TblBaseServiceMysql.create(db, tbl);
		}else{
			throw new Exception("db type not valid");
		}
	}
	
	//==============================update=====================================
	public static int updateById(DbBase db, TblBase tbl) throws Exception {
		SingleTblQueryCondition stqc = SingleTblQueryCondition.getSTQCById(tbl);
		return updateByCondition(db, tbl, stqc);
	}
	
	public static int updateByIdList(DbBase db, TblBase tbl, ArrayList<Integer> idList) throws Exception {
		SingleTblQueryCondition stqc = SingleTblQueryCondition.getSTQCByIdList(tbl, idList);
		
		return updateByCondition(db, tbl, stqc);

	}
	
	public static int updateByCondition(DbBase db, TblBase tbl, SingleTblQueryCondition stqc) throws Exception {
		if(DbConfig.isOracle()){
			return TblBaseServiceOracle.updateByCondition(db, tbl, stqc);
		}else if(DbConfig.isDb2()){
			return TblBaseServiceDb2.updateByCondition(db, tbl, stqc);
		}else if(DbConfig.isSqlserver()){
			return TblBaseServiceSqlserver.updateByCondition(db, tbl, stqc);
		}else if(DbConfig.isMysql()){
			return TblBaseServiceMysql.updateByCondition(db, tbl, stqc);
		}else{
			throw new Exception("db type not valid");
		}
	}
	
	//update by multi table condition
	public static int updateByCondition(DbBase db, TblBase tblUpdate, MultiTblQueryCondition mtqc) throws Exception {
		if(DbConfig.isOracle()){
			return TblBaseServiceOracle.updateByCondition(db, tblUpdate, mtqc);
		}else if(DbConfig.isDb2()){
			return TblBaseServiceDb2.updateByCondition(db, tblUpdate, mtqc);
		}else if(DbConfig.isSqlserver()){
			return TblBaseServiceSqlserver.updateByCondition(db, tblUpdate, mtqc);
		}else if(DbConfig.isMysql()){
			return TblBaseServiceMysql.updateByCondition(db, tblUpdate, mtqc);
		}else{
			throw new Exception("db type not valid");
		}
	}
	
	//remove
	public static int removeById(DbBase db, TblBase tbl) throws Exception {
		
		SingleTblQueryCondition stqc = SingleTblQueryCondition.getSTQCById(tbl);
		return removeByCondition(db, stqc);
		
	}
	
	public static int removeByIdList(DbBase db, TblBase tbl, ArrayList<Integer> idList) throws Exception {
		SingleTblQueryCondition stqc = SingleTblQueryCondition.getSTQCByIdList(tbl, idList);
		
		return removeByCondition(db, stqc);
	}

	//remove all data
	public static int removeAllData(DbBase db, TblBase tbl) throws Exception {
		SingleTblQueryCondition stqc = new SingleTblQueryCondition();
		stqc.setTbl(tbl);
		return removeByCondition(db, stqc);
	}
	
	public static int removeByCondition(DbBase db, SingleTblQueryCondition stqc) throws Exception {
		if(DbConfig.isOracle()){
			return TblBaseServiceOracle.removeByCondition(db, stqc);
		}else if(DbConfig.isDb2()){
			return TblBaseServiceDb2.removeByCondition(db, stqc);
		}else if(DbConfig.isSqlserver()){
			return TblBaseServiceSqlserver.removeByCondition(db, stqc);
		}else if(DbConfig.isMysql()){
			return TblBaseServiceMysql.removeByCondition(db, stqc);
		}else{
			throw new Exception("db type not valid");
		}
	}
	
	//remove by multi table condition
	public static int removeByCondition(DbBase db, TblBase tblRemove, MultiTblQueryCondition mtqc) throws Exception {
		if(DbConfig.isOracle()){
			return TblBaseServiceOracle.removeByCondition(db, tblRemove, mtqc);
		}else if(DbConfig.isDb2()){
			return TblBaseServiceDb2.removeByCondition(db, tblRemove, mtqc);
		}else if(DbConfig.isSqlserver()){
			return TblBaseServiceSqlserver.removeByCondition(db, tblRemove, mtqc);
		}else if(DbConfig.isMysql()){
			return TblBaseServiceMysql.removeByCondition(db, tblRemove, mtqc);
		}else{
			throw new Exception("db type not valid");
		}
	}
	
	//query
	public static TblBase queryById(DbBase db, TblBase tbl) throws Exception {

		SingleTblQueryCondition stqc = SingleTblQueryCondition.getSTQCById(tbl);
		
		ArrayList<TblBase> list = queryByCondition(db, stqc);
		
		if(list.size() >0){
			return (TblBase)list.get(0);
		}else{
			return null;
		}
	}

	public static ArrayList<TblBase> queryByIdList(DbBase db, TblBase tbl, ArrayList<Integer> idList) throws Exception {

		SingleTblQueryCondition stqc = SingleTblQueryCondition.getSTQCByIdList(tbl, idList);
		
		ArrayList<TblBase> list = queryByCondition(db, stqc);
		
		return list;
	}

	
	public static ArrayList<TblBase> queryByCondition(DbBase db, SingleTblQueryCondition stqc) throws Exception {
		if(DbConfig.isOracle()){
			return TblBaseServiceOracle.queryByCondition(db, stqc);
		}else if(DbConfig.isDb2()){
			return TblBaseServiceDb2.queryByCondition(db, stqc);
		}else if(DbConfig.isSqlserver()){
			return TblBaseServiceSqlserver.queryByCondition(db, stqc);
		}else if(DbConfig.isMysql()){
			return TblBaseServiceMysql.queryByCondition(db, stqc);
		}else{
			throw new Exception("db type not valid");
		}
	}
	
	public static HashMap<String,ArrayList<TblBase>> queryByCondition(DbBase db, MultiTblQueryCondition mtqc) throws Exception {
		
		if(DbConfig.isOracle()){
			return TblBaseServiceOracle.queryByCondition(db, mtqc);
		}else if(DbConfig.isDb2()){
			return TblBaseServiceDb2.queryByCondition(db, mtqc);
		}else if(DbConfig.isSqlserver()){
			return TblBaseServiceSqlserver.queryByCondition(db, mtqc);
		}else if(DbConfig.isMysql()){
			return TblBaseServiceMysql.queryByCondition(db, mtqc);
		}else{
			throw new Exception("db type not valid");
		}
	}
	
	public static ArrayList<TblBase> getTblListFromRS(TblBase tbl, ResultSet rs) throws Exception{
		ArrayList<TblBase> list = new ArrayList<TblBase>();
		
		TblBase tbl1 = null;
		while(rs.next()){
			tbl1 = tbl.newTbl();
			for(int i=0;i<tbl.getFieldList().size();i++){
				TblBaseUtil.setTblValue(rs, tbl1, i);
			}

			list.add(tbl1);
		}
		
		return list;
	}
	
	public static HashMap<String,ArrayList<TblBase>> getMultiTblListFromRS(ArrayList<TblBase> tblList, ArrayList<String> unSelectTblList, ResultSet rs) throws Exception{
		HashMap<String,ArrayList<TblBase>> tblListMap = new HashMap<String,ArrayList<TblBase>>();
		
		ArrayList<TblBase> selectTblList = new ArrayList<TblBase>();
		String tblName;
		for(int i=0;i<tblList.size();i++){
			tblName = tblList.get(i).getTblName();
			if(unSelectTblList.contains(tblName)){
				continue;
			}
			
			selectTblList.add(tblList.get(i));
			
			//init map,list
			tblListMap.put(tblName, new ArrayList<TblBase>());
		}
		
		ArrayList<TblBase> list;
		TblBase tbl1 = null;
		while(rs.next()){
			
			for(int k=0;k<selectTblList.size();k++){
				tbl1 = selectTblList.get(k).newTbl();
				tblName = tbl1.getTblName();
				
				for(int i=0;i<tbl1.getFieldList().size();i++){
					TblBaseUtil.setTblValue(rs, tbl1, i, true);
				}
				
				list = tblListMap.get(tblName);
				list.add(tbl1);
			}
		}
		
		return tblListMap;
	}
	
	//hasRecord
	public static boolean hasRecordByCondition(DbBase db, SingleTblQueryCondition stqc) throws Exception {
		int limit = stqc.getLimit();
		stqc.setLimit(1);
		queryByCondition(db, stqc);
		boolean hasRecord = stqc.isLimitReached();
		stqc.setLimit(limit);
		return hasRecord;
	}
	
	//hasRecord
	public static boolean hasRecordByCondition(DbBase db, MultiTblQueryCondition mtqc) throws Exception {
		int limit = mtqc.getLimit();
		mtqc.setLimit(1);
		queryByCondition(db, mtqc);
		boolean hasRecord = mtqc.isLimitReached();
		mtqc.setLimit(limit);
		
		return hasRecord;
	}
	
	public static int getMultiTblQuerySize(HashMap<String,ArrayList<TblBase>> tblListMap) throws Exception{
		Iterator<String> iter = tblListMap.keySet().iterator();
		String key;
		ArrayList<TblBase> tblList;
		if(iter.hasNext()){
			key = iter.next();
			tblList = tblListMap.get(key);
			return tblList.size();
		}else{
			return 0;
		}
	}
	
	//get id
	public static int getNextSequenceValue(DbBase db, TblBase tbl) throws Exception {
		
		if(DbConfig.isOracle()){
			return TblBaseServiceOracle.getNextSequenceValue(db, tbl);
		}else if(DbConfig.isDb2()){
			return TblBaseServiceDb2.getNextSequenceValue(db, tbl);
		}else if(DbConfig.isSqlserver()){
			return 0;
		}else if(DbConfig.isMysql()){
			return 0;
		}else{
			throw new Exception("db type not valid");
		}

	}
	
	//==============================ddl=====================================
	public static void createTable(DbBase db, TblBase tbl) throws Exception {
		
		String sql = getCreateTblSql(tbl);
		logger.debug(sql);
		db.initPS(sql);
		db.executeUpdate();
	}
	
	public static void dropTable(DbBase db, TblBase tbl) throws Exception {
		
		String sql = getDropTblSql(tbl);
		db.initPS(sql);
		db.executeUpdate();
	}
	
	public static void createSequence(DbBase db, TblBase tbl) throws Exception {
		
		String sql = getCreateSequenceSql(tbl);
		if(sql.length() == 0){
			return;
		}
		db.initPS(sql);
		db.executeUpdate();
	}
	
	public static void dropSequence(DbBase db, TblBase tbl) throws Exception {
		
		String sql = getDropSequenceSql(tbl);
		if(sql.length() == 0){
			return;
		}
		db.initPS(sql);
		db.executeUpdate();
	}
	
	public static void createIndex(DbBase db, TblBase tbl) throws Exception {
		ArrayList<String> sqls = getCreateIndexSql(tbl);
		String sql;
		for(int i=0;i<sqls.size();i++){
			sql = sqls.get(i);
			logger.debug(sql);
			db.initPS(sql);
			db.executeUpdate();
		}
	}
	
	public static void dropIndex(DbBase db, TblBase tbl) throws Exception {
		
		ArrayList<String> sqls = getDropIndexSql(tbl);
		String sql;
		for(int i=0;i<sqls.size();i++){
			sql = sqls.get(i);
			logger.debug(sql);
			db.initPS(sql);
			db.executeUpdate();
		}
	}

	public static String getCreateTblSql(TblBase tbl) throws Exception {
		
		if(DbConfig.isOracle()){
			return TblBaseServiceOracle.getCreateTblSql(tbl);
		}else if(DbConfig.isDb2()){
			return TblBaseServiceDb2.getCreateTblSql(tbl);
		}else if(DbConfig.isSqlserver()){
			return TblBaseServiceSqlserver.getCreateTblSql(tbl);
		}else if(DbConfig.isMysql()){
			return TblBaseServiceMysql.getCreateTblSql(tbl);
		}else{
			throw new Exception("db type not valid");
		}
	}
	
	public static String getDropTblSql(TblBase tbl) throws Exception {
		if(DbConfig.isOracle()){
			return TblBaseServiceOracle.getDropTblSql(tbl);
		}else if(DbConfig.isDb2()){
			return TblBaseServiceDb2.getDropTblSql(tbl);
		}else if(DbConfig.isSqlserver()){
			return TblBaseServiceSqlserver.getDropTblSql(tbl);
		}else if(DbConfig.isMysql()){
			return TblBaseServiceMysql.getDropTblSql(tbl);
		}else{
			throw new Exception("db type not valid");
		}
	}
	
	public static String getCreateSequenceSql(TblBase tbl) throws Exception {
		if(DbConfig.isOracle()){
			return TblBaseServiceOracle.getCreateSequenceSql(tbl);
		}else if(DbConfig.isDb2()){
			return TblBaseServiceDb2.getCreateSequenceSql(tbl);
		}else if(DbConfig.isSqlserver()){
			return "";
		}else if(DbConfig.isMysql()){
			return "";
		}else{
			throw new Exception("db type not valid");
		}
	}
	
	public static String getDropSequenceSql(TblBase tbl) throws Exception {

		if(DbConfig.isOracle()){
			return TblBaseServiceOracle.getDropSequenceSql(tbl);
		}else if(DbConfig.isDb2()){
			return TblBaseServiceDb2.getDropSequenceSql(tbl);
		}else if(DbConfig.isSqlserver()){
			return "";
		}else if(DbConfig.isMysql()){
			return "";
		}else{
			throw new Exception("db type not valid");
		}
	}
	
	public static ArrayList<String> getCreateIndexSql(TblBase tbl) throws Exception {

		if(DbConfig.isOracle()){
			return TblBaseServiceOracle.getCreateIndexSql(tbl);
		}else if(DbConfig.isDb2()){
			return TblBaseServiceDb2.getCreateIndexSql(tbl);
		}else if(DbConfig.isSqlserver()){
			return TblBaseServiceSqlserver.getCreateIndexSql(tbl);
		}else if(DbConfig.isMysql()){
			return TblBaseServiceMysql.getCreateIndexSql(tbl);
		}else{
			throw new Exception("db type not valid");
		}
	}
	
	public static ArrayList<String> getDropIndexSql(TblBase tbl) throws Exception {
		
		if(DbConfig.isOracle()){
			return TblBaseServiceOracle.getDropIndexSql(tbl);
		}else if(DbConfig.isDb2()){
			return TblBaseServiceDb2.getDropIndexSql(tbl);
		}else if(DbConfig.isSqlserver()){
			return TblBaseServiceSqlserver.getDropIndexSql(tbl);
		}else if(DbConfig.isMysql()){
			return TblBaseServiceMysql.getDropIndexSql(tbl);
		}else{
			throw new Exception("db type not valid");
		}
	}
	
}
