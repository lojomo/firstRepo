package com.suncreate.webService.service.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suncreate.system.db.DBManager;
import com.suncreate.system.xmlutil.WriteOutXML;
import com.suncreate.webService.service.IwsUserInfo;
import com.suncreate.webService.model.UserInfoESB;

public class wsUserInfo implements IwsUserInfo {
	

	public String getuserInfoStr(UserInfoESB userInfoESB){
		List<UserInfoESB> list = new ArrayList<UserInfoESB>();
		UserInfoESB user = new UserInfoESB();
		String selectsql = " select  user_Id,user_name,department_name,userid from secu_user t where t.user_id = ?";
		DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstselect = null;
		ResultSet rsSel = null;
		try {
			  // 查询出保密系统对应的id
			pmstselect= conn.prepareStatement(selectsql);
			pmstselect.setString(1,userInfoESB.getUser_id());
			rsSel = pmstselect.executeQuery();
			if(rsSel.next()){
				user.setUser_id(rsSel.getString(1));
				user.setUser_name(rsSel.getString(2));
				user.setDept_id("11111");
				user.setDept_name(rsSel.getString(3));
				user.setUserid(rsSel.getString(4));
				list.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (rsSel != null) {
				try {
					rsSel.close();
					rsSel = null;
				} catch (SQLException e) {
				}
			}
			if (pmstselect != null) {
				try {
					pmstselect.close();
					pmstselect = null;
				} catch (SQLException e) {
				}
			}
		
			DBManager.close(conn);

		}
		
		WriteOutXML wox = new WriteOutXML();
	    String outxmldata="";
	    try {
			outxmldata = wox.ExecElementToXml("com.suncreate.webService.model.UserInfoESB",list,0 );
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return outxmldata;
	} 
	
	public String getuserInfo(UserInfoESB userInfoESB){
		UserInfoESB user = new UserInfoESB();
		String selectsql = " select  user_Id,user_name,department_name,userid from secu_user t where t.user_id = ?";
		DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstselect = null;
		ResultSet rsSel = null;
		try {
			  // 查询出保密系统对应的id
			pmstselect= conn.prepareStatement(selectsql);
			pmstselect.setString(1,userInfoESB.getUser_id());
			rsSel = pmstselect.executeQuery();
			if(rsSel.next()){
				user.setUser_id(rsSel.getString(1));
				user.setUser_name(rsSel.getString(2));
				user.setDept_id("11111");
				user.setDept_name(rsSel.getString(3));
				user.setUserid(rsSel.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (rsSel != null) {
				try {
					rsSel.close();
					rsSel = null;
				} catch (SQLException e) {
				}
			}
			if (pmstselect != null) {
				try {
					pmstselect.close();
					pmstselect = null;
				} catch (SQLException e) {
				}
			}
		
			DBManager.close(conn);

		}
		
		WriteOutXML wox = new WriteOutXML();
	    String outxmldata="";
		try {
			outxmldata = wox.ExecObjectToXml(1,"");
			System.out.println("文件打印/刻录接口出参："+outxmldata);

		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outxmldata;
	} 
	public List<UserInfoESB> userNameList(String user_id) {
		List<UserInfoESB> list = new ArrayList<UserInfoESB>();
		String selectsql = " select  user_Id,user_name,department_name,userid from secu_user t where t.user_id = ?";
		DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstselect = null;
		ResultSet rsSel = null;
		try {
			  // 查询出保密系统对应的id
			pmstselect= conn.prepareStatement(selectsql);
			pmstselect.setString(1,user_id);
			rsSel = pmstselect.executeQuery();
			if(rsSel.next()){
				UserInfoESB user = new UserInfoESB();
				user.setUser_id(user_id);
				user.setUser_name(rsSel.getString(2));
				user.setDept_name(rsSel.getString(3));
				user.setUserid(rsSel.getString(4));
				list.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (rsSel != null) {
				try {
					rsSel.close();
					rsSel = null;
				} catch (SQLException e) {
				}
			}
			if (pmstselect != null) {
				try {
					pmstselect.close();
					pmstselect = null;
				} catch (SQLException e) {
				}
			}
		
			DBManager.close(conn);

		}
		return list;
	}

	public UserInfoESB userinfo(String user_id) {
		UserInfoESB user = new UserInfoESB();
		String selectsql = " select  user_Id,user_name,department_name,userid from secu_user t where t.user_id = ?";
		DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstselect = null;
		ResultSet rsSel = null;
		try {
			  // 查询出保密系统对应的id
			pmstselect= conn.prepareStatement(selectsql);
			pmstselect.setString(1,user_id);
			rsSel = pmstselect.executeQuery();
			if(rsSel.next()){
				user.setUser_id(user_id);
				user.setUser_name(rsSel.getString(2));
				user.setDept_name(rsSel.getString(3));
				user.setUserid(rsSel.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (rsSel != null) {
				try {
					rsSel.close();
					rsSel = null;
				} catch (SQLException e) {
				}
			}
			if (pmstselect != null) {
				try {
					pmstselect.close();
					pmstselect = null;
				} catch (SQLException e) {
				}
			}
		
			DBManager.close(conn);

		}
		return user;
	}
	
	

}
