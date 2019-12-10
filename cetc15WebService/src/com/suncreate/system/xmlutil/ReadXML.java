package com.suncreate.system.xmlutil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
/**
 * 解析Xml
 * @author zhanzhao
 *
 */
public class ReadXML {

	private static Logger logger = Logger.getLogger(ReadXML.class);
	private  Document doc = null;
	/**
	 * 
	 * @param xml
	 * @param param
	 * @param xmlPath
	 * @return
	 */
	 public String getParamValue(String xml, String param,String xmlPath){
	
		getDoc(xml);
		try{
			if(doc!=null){
	    		String xpathNodes =  "//" + xmlPath;
		    	List<Element> listParam = doc.selectNodes(xpathNodes);
		    	if (listParam == null)
		    		return "";
		    	for ( Iterator iter = listParam.iterator(); iter.hasNext(); ){
		    		Element nodeParam = (Element)iter.next();
		    		if (param.equals(nodeParam.getName())){
		    			return nodeParam.getText();
		    		}
		    	}
	    	}	    
		}catch(Exception e){
			logger.error("xml==" + xml);
			logger.error("getParamValue异常：" + e);			
		}
    	return "";
	 }
	 
	 
	 
	 /**
	  * 解析xml文，构造DOC对象
	  * @param xml
	  * @return
	  */
	 public void getDoc(String xml){
			try {
				if(doc==null){
					doc = DocumentHelper.parseText(xml);
				}				
			} catch (DocumentException e) {
				logger.error("DOC对象xml==" + xml);
				logger.error("解析xml文 构造DOC对象异常:" + e.getMessage() + "===" + e);
			}
	 }
	 
	 /**
	  * 
	  * @param xml
	  * @param param
	  * @param xmlPath
	  * @return
	  */
	    public String getParamAtrub(String xml, String param,String xmlPath)
	    {
	    	Document doc = null;
			try {
				doc = DocumentHelper.parseText(xml);
			} catch (DocumentException e) {
				System.out.println("获取参数值失败：解析XML异常" + e);
				logger.error("获取参数值失败：解析XML异常" + e.getMessage());
				return "";
			}
	    	
	    	String xpathNodes =  "//" + xmlPath;
	    	List<Element> listParam = doc.selectNodes(xpathNodes);
	    	if (listParam == null)
	    		return "";
	    	for ( Iterator iter = listParam.iterator(); iter.hasNext(); ){
	    		Element nodeParam = (Element)iter.next();
	    		 for(Iterator itAttribute=nodeParam.attributeIterator();itAttribute.hasNext();){
	    			 Attribute attribute = (Attribute) itAttribute.next();
	                 if(param.endsWith(attribute.getName())){
	                	 return attribute.getText();
	                 }
	    		 }
	    	}
	    	return "";
	    }
	    
	    /**
	     * 把xml解析成对象
	     * @param xml
	     * @param xmlPath
	     * @param beanName
	     * @return
	     */
	    public ArrayList<?> getParamList(String xml,String xmlPath,String beanName){
	    	this.getDoc(xml);
	    	ArrayList<Object> paramList = new ArrayList<Object>();
			try {
				if (doc != null) {
					String xpathNodes =  "//" + xmlPath;
					List<Element> listParam = doc.selectNodes(xpathNodes);
					if (listParam == null)
						return paramList;
					Object o = Class.forName(beanName).newInstance();
					Method moths[] = o.getClass().getMethods();
					for (int i = 0; i < listParam.size(); i++) {
						o = Class.forName(beanName).newInstance();
						Element element = listParam.get(i);
						List<Element>  sonelementList = element.elements();
						for(int j = 0;j < sonelementList.size(); j ++){
							Element sonelement = sonelementList.get(j);
							for(Method m : moths){
								if(m.getName().startsWith("set") && m.getName().substring(3).equalsIgnoreCase(sonelement.getName())){
									m.invoke(o, sonelement.getText());
									break;
								}
							}
							
						}
						
						paramList.add(o);
					}
				}
			} catch (Exception e) {
				logger.error("xml==" + xml);
				logger.error("getParamValue异常：" + e);
			}
	    	return paramList;
	    }
		 

}
