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
import org.bsf.tools.guid.GUID;

import com.suncreate.system.common.Configuration;
import com.suncreate.system.common.MD5Util;
import com.suncreate.system.common.StringUtil;
import com.suncreate.system.db.DBManager;
import com.suncreate.system.xmlutil.ReadXML;
import com.suncreate.system.xmlutil.WriteOutXML;

import com.suncreate.webService.model.CheckAuthentication;
import com.suncreate.webService.model.FileCopy;
import com.suncreate.webService.model.FilePrint;

import com.suncreate.webService.service.IwsPrintFile;



public class wsPrintFile  extends HttpServlet implements IwsPrintFile {
	
	public static Logger logger = Logger.getLogger(wsPrintFile.class);
	

	/**
	 * 文件申请打印/刻录接口
	 */
	public String SubmitTaskResult(String xmldata) {
    	PropertyConfigurator.configure(wsPrintFile.class.getClassLoader().getResource("").getPath()+"log.property");
    	xmldata = xmldata.replace("&", "&amp;");
    	String selectsql = " select  t.department_number,t.user_id,t.user_name,t.userid,t.department_name,t.grade from secu_user t where t.userid = ?";
    	
    	String insertsql = "insert into YW_WJZZ_SUB2(ID,LSC_TASKID,WJ_ZZDH,WJ_NAME,WJ_GRADE,WJ_COUNT,SC_TIME,SQR_ID,SQR_NAME,SQR_GRADE," +
    			"SQR_DEPARTMENT_NUMBER,SQR_DEPARTMENT_NAME,WJ_STATUS,WJ_XS) " +
        		" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?) " ;
    	
    	DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstselect = null;
		PreparedStatement pmstinsert = null;
        int result = 1;
        String desc="同步成功";
		ResultSet rsSel = null;
    	ReadXML readXml = new ReadXML();
    	String key="";

        try{
        	logger.info("文件申请打印接口入参："+xmldata);

        	conn = db.getConnection("proxool.test");
            Configuration config = new Configuration(wsPrintFile.class.getClassLoader().getResource("").getPath()+"constant.properties");
            String FunctionName =  readXml.getParamValue(xmldata, "FunctionName", "Lanxum/FunctionName");
            String TaskType =  readXml.getParamValue(xmldata, "TaskType", "Lanxum/TaskType");//1 打印 3 三代复印 4 刻录 32 扫描打印，
            String WJ_XS = "";//WJ_XS 文件形式
            if("1".equals(TaskType)){
            	WJ_XS = "纸介质";
            }else if("4".equals(TaskType)){
            	WJ_XS = "光介质";
            }else{
            	WJ_XS = "未定义";
            }
            String Result =  readXml.getParamValue(xmldata, "Result", "Lanxum/Result");//true or false 输出成功或者失败
            
            String TaskId =  readXml.getParamValue(xmldata, "TaskId", "Lanxum/FileInfos/FileInfo/Task/TaskId");
            //登录账号
            String UserId =  readXml.getParamValue(xmldata, "UserId", "Lanxum/FileInfos/FileInfo/Task/UserId");
            
            String UserName =  readXml.getParamValue(xmldata, "UserName", "Lanxum/FileInfos/FileInfo/Task/UserName");
            //WJ_NAME 文件名
            String FileName =  readXml.getParamValue(xmldata, "FileName", "Lanxum/FileInfos/FileInfo/Task/FileName");
            String Duplex =  readXml.getParamValue(xmldata, "Duplex", "Lanxum/FileInfos/FileInfo/Task/Duplex");
            //WJ_COUNT 文件页数/光盘内容
            String Copies =  readXml.getParamValue(xmldata, "Copies", "Lanxum/FileInfos/FileInfo/Task/Copies");
            
            String Pages =  readXml.getParamValue(xmldata, "Pages", "Lanxum/FileInfos/FileInfo/Task/Pages");
            //主条码
            String MainBarcode =  readXml.getParamValue(xmldata, "MainBarcode", "Lanxum/FileInfos/FileInfo/Task/MainBarcode");
            
            String Color =  readXml.getParamValue(xmldata, "Color", "Lanxum/FileInfos/FileInfo/Task/Color");
            //WJ_GRADE 载体密级
            String SafeLevel =  readXml.getParamValue(xmldata, "SafeLevel", "Lanxum/FileInfos/FileInfo/Task/SafeLevel");
            //SC_TIME 生成日期（格式:YYYY_MM_DD HH:MM:SS）
            String PrintTime =  readXml.getParamValue(xmldata, "PrintTime", "Lanxum/FileInfos/FileInfo/Task/PrintTime");
            
            //插入载体台账
			 List<FileCopy> fileprintlist = (List<FileCopy>) readXml.getParamList(xmldata, "Lanxum/FileInfos/FileInfo/Task/Copys/Copy", "com.suncreate.webService.model.FileCopy");
			 if(fileprintlist!=null&&fileprintlist.size()>0){
				 for (FileCopy fileprint : fileprintlist) {
					// 查询出保密系统对应的用户信息
					pmstselect= conn.prepareStatement(selectsql);
				    pmstselect.setString(1,UserId);
				    rsSel = pmstselect.executeQuery();
				    if(rsSel.next()){
				    	pmstinsert= conn.prepareStatement(insertsql);
				    	
				    	String uuid = new GUID().toString();//UUID.randomUUID().toString();
				    	pmstinsert.setString(1, uuid);
				    	pmstinsert.setString(2, TaskId);
				    	pmstinsert.setString(3, fileprint.getFileBarcodeText());
				    	pmstinsert.setString(4, FileName);
				    	pmstinsert.setString(5, SafeLevel);
				    	pmstinsert.setString(6, Copies);
				    	pmstinsert.setString(7, PrintTime);
				    	pmstinsert.setString(8, rsSel.getString(2));
				    	pmstinsert.setString(9, UserName);
				    	pmstinsert.setString(10, rsSel.getString(6));
				    	pmstinsert.setString(11, rsSel.getString(1));
				    	pmstinsert.setString(12, rsSel.getString(5));
				    	pmstinsert.setString(13, "在用");
				    	pmstinsert.setString(14, WJ_XS);
				    	result = pmstinsert.executeUpdate();
				    	
				    }else{
						result = 0;
						desc="用户信息不存在!";
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
			if (pmstinsert != null) {
				try {
					pmstinsert.close();
					pmstinsert = null;
				} catch (SQLException e) {
				}
			}
		
			logger.info("文件打印/刻录接口结束");
			DBManager.close(conn);

		}
		
		WriteOutXML wox = new WriteOutXML();
	    String outxmldata="";
		try {
			outxmldata = wox.ExecElementToXml(result,desc);
        	logger.info("文件打印/刻录接口出参："+outxmldata);

		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     return  outxmldata;
	   
	  
	 }
	
	
	public String SubmitTaskResult2(String xmldata) {
    	PropertyConfigurator.configure(wsPrintFile.class.getClassLoader().getResource("").getPath()+"log.property");
    	xmldata = xmldata.replace("&", "&amp;");
    	String selectsql = " select  t.department_number,t.user_id,t.user_name,t.userid from secu_user t where t.userid = ?";
    	
    	String insertsql = "insert into YW_WJZZ_SUB2(ID,WJ_BH,WJ_ZZDH,WJ_NAME,WJ_GRADE,WJ_COUNT,SC_TIME,SQR_ID,SQR_NAME,SQR_GRADE," +
    			"SQR_DEPARTMENT_NUMBER,SQR_DEPARTMENT_NAME,WJ_STATUS,WJ_XS) " +
        		" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?) " ;
    	
    	DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstselect = null;
		PreparedStatement pmstinsert = null;
        int result = 1;
        String desc="";
		ResultSet rsSel = null;
    	ReadXML readXml = new ReadXML();
    	String key="";

        try{
        	logger.info("文件申请打印接口入参："+xmldata);

        	conn = db.getConnection("proxool.test");
            List<CheckAuthentication> checkAuthentication = (List<CheckAuthentication>) readXml.getParamList(xmldata, "root/check", "com.suncreate.webService.model.CheckAuthentication");
            if(checkAuthentication!=null&&checkAuthentication.size()>0){
            	CheckAuthentication check = checkAuthentication.get(0);
            	//首先验证身份
            	String checkcode = check.getCheckcode();
            	String checktime = check.getChecktime();
                Configuration config = new Configuration(wsPrintFile.class.getClassLoader().getResource("").getPath()+"constant.properties");
				
				key =  config.getValue("key");
				if(StringUtil.isNullOrBlank(checktime)||StringUtil.isNullOrBlank(checkcode)){
					result = 0;
				}else{
					String checkcodekey = MD5Util.MD5(checktime+key);//加密
					if(checkcode.equals(checkcodekey)){//验证身份通过 
						//插入载体台账
						 List<FilePrint> fileprintlist = (List<FilePrint>) readXml.getParamList(xmldata, "root/data", "com.suncreate.webService.model.FilePrint");
						 if(fileprintlist!=null&&fileprintlist.size()>0){
							 for (FilePrint fileprint : fileprintlist) {
								// 查询出保密系统对应的用户信息
								pmstselect= conn.prepareStatement(selectsql);
							    pmstselect.setString(1,fileprint.getUserid());
							    rsSel = pmstselect.executeQuery();
							    if(rsSel.next()){
							    	pmstinsert= conn.prepareStatement(insertsql);
							    	
							    	String uuid = new GUID().toString();//UUID.randomUUID().toString();
							    	pmstinsert.setString(1, uuid);
							    	pmstinsert.setString(2, fileprint.getFilebarcode());
							    	pmstinsert.setString(3, fileprint.getFilecode());
							    	pmstinsert.setString(4, fileprint.getFilename());
							    	pmstinsert.setString(5, fileprint.getFilegrade());
							    	pmstinsert.setString(6, fileprint.getPagecount());
							    	pmstinsert.setString(7, fileprint.getCreatedate());
							    	pmstinsert.setString(8, rsSel.getString(2));
							    	pmstinsert.setString(9, fileprint.getUsername());
							    	pmstinsert.setString(10, fileprint.getUsergrade());
							    	pmstinsert.setString(11, rsSel.getString(1));
							    	pmstinsert.setString(12, fileprint.getDeptname());
							    	pmstinsert.setString(13, "在用");
							    	pmstinsert.setString(14, fileprint.getFiletype());
							    	result = pmstinsert.executeUpdate();
							    	
							    }else{
									result = 0;
									desc="用户信息不存在!";
								}
							}
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
			if (pmstinsert != null) {
				try {
					pmstinsert.close();
					pmstinsert = null;
				} catch (SQLException e) {
				}
			}
		
			logger.info("文件打印/刻录接口结束");
			DBManager.close(conn);

		}
		
		WriteOutXML wox = new WriteOutXML();
	    String outxmldata="";
		try {
			outxmldata = wox.ExecElementToXml(result,desc);
        	logger.info("文件打印/刻录接口出参："+outxmldata);

		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     return  outxmldata;
	   
	  
	 }
	
	/**
	 * 文件申请打印接口
	 */
	public String sendRequestPrintFile_old(String xmldata) {
    	PropertyConfigurator.configure(wsPrintFile.class.getClassLoader().getResource("").getPath()+"log.property");
    	xmldata = xmldata.replace("&", "&amp;");
    	String deletesql = " delete from JK_FILE_PRINT where JOBNUMBER = ? ";
    	String selectsql = " select  department_number,userid from secu_user t where t.userid = ?";
        String insertsql = "insert into JK_FILE_PRINT(JOBNUMBER,DEPTID,USERID,REQUESTTIME,FILENAME,FILEPATH,PRINTDIRECTION,PRINTWAY,PRINTCOLOR,PAPERTYPE,PAGECOUNT,COPIES,FILEPURPOSE,FILEDESTINATION,PLAINCODE,HIDDENCODE,DEPTNAME,USERNAME,TOKENID,OVERTIME) " +
        		" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate+0.5/24) " ;
    	DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstdelete = null;
		PreparedStatement pmstselect = null;
		PreparedStatement pmstinsert = null;
         int result = 1;
         String desc="";
         String sendrequestprintfileurl ="";
		ResultSet rsSel = null;
    	ReadXML readXml = new ReadXML();
    	String key="";

        try{
        	logger.info("文件申请打印接口入参："+xmldata);

            List<FilePrint> fileprintlist = (List<FilePrint>) readXml.getParamList(xmldata, "root/data", "com.suncreate.webService.model.FilePrint");
			conn = db.getConnection("proxool.test");
			if(fileprintlist!=null&&fileprintlist.size()>0){
				FilePrint fileprint = fileprintlist.get(0);
				//首先验证身份
				String userid = fileprint.getUserid();
				String checktime = fileprint.getChecktime();
				String checkcode = fileprint.getCheckcode();
				Configuration config = new Configuration(wsPrintFile.class.getClassLoader().getResource("").getPath()+"constant.properties");
				
				key =  config.getValue("key"); 
				if(StringUtil.isNullOrBlank(userid)||StringUtil.isNullOrBlank(checktime)||StringUtil.isNullOrBlank(checkcode)){
					result = 0;
				}else{
					String checkcodekey = MD5Util.MD5(userid+checktime+key);//加密
					if(checkcode.equals(checkcodekey)){//验证身份通过 
						//先删除
						pmstdelete= conn.prepareStatement(deletesql);
						pmstdelete.setString(1, fileprint.getJobnumber());
					    pmstdelete.executeUpdate();
					    // 查询出保密系统对应的id
						pmstselect= conn.prepareStatement(selectsql);
					    pmstselect.setString(1,fileprint.getUserid());
					    rsSel = pmstselect.executeQuery();
						if(rsSel.next()){
							
							pmstinsert= conn.prepareStatement(insertsql);
							pmstinsert.setString(1, fileprint.getJobnumber());
							pmstinsert.setString(2, rsSel.getString(1));
							pmstinsert.setString(3, fileprint.getUserid());
							pmstinsert.setString(4, fileprint.getRequesttime());
							pmstinsert.setString(5, fileprint.getFilename());
							pmstinsert.setString(6, fileprint.getFilepath());
							pmstinsert.setString(7, fileprint.getPrintdirection());
							pmstinsert.setString(8, fileprint.getPrintway());
							pmstinsert.setString(9, fileprint.getPrintcolor());
							pmstinsert.setString(10, fileprint.getPapertype());
							pmstinsert.setString(11, fileprint.getPagecount());
							pmstinsert.setString(12, fileprint.getCopies());
							pmstinsert.setString(13, fileprint.getFilepurpose());
							pmstinsert.setString(14, fileprint.getFiledestination());
							pmstinsert.setString(15, fileprint.getPlaincode());
							pmstinsert.setString(16, fileprint.getHiddencode());
							pmstinsert.setString(17, fileprint.getDeptname());
							pmstinsert.setString(18, fileprint.getUsername());
							String uuid = UUID.randomUUID().toString();
							pmstinsert.setString(19,uuid);
							result = pmstinsert.executeUpdate();
							if(result>0){
								sendrequestprintfileurl = config.getValue("sendrequestprintfileurl"); 
								sendrequestprintfileurl = sendrequestprintfileurl+"?jobnumber="+fileprint.getJobnumber()+"&hdPrintTokenId="+uuid;
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
			if (pmstdelete != null) {
				try {
					pmstdelete.close();
					pmstdelete = null;
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
			if (pmstinsert != null) {
				try {
					pmstinsert.close();
					pmstinsert = null;
				} catch (SQLException e) {
				}
			}
		
			logger.info("文件申请打印接口结束");
			DBManager.close(conn);

		}
		
		WriteOutXML wox = new WriteOutXML();
	    String outxmldata="";
		try {
			outxmldata = wox.ExecElementToXml(result,desc,sendrequestprintfileurl);
        	logger.info("文件申请打印接口出参："+outxmldata);

		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     return  outxmldata;
	   
	  
	 }
	
	
	
	
	
	

}