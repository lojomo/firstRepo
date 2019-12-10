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

import com.suncreate.webService.model.FilePrint;

import com.suncreate.webService.service.IwsCreateBarcode;
import com.suncreate.webService.service.IwsPrintFile;



public class wsCreateBarcode  extends HttpServlet implements IwsCreateBarcode {
	
	public static Logger logger = Logger.getLogger(wsCreateBarcode.class);
	

	/**
	 * 文件申请打印接口
	 */
	public String GenPDF417Barcode(String xmldata) {
		String xml ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<root>"+
		"<printInfo>明码</printInfo> "+
		"<barcode>417码</barcode> "+
		"</root>";
			System.out.println(xmldata);
	   return xml;
	  
	 }
	
	
	
	
	
	

}