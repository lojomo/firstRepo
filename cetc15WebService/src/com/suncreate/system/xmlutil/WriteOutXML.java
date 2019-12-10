package com.suncreate.system.xmlutil;


import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * 返回xml
 * @author zhanzhao
 *
 */
public class WriteOutXML {

	
	private static Logger logger = Logger.getLogger(WriteOutXML.class);
		
	public String ExecElementToXml(String beanname,List<?> beanlist,int flag) throws IOException, SQLException{
		
		StringWriter stringWriter = new StringWriter();
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		ConstuctionXmlOut(root,beanname,beanlist,flag); //构造输出的xml
	    OutputFormat format = OutputFormat.createPrettyPrint();
	    format.setTrimText(false); //设置XML节点中的换行
		format.setExpandEmptyElements(true);
		format.setEncoding("UTF-8");
	    format.setIndent("    ");
		XMLWriter xmlwriter = new XMLWriter(stringWriter ,format);
		xmlwriter.write(document);
		String strXml = stringWriter.toString();
		return strXml;
	}
	
	
	public String ExecElementToXml(int retUpdate,String desc,String requesturl){
		
		StringWriter stringWriter = new StringWriter();
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		Element data1 = root.addElement("result");
		Element data2 = root.addElement("desc");
		Element data3 = root.addElement("taskcount");

		if(retUpdate>0){
			data1.setText("1"); //设置返回值 处理成功
			data2.setText(desc);
			data3.setText(requesturl);
		}else{
			data1.setText("0"); //设置返回值 处理失败
			data2.setText(desc);
			data3.setText(requesturl);
		}
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setExpandEmptyElements(true);
	    format.setTrimText(false); //设置XML节点中的换行
		format.setEncoding("UTF-8");
	    format.setIndent("    ");
	    format.setLineSeparator("");
		XMLWriter xmlwriter = new XMLWriter(stringWriter ,format);
		try {
			xmlwriter.write(document);
		} catch (IOException e) {
			logger.error("(fault close)xmlwriter XML dom Exception：" + e.getMessage());
		}
		String strXml = stringWriter.toString();
		return strXml;
	}
      public String ExecElementToXml(int retUpdate,String desc){
		
		StringWriter stringWriter = new StringWriter();
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		Element data1 = root.addElement("result");
		Element data2 = root.addElement("desc");
		if(retUpdate>0){
			data1.setText("1"); //设置返回值 处理成功
			data2.setText(desc);
		}else{
			data1.setText("0"); //设置返回值 处理失败
			data2.setText(desc);
		}
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setExpandEmptyElements(true);
	    format.setTrimText(false); //设置XML节点中的换行
		format.setEncoding("GB2312");
	    format.setIndent("    ");
	    format.setLineSeparator("");
		XMLWriter xmlwriter = new XMLWriter(stringWriter ,format);
		try {
			xmlwriter.write(document);
		} catch (IOException e) {
			logger.error("(fault close)xmlwriter XML dom Exception：" + e.getMessage());
		}
		String strXml = stringWriter.toString();
		return strXml;
	}
	
      
      public String ExecObjectToXml(int retUpdate,String desc){
  		
    	  StringWriter stringWriter = new StringWriter();
  		Document document = DocumentHelper.createDocument();
  		Element root = document.addElement("userInfoESB");
  		Element data1 = root.addElement("dept_id");
  		Element data2 = root.addElement("dept_name");
  		Element data3 = root.addElement("user_id");
  		Element data4 = root.addElement("user_name");
  		Element data5 = root.addElement("userid");

  		data1.setText("28");
  		data2.setText("保密办");
  		data3.setText("1001");
  		data4.setText("管理员");
  		data5.setText("admin");
  		
  		
  		OutputFormat format = OutputFormat.createPrettyPrint();
  		format.setExpandEmptyElements(true);
  	    format.setTrimText(false); //设置XML节点中的换行
  		format.setEncoding("UTF-8");
  	    format.setIndent("    ");
  	    format.setLineSeparator("");
  		XMLWriter xmlwriter = new XMLWriter(stringWriter ,format);
  		try {
  			xmlwriter.write(document);
  		} catch (IOException e) {
  			logger.error("(fault close)xmlwriter XML dom Exception：" + e.getMessage());
  		}
  		String strXml = stringWriter.toString();
  		return strXml;
  	}


	/**
	 * 返回出参
	 * @param res
	 * @param element
	 * @param outparamOne
	 * @param outparamTwo
	 * @param sysSrc
	 * @return
	 * @throws SQLException 
	 */
	private void ConstuctionXmlOut(Element root, String beanname,
			List<?> beanlist, int flag) throws SQLException {

		Element out = root;
		Object o;
		try {
			o = Class.forName(beanname).newInstance();

			Method moths[] = o.getClass().getMethods();
			Field fields[] = o.getClass().getDeclaredFields();

			for (int i = 0; i < beanlist.size(); i++) {
				// 决定要不要加上item属性
				if (flag == 0) {
					out = root.addElement("result");
				}
				for (int j = 0; j < fields.length; j++) {
					Element tableCol = out.addElement(fields[j].getName()); //
					for (Method m : moths) {
						if (m.getName().startsWith("get")
								&& m.getName().substring(3).equalsIgnoreCase(
										fields[j].getName())
								&& m.invoke(beanlist.get(i)) != null) {

							tableCol.setText(m.invoke(beanlist.get(i))
									.toString());

							break;
						}
					}

				}
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
				
	
	
	
	
	
	}
		



