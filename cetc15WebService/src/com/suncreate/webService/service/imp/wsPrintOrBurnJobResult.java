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

import com.suncreate.webService.model.Barcode;
import com.suncreate.webService.model.FilePrint;
import com.suncreate.webService.model.PrintOrBurnJobResult;

import com.suncreate.webService.service.IwsPrintFile;
import com.suncreate.webService.service.IwsPrintOrBurnJobResult;



public class wsPrintOrBurnJobResult  extends HttpServlet implements IwsPrintOrBurnJobResult {
	
	public static Logger logger = Logger.getLogger(wsPrintOrBurnJobResult.class);
	

	/**
	 * 打印刻录作业结果通知接口
	 */
	public String sendRequestPrintOrBurnJobResult (String xmldata) {
    	PropertyConfigurator.configure(wsPrintOrBurnJobResult.class.getClassLoader().getResource("").getPath()+"log.property");
    	xmldata = xmldata.replace("&", "&amp;");
    	//涉密文件打印处理sql
    	String selectsecprintfilesql = "select id from yw_sec_file_print t where t.task_code = ? ";
    	String deletesecprinfilesubsql = "delete from yw_sec_file_print_sub t where t.wf_id = ? ";
    	String insertsecprinfilesubsql = "insert into yw_sec_file_print_sub(id,wf_id,carrier_sno,carrier_bar_code,is_success) values(?,?,?,?,?)";
    	
    	//非密文件打印处理sql
    	String selectnosecprintfilesql = "select id from yw_non_sec_file_print t where t.task_code = ? ";
    	String deletenosecprinfilesubsql = "delete from yw_non_sec_file_print_sub t where t.wf_id = ? ";
    	String insertnosecprinfilesubsql = "insert into yw_non_sec_file_print_sub(id,wf_id,carrier_sno,carrier_bar_code,is_success) values(?,?,?,?,?)";
    	
    	// 涉密文件刻录处理sql 
    	String updatesecburnsql = "update yw_goods_facet t set t.info_treat = ?,set  t.carrier_sno=? ,set t.carrier_bar_code=?  where wf_id = ? ";
    	//非密文件刻录处理sql
    	String updatenosecburnsql = "update yw_goods_export t set t.cd_record = ,set  t.carrier_sno=? ,set t.carrier_bar_code=?   where wf_id = ? ";
    
    	DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstselectsecprintfile = null;
		PreparedStatement pmstdeletesecprinfilesub = null;
		PreparedStatement pmstinsertsecprinfilesub = null;
		PreparedStatement pmstselectnosecprintfile = null;
		PreparedStatement pmstdeletenosecprinfilesub = null;
		PreparedStatement pmstinsertnosecprinfilesub= null;
		PreparedStatement pmstupdatesecburn= null;
		PreparedStatement pmstupdatenosecburn = null;
        int result = 1;
        String desc="";
		ResultSet rssecSel = null;
		ResultSet rsnosecSel = null;
    	ReadXML readXml = new ReadXML();

        try{
        	logger.info("打印刻录作业结果通知接口入参："+xmldata);
            List<PrintOrBurnJobResult> printburnjobresultlist = (List<PrintOrBurnJobResult>) readXml.getParamList(xmldata, "root/data", "com.suncreate.webService.model.PrintOrBurnJobResult");
            List<Barcode> barcodelist = (List<Barcode>) readXml.getParamList(xmldata, "root/data/barcode", "com.suncreate.webService.model.Barcode");
			conn = db.getConnection("proxool.test");
			if(printburnjobresultlist!=null&&printburnjobresultlist.size()>0){
				PrintOrBurnJobResult printburnjobresult = printburnjobresultlist.get(0);
				if("1".equals(printburnjobresult.getJobtype())){//打印
					if(StringUtil.isNotEmpty(printburnjobresult.getJobnumber())){//打印时作业编号非空
						   if("1".equals(printburnjobresult.getSecrettype())||"2".equals(printburnjobresult.getSecrettype())||
								   "3".equals(printburnjobresult.getSecrettype())||"4".equals(printburnjobresult.getSecrettype())){//涉密文件
							    pmstselectsecprintfile= conn.prepareStatement(selectsecprintfilesql);
								pmstselectsecprintfile.setString(1,printburnjobresult.getJobnumber());
								rssecSel = pmstselectsecprintfile.executeQuery();
								if(rssecSel.next()){
										//先删除
										pmstdeletesecprinfilesub= conn.prepareStatement(deletesecprinfilesubsql);
										pmstdeletesecprinfilesub.setString(1, rssecSel.getString(1));
										pmstdeletesecprinfilesub.executeUpdate();
										//再插入
										for(Barcode barcode:barcodelist){
											pmstinsertsecprinfilesub= conn.prepareStatement(insertsecprinfilesubsql);
											String uuid = UUID.randomUUID().toString();
											uuid = uuid.replace("-", "");
											pmstinsertsecprinfilesub.setString(1, uuid);
											pmstinsertsecprinfilesub.setString(2, rssecSel.getString(1));
											pmstinsertsecprinfilesub.setString(3, barcode.getPlaincode());
											pmstinsertsecprinfilesub.setString(4, barcode.getHiddencode());
											if("0".equals(barcode.getJobresult())){
												pmstinsertsecprinfilesub.setString(5, "否");
											}else if("1".equals(barcode.getJobresult())){
												pmstinsertsecprinfilesub.setString(5, "是");
											}else{
												pmstinsertsecprinfilesub.setString(5, "");
											}
											pmstinsertsecprinfilesub.executeUpdate();
										}
									
								}else{
									result = 0;	
									desc ="对应作业不存在!";
								} 
						   }else if("5".equals(printburnjobresult.getSecrettype())||"6".equals(printburnjobresult.getSecrettype())){
							    pmstselectnosecprintfile= conn.prepareStatement(selectnosecprintfilesql);
							    pmstselectnosecprintfile.setString(1,printburnjobresult.getJobnumber());
							    rsnosecSel = pmstselectnosecprintfile.executeQuery();
								if(rsnosecSel.next()){
										//先删除
										pmstdeletenosecprinfilesub= conn.prepareStatement(deletenosecprinfilesubsql);
										pmstdeletenosecprinfilesub.setString(1, rsnosecSel.getString(1));
										pmstdeletenosecprinfilesub.executeUpdate();
										//再插入
										for(Barcode barcode:barcodelist){
											pmstinsertnosecprinfilesub= conn.prepareStatement(insertnosecprinfilesubsql);
											String uuid = UUID.randomUUID().toString();
											uuid = uuid.replace("-", "");
											pmstinsertnosecprinfilesub.setString(1, uuid);
											pmstinsertnosecprinfilesub.setString(2, rsnosecSel.getString(1));
											pmstinsertnosecprinfilesub.setString(3, barcode.getPlaincode());
											pmstinsertnosecprinfilesub.setString(4, barcode.getHiddencode());
											if("0".equals(barcode.getJobresult())){
												pmstinsertnosecprinfilesub.setString(5, "否");
											}else if("1".equals(barcode.getJobresult())){
												pmstinsertnosecprinfilesub.setString(5, "是");
											}else{
												pmstinsertnosecprinfilesub.setString(5, "");
											}
											pmstinsertnosecprinfilesub.executeUpdate();
										}
						   }else{
								result = 0;	
								desc ="对应作业不存在!";
							} 
					}else{
						result = 0;	
						desc ="文件密级为空或者不正确!"; 
					}
			    }else{
			    	result = 0;
			    	desc ="打印作业编号为空!"; 
			    }
			 }else if("2".equals(printburnjobresult.getJobtype())){//刻录
				  if(StringUtil.isNotEmpty(printburnjobresult.getWfid())){//刻录时流程id不能为空
					  if(barcodelist!=null&&barcodelist.size()>0){
						   String barcoderesult ="";
						  Barcode barcode = barcodelist.get(0);
						  if("0".equals(barcode.getJobresult())){
							  barcoderesult = "刻录失败";
						  }else if("1".equals(barcode.getJobresult())){
							  barcoderesult = "刻录成功";
						  }
					  
					  if("1".equals(printburnjobresult.getSecrettype())||"2".equals(printburnjobresult.getSecrettype())||
							   "3".equals(printburnjobresult.getSecrettype())||"4".equals(printburnjobresult.getSecrettype())){//涉密文件
						       pmstupdatesecburn= conn.prepareStatement(updatesecburnsql);
						       pmstupdatesecburn.setString(1, barcoderesult);
							  pmstupdatesecburn.setString(2, barcode.getPlaincode());
							  pmstupdatesecburn.setString(3, barcode.getHiddencode());
							  pmstupdatesecburn.setString(4, printburnjobresult.getWfid());
							  pmstupdatesecburn.executeUpdate();  
						  }else if("5".equals(printburnjobresult.getSecrettype())||"6".equals(printburnjobresult.getSecrettype())){//非密文件
						  pmstupdatenosecburn= conn.prepareStatement(updatenosecburnsql);
						  pmstupdatenosecburn.setString(1, barcoderesult);
						  pmstupdatenosecburn.setString(2, barcode.getPlaincode());
						  pmstupdatenosecburn.setString(3,  barcode.getHiddencode());
						  pmstupdatenosecburn.setString(4, printburnjobresult.getWfid());
						  pmstupdatenosecburn.executeUpdate();
						  
					  }else{
						  result = 0;	
						  desc ="文件密级为空或者不正确!";  
					  }
					 }else{
						 result = 0;	
						 desc ="条形码为空!";  
					 } 
				  }else{
					  result = 0;
				      desc ="流程编号为空!";  
				  }
			 }else {
				 result = 0;	
				 desc ="作业类型为空或者不正确!"; 
			 }
		  }else{
			  result = 0;	
			  desc ="消息为空!";
		  }
		}catch(SQLException e){
			result = 0;
			desc = "调用接口失败";
			logger.error("e==" + e);
		}catch(Exception e){
			result = 0;
			desc = "调用接口失败";
			logger.error("e==" + e);
		}finally{
			if (rssecSel != null) {
				try {
					rssecSel.close();
					rssecSel = null;
				} catch (SQLException e) {
				}
			}
			if (rsnosecSel != null) {
				try {
					rsnosecSel.close();
					rsnosecSel = null;
				} catch (SQLException e) {
				}
			}
			if (pmstselectsecprintfile != null) {
				try {
					pmstselectsecprintfile.close();
					pmstselectsecprintfile = null;
				} catch (SQLException e) {
				}
			}
			if (pmstdeletesecprinfilesub != null) {
				try {
					pmstdeletesecprinfilesub.close();
					pmstdeletesecprinfilesub = null;
				} catch (SQLException e) {
				}
			}
			if (pmstinsertsecprinfilesub != null) {
				try {
					pmstinsertsecprinfilesub.close();
					pmstinsertsecprinfilesub = null;
				} catch (SQLException e) {
				}
			}
			if (pmstselectnosecprintfile != null) {
				try {
					pmstselectnosecprintfile.close();
					pmstselectnosecprintfile = null;
				} catch (SQLException e) {
				}
			}
			if (pmstdeletenosecprinfilesub != null) {
				try {
					pmstdeletenosecprinfilesub.close();
					pmstdeletenosecprinfilesub = null;
				} catch (SQLException e) {
				}
			}
			if (pmstinsertnosecprinfilesub != null) {
				try {
					pmstinsertnosecprinfilesub.close();
					pmstinsertnosecprinfilesub = null;
				} catch (SQLException e) {
				}
			}
			if (pmstupdatesecburn != null) {
				try {
					pmstupdatesecburn.close();
					pmstupdatesecburn = null;
				} catch (SQLException e) {
				}
			}
			if (pmstupdatenosecburn != null) {
				try {
					pmstupdatenosecburn.close();
					pmstupdatenosecburn = null;
				} catch (SQLException e) {
				}
			}
		
		
			logger.info("打印刻录作业结果通知接口结束");
			DBManager.close(conn);

		}
		
		WriteOutXML wox = new WriteOutXML();
	    String outxmldata="";
		try {
			outxmldata = wox.ExecElementToXml(result,desc);
        	logger.info("打印刻录作业结果通知接口出参："+outxmldata);

		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     return  outxmldata;
	   
	  
	 }
	
	
	
	
	
	

}