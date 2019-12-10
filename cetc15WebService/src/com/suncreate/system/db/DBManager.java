package com.suncreate.system.db;

/*
数据库连接池管理类，包括初始化，获取连接，关闭连接，该类采用单模式，唯一实例
File:	DBManager.java
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;


/**
* 数据库连接池管理类，包括初始化，获取连接，关闭连接，该类采用单模式，唯一实例
* @author zhanzhao
*
*/
public class DBManager{
	private static DBManager dbInstanse=null;
	private static Logger logger = Logger.getLogger(DBManager.class);
	/**
	 * 
	 *
	 */
	private DBManager(){
		try{
		    JAXPConfigurator.configure(DBManager.class.getClassLoader().getResource("").getPath()+"dbconfig.xml",false);
		}catch(ProxoolException e){
			logger.error("连接池配置失败,"+e.getMessage());
		}
	}
	/**
	 * 获取该类实例
	 * @return
	 */
	public synchronized static DBManager getInstanse(){
		if (dbInstanse == null){
			dbInstanse = new DBManager();
		}
		return dbInstanse;
	}
	/**
	 * 取得数据库连接
	 * @param url
	 * @return con
	 * @throws SQLException
	 */
	public Connection getConnection(String url) throws SQLException{
		if (url == null || "".equals(url)){
			throw new SQLException("url is error!");
		}			
		Connection con = DriverManager.getConnection(url);
		return con;
	}
	/**
	 * 关闭连接
	 * @param con
	 */
	public static void close(Connection con){
		try{
			if (con == null || con.isClosed()){
				return;
			}
			con.close();
		}catch(SQLException e){
			logger.error("关闭连接异常,"+e.getMessage());
		}		
	}
}
