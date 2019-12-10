package com.suncreate.webService.service.imp;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServlet;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.suncreate.system.common.Configuration;
import com.suncreate.system.common.MD5Util;
import com.suncreate.system.common.StringUtil;
import com.suncreate.system.db.DBManager;
import com.suncreate.system.xmlutil.ReadXML;
import com.suncreate.system.xmlutil.WriteOutXML;

import com.suncreate.webService.model.UserInfo;

import com.suncreate.webService.service.IwsOaLogin;



public class wsOaLogin  extends HttpServlet implements IwsOaLogin {
	
	public static Logger logger = Logger.getLogger(wsOaLogin.class);
	

	/**
	 * 登录接口
	 */
	public String createKeyForOaLogin(String xmldata) {
		
		
    	PropertyConfigurator.configure(wsOaLogin.class.getClassLoader().getResource("").getPath()+"log.property");
    	xmldata = xmldata.replace("&", "&amp;");
    	String selectsql = " select  userid from secu_user t where t.userid = ?";
        String updatesql = " update secu_user t set TOKENID = ?, OVERTIME = sysdate+0.5/24 where t.userid = ? " ;
    	DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstselect = null;
		PreparedStatement pmstupdate = null;
        int result = 1;
        String desc="";
        String baomiloginurl ="";
		ResultSet rsSel = null;
    	ReadXML readXml = new ReadXML();
    	String key="";

        try{
        	logger.info("OA登录验证接口入参："+xmldata);
            List<UserInfo> userinfolist = (List<UserInfo>) readXml.getParamList(xmldata, "root/data", "com.suncreate.webService.model.UserInfo");
			conn = db.getConnection("proxool.test");
			if(userinfolist!=null&&userinfolist.size()>0){
				UserInfo userinfo = userinfolist.get(0);
				//首先验证身份
				String userid = userinfo.getUserid();
				String checktime = userinfo.getChecktime();
				String checkcode = userinfo.getCheckcode();
				Configuration config = new Configuration(wsOaLogin.class.getClassLoader().getResource("").getPath()+"constant.properties");
				
				key =  config.getValue("key"); 
				if(StringUtil.isNullOrBlank(userid)||StringUtil.isNullOrBlank(checktime)||StringUtil.isNullOrBlank(checkcode)){
					result = 0;
					desc="输入参数为空!";
				}else{
					String checkcodekey = MD5Util.MD5(userid+checktime+key);//加密
					if(checkcode.equals(checkcodekey)){//验证身份通过 
						
					    // 查询出保密系统中是否存在该用户
						pmstselect= conn.prepareStatement(selectsql);
					    pmstselect.setString(1,userinfo.getUserid());
					    rsSel = pmstselect.executeQuery();
						if(rsSel.next()){
							String uuid = UUID.randomUUID().toString();
							pmstupdate= conn.prepareStatement(updatesql);
							pmstupdate.setString(1, uuid);
							pmstupdate.setString(2, userinfo.getUserid());
							result = pmstupdate.executeUpdate();
							if(result>0){
								baomiloginurl = config.getValue("baomiloginurl"); 
								baomiloginurl = baomiloginurl+"?func=loginForOA&userid="+userid+"&OATokenId="+uuid;
							}
						}else{
							result = 0;
							desc="用户信息不存在!";
						}
					}else{
						result = 0;
						desc="身份验证失败!";
					}

				}
		
			
			}
			
		}catch(SQLException e){
			result = 0;
			desc = "sql执行异常";
			logger.error("e==" + e);
		}catch(Exception e){
			result = 0;
			desc = "调用接口异常";
			logger.error("e==" + e);
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
			if (pmstupdate != null) {
				try {
					pmstupdate.close();
					pmstupdate = null;
				} catch (SQLException e) {
				}
			}
		
			logger.info("OA登录结束接口调用结束");
			DBManager.close(conn);

		}
		
		WriteOutXML wox = new WriteOutXML();
	    String outxmldata="";
		try {
			outxmldata = wox.ExecElementToXml(result,desc,baomiloginurl);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	logger.info("OA登录验证接口出参："+outxmldata);

	     return  outxmldata;
	   
	  
	 }
	
	
	
	
	
	

}