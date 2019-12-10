package com.suncreate.webService.service.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.suncreate.system.common.Configuration;
import com.suncreate.system.common.MD5Util;
import com.suncreate.system.common.StringUtil;
import com.suncreate.system.db.DBManager;
import com.suncreate.system.xmlutil.ReadXML;
import com.suncreate.system.xmlutil.WriteOutXML;
import com.suncreate.webService.model.CheckAuthentication;
import com.suncreate.webService.model.TaskCount;
import com.suncreate.webService.model.WfModel;
import com.suncreate.webService.service.IwsGetMyTask;

public class wsGetMyTask extends HttpServlet implements IwsGetMyTask {
	public static Logger logger = Logger.getLogger(wsGetMyTask.class);
	
	
	
	
	/**
	 * OA获取登录人的待办任务
	 */
	public String getMyTaskCount(String xmldata) {
		PropertyConfigurator.configure(wsPrintFile.class.getClassLoader().getResource("").getPath()+"log.property");
    	xmldata = xmldata.replace("&", "&amp;");
    	
    	
    	
    	DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstuserselect = null;
		PreparedStatement pmstselect = null;
		PreparedStatement pmstkeyIdSql = null;
        int result = 1;
        String desc="";
		ResultSet rsuserSel = null;
		ResultSet rsSel = null;
		ResultSet rskeyIdSql = null;
    	ReadXML readXml = new ReadXML();
    	String key="";
    	int taskCount = 0;
    	try{
    		logger.info("我的待办任务接口入参："+xmldata);
    		
    		conn = db.getConnection("proxool.test");
            List<CheckAuthentication> checkAuthentication = (List<CheckAuthentication>) readXml.getParamList(xmldata, "root/data", "com.suncreate.webService.model.CheckAuthentication");
            if(checkAuthentication!=null&&checkAuthentication.size()>0){
            	CheckAuthentication check = checkAuthentication.get(0);
            	//首先验证身份
            	String checkcode = check.getCheckcode();
            	String checktime = check.getChecktime();
            	String userid = check.getUserid();
            	String selectusersql = " select  t.department_number,t.user_id,t.user_name,t.userid from secu_user t where t.userid = ?";
            	
            	String selectsql = " select count(*) " +
            			" from wf_workflowdefs f, wf_task_workitem i, wf_task_info t, wf_wfentry a left join secu_user c on a.create_user_id = c.user_id " +
            			" where a.id = i.wf_id and a.name = f.wf_name and i.task_id = t.task_id and i.sign_user_id = ? " +
            			"and t.task_status = '2' and i.finish_status = '0' " +
            			" order by t.create_date desc ";
            	
            	logger.info("selectsql："+selectsql);
                Configuration config = new Configuration(wsPrintFile.class.getClassLoader().getResource("").getPath()+"constant.properties");
				
				key =  config.getValue("key");
			
				if(StringUtil.isNullOrBlank(userid)||StringUtil.isNullOrBlank(checktime)||StringUtil.isNullOrBlank(checkcode)){
					result = 0;
				}else{
					String checkcodekey = MD5Util.MD5(userid+checktime+key);//加密
					if(checkcode.equals(checkcodekey)){//验证身份通过 
						//查询我的待办任务
						pmstuserselect= conn.prepareStatement(selectusersql);
						pmstuserselect.setString(1,userid);
						rsuserSel = pmstuserselect.executeQuery();
						if(rsuserSel.next()){
							pmstselect= conn.prepareStatement(selectsql);
							pmstselect.setString(1,rsuserSel.getString(2));
							rsSel = pmstselect.executeQuery();
							
							if(rsSel.next()){
								taskCount = rsSel.getInt(1);
							}
							result = 1;
						}
						
						
					}else{
						result = 0;
						desc = "身份认证失败";
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
			if (rsuserSel != null) {
				try {
					rsuserSel.close();
					rsuserSel = null;
				} catch (SQLException e) {
				}
			}
			if (rsSel != null) {
				try {
					rsSel.close();
					rsSel = null;
				} catch (SQLException e) {
				}
			}
			if (rskeyIdSql != null) {
				try {
					rskeyIdSql.close();
					rskeyIdSql = null;
				} catch (SQLException e) {
				}
			}
			if (pmstuserselect != null) {
				try {
					pmstuserselect.close();
					pmstuserselect = null;
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
			
			if (pmstkeyIdSql != null) {
				try {
					pmstkeyIdSql.close();
					pmstkeyIdSql = null;
				} catch (SQLException e) {
				}
			}
		
			logger.info("我的待办接口结束");
			DBManager.close(conn);

		}
    	
	    WriteOutXML wox = new WriteOutXML();
	    String outxmldata="";
		try {
			outxmldata = wox.ExecElementToXml(result,desc,taskCount+"");
        	logger.info("我的待办接口出参："+outxmldata);

		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     return  outxmldata;
	}

}
