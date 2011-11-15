package org.burst.db.conn;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.burst.db.config.DbConfig;


public class ConnectionPool {
	
	private static final Logger logger = Logger.getLogger(ConnectionPool.class);
	
	private static String jdbcDriver = ""; // jdbc driver
	private static String dbUrl = ""; // connection URL
	private static String dbUsername = ""; // connect username
	private static String dbPassword = ""; // connect password
	private static String testTable = ""; // test table
	private static int initialConnections = 10; // connection pool size
	private static int incrementalConnections = 5;// connection pool increment size
	private static int maxConnections = 50; // connection pool max size

	//connection pool
	private static Vector<PooledConnection> connections = null; 
	
	private static boolean inited = false;
	
	public static void init(boolean bForce) throws Exception{
		if(inited && !bForce){
			return;
		}
		
		//logger.debug("start init database connection pool");
		
		//load system settings
		jdbcDriver = DbConfig.getDbDriver();
		dbUrl = DbConfig.getDbConnectString();
		
		dbUsername = DbConfig.getDbUser();
		dbPassword = DbConfig.getDbPassword();
		initialConnections = DbConfig.getDbPoolMinSize();
		incrementalConnections = DbConfig.getDbPoolAddSize();
		maxConnections = DbConfig.getDbPoolMaxSize();
		
		logger.debug("database settings loaded");
		
		//createPool
		createPool();
			
		inited = true;
		
		//logger.debug("end init database connection pool");
	}

	//create connection pool
	private static synchronized void createPool() throws Exception {

		//search for jdbc driver
		Driver driver = (Driver) (Class.forName(jdbcDriver).newInstance());
		DriverManager.registerDriver(driver); 

		// init pool
		connections = new Vector<PooledConnection>();
		// create pool
		createConnections(initialConnections);

	}

	private static void createConnections(int numConnections) throws SQLException {

		for (int x = 0; x < numConnections; x++) {
			if (maxConnections > 0 && connections.size() >= maxConnections) {
				break;
			}
			try {
				connections.addElement(new PooledConnection(newConnection()));
			} catch (SQLException e) {
				logger.debug("create connection failed: " + e.getMessage());
				throw new SQLException();
			}

		}

	}

	//create new connection
	private static Connection newConnection() throws SQLException {

		Connection conn = DriverManager.getConnection(dbUrl, dbUsername,dbPassword);
		conn.setAutoCommit(false);

		if (connections.size() == 0) {
			DatabaseMetaData metaData = conn.getMetaData();
			int driverMaxConnections = metaData.getMaxConnections();
			if (driverMaxConnections > 0 && maxConnections > driverMaxConnections) {
				maxConnections = driverMaxConnections;
			}
		}
		return conn;

	}


	public static synchronized Connection getConnection() throws SQLException {
		if (connections == null) {
			return null; 
		}

		Connection conn = getFreeConnection();
		while (conn == null) {
			wait(250);
			conn = getFreeConnection(); 
		}
		return conn;
	}


	private static Connection getFreeConnection() throws SQLException {
		Connection conn = findFreeConnection();
		if (conn == null) {
			createConnections(incrementalConnections);
			conn = findFreeConnection();
			if (conn == null) {
				return null;
			}
		}
		return conn;

	}

	private static Connection findFreeConnection() throws SQLException {

		Connection conn = null;
		PooledConnection pConn = null;
		Enumeration enumerate = connections.elements();
		while (enumerate.hasMoreElements()) {
			pConn = (PooledConnection) enumerate.nextElement();

			if (!pConn.isBusy()) {
				conn = pConn.getConnection();
				pConn.setBusy(true);

				if (!testConnection(conn)) {
					try {
						conn = newConnection();
					} catch (SQLException e) {
						logger.debug("create connection failed: " + e.getMessage());
						return null;
					}
					pConn.setConnection(conn);
				}
				break;
			}
		}
		return conn;
	}

	private static boolean testConnection(Connection conn) {
		if(conn == null){
			return false;
		}
		
		try {
			if (testTable.equals("")) {
				conn.setAutoCommit(false);
			} else {
				//check if this connection is valid
				Statement stmt = conn.createStatement();
				stmt.execute("select count(*) from " + testTable);
			}

		} catch (SQLException e) {
			closeConnection(conn);
			return false;

		}
		return true;

	}

	public static void returnConnection(Connection conn) {

		if (connections == null) {
			logger.debug("connection pool is null");
			return;
		}

		PooledConnection pConn = null;
		Enumeration enumerate = connections.elements();

		while (enumerate.hasMoreElements()) {

			pConn = (PooledConnection) enumerate.nextElement();
			if (conn == pConn.getConnection()) {
				pConn.setBusy(false);
				break;
			}
		}
	}

	public static synchronized void refreshConnections() throws SQLException {

		if (connections == null) {
			logger.debug("connection pool is null");
			return;
		}

		PooledConnection pConn = null;
		Enumeration enumerate = connections.elements();

		while (enumerate.hasMoreElements()) {
			pConn = (PooledConnection) enumerate.nextElement();
			if (pConn.isBusy()) {
				wait(5000); 
			}

			closeConnection(pConn.getConnection());
			pConn.setConnection(newConnection());
			pConn.setBusy(false);
		}

	}

	public static synchronized void closeConnectionPool(){
		if (connections == null) {
			logger.debug("connection pool is null");
			return;
		}

		PooledConnection pConn = null;
		Enumeration enumerate = connections.elements();

		try{
			while (enumerate.hasMoreElements()) {
				pConn = (PooledConnection) enumerate.nextElement();
				if (pConn.isBusy()) {
					wait(5000); 
				}
				closeConnection(pConn.getConnection());
				connections.removeElement(pConn);

			}
		}catch(Exception e){
			logger.debug("closeConnectionPool failed: " + e.toString());
		}

		connections = null;
	}

	private static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			logger.debug("close connection failed: " + e.getMessage());
		}
	}

	private static void wait(int mSeconds) {
		try {
			Thread.sleep(mSeconds);
		} catch (InterruptedException e) {
		}
	}

}