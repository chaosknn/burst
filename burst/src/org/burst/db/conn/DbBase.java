package org.burst.db.conn;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.burst.db.tbl.base.MyBlob;
import org.burst.db.tbl.base.TblBaseUtil;


public class DbBase {

	private static final Logger logger = Logger.getLogger(DbBase.class);
	
	private Connection conn = null;
	private PreparedStatement ps = null;
	private Hashtable stmtPool = new Hashtable();
	
	public DbBase() throws Exception{
		conn = ConnectionPool.getConnection();
		conn.setAutoCommit(false);
	}
	
	public void initPS(String sql) throws Exception {
		try {
			//initConnection();
			if(ps != null){
				ps.close();
			}
			ps = null;
			ps = conn.prepareStatement(sql);
		} catch (Exception e) {
			throw e;
		}
	}

	public void commit() throws Exception {
		try {
			conn.commit();
		} catch (Exception e) {
			throw e;
		}
	}

	public void rollback() {
		try {
			conn.rollback();
		} catch (Exception e) {
		}
	}

	public boolean hasRecord(String sql) throws Exception {
		ResultSet rs = null;
		try {
			rs = getResultSet(sql);
			return rs.next();
		} catch (Exception e) {
			throw e;
		} finally {
			closeResultSet(rs);
		}
	}

	public ResultSet getResultSet(String sql) throws Exception {

		ResultSet rs = null;
		try {
			//initConnection();
			initPS(sql);

			rs = ps.executeQuery();
			stmtPool.put(rs, ps);
			return rs;
		} catch (Exception e) {
			if (ps != null) {
				try {
					ps.close();
					ps = null;
				} catch (Exception ex) {
				}
			}
			throw e;
		}
	}

	public void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				PreparedStatement stmt = (PreparedStatement) stmtPool.get(rs);
				stmtPool.remove(rs);
				rs.close();
				rs = null;
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			}
		} catch (Exception e) {
		}
	}

	public void executeUpdate(String sql) throws Exception {
		int i = -1;
		try {
			initPS(sql);
			i = ps.executeUpdate();
			if (i < 1) {
				throw new Exception("execute failed");
			}
		} catch (Exception e) {
			throw e;
		}finally{
			ps.close();
		}

	}

	public int executeUpdate() throws Exception {
		int i = -1;
		try {
			i = ps.executeUpdate();
			if (i < 0) {
				throw new Exception("execute failed");
			}
			
			return i;
		} catch (Exception e) {
			throw e;
		}finally{
			ps.close();
		}

	}

	public ResultSet executeQuery() throws Exception {
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
			stmtPool.put(rs, ps);
			return rs;
		} catch (Exception e) {
			if (ps != null) {
				try {
					ps.close();
					ps = null;
				} catch (Exception ex) {
				}
			}
			throw e;
		}
	}

	public boolean getLockForUpdate(String sql) throws Exception {
		boolean bRtn = false;

		ResultSet rs = null;
		try {
			//initConnection();
			sql = sql + " FOR UPDATE NOWAIT ";
			ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs = ps.executeQuery();
			bRtn = true;
			rs.close();
		} catch (Exception e) {
			if (e instanceof SQLException) {
				if (((SQLException) e).getErrorCode() == 54) {
					bRtn = false;
				} else {
					throw e;
				}
			}
		} finally {
			if (ps != null) {
				try {
					ps.close();
					ps = null;
				} catch (Exception ex) {
				}
			}
		}
		return bRtn;
	}

	public void release() {
		try {
			if (ps != null) {
				ps.close();
				ps = null;
			}

			clearStatementPool();
			
			ConnectionPool.returnConnection(conn);

		} catch (Exception e) {
		}
	}

	private void clearStatementPool() {
		for (Enumeration e = stmtPool.keys(); e.hasMoreElements();) {
			Object obj = e.nextElement();
			if (obj != null) {
				closeResultSet((ResultSet) obj);
			}
		}
	}

	public Connection getConn() throws Exception {
		return conn;
	}

	public void setConn(Connection connection) {
		conn = connection;
	}

	public void setPSInt(int idx, int value) throws Exception {
		ps.setInt(idx, value);
	}

	public void setPSDouble(int idx, Double value) throws Exception {
		ps.setDouble(idx, value);
	}
	
	public void setPSDate(int idx, Date value) throws Exception {
		ps.setTimestamp(idx, TblBaseUtil.utilDateToTimestamp(value));
	}
	
	public void setPSBlob(int idx, String value) throws Exception {
		if(value == null){
			value = "";
		}
		ps.setObject(idx, value);

	}
	
	public void setPSBlob(int idx, Blob value) throws Exception {
		ps.setBlob(idx, value);
	}
	
	public void setPSBlob(int idx, MyBlob value) throws Exception {
		ps.setBinaryStream(idx, value.getInputStream(), value.getInputStream().available());
	}
	
	public void setPSString(int idx, String value) throws Exception {
		if(value == null){
			value = "";
		}
		if(value.length() > TblBaseUtil.MAX_LENGTH){
			value = value.substring(0, TblBaseUtil.MAX_LENGTH);
		}
		ps.setString(idx, value);
	}
	
	public void setPSNull(int idx, int sqlType) throws Exception {
		ps.setNull(idx, sqlType);
	}
}