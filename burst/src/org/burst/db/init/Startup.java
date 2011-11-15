package org.burst.db.init;

import org.apache.log4j.Logger;
import org.burst.db.config.DbConfig;
import org.burst.db.config.LogUtil;
import org.burst.db.conn.ConnectionPool;


public class Startup {
	private static final Logger logger = Logger.getLogger(Startup.class);
	
	public static void init() throws Exception{
		LogUtil.init(false);
		logger.debug("......... system start up ........");
		logger.debug("......... load log4j.properties finish ........");
		DbConfig.init(false);
		logger.debug("......... load db.properties finish ........");
		ConnectionPool.init(false);
		logger.debug("......... create database connection pool finish ........");
	}
}
