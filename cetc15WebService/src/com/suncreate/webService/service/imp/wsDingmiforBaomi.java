package com.suncreate.webService.service.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.suncreate.webService.model.DingMiYiJu;
import com.suncreate.webService.model.JgbBmxm;
import com.suncreate.system.db.DBManager;
import com.suncreate.system.xmlutil.ReadXML;
import com.suncreate.system.xmlutil.WriteOutXML;
import com.suncreate.webService.service.IwsDingmiforBaomi;

public class wsDingmiforBaomi implements IwsDingmiforBaomi{
	public static Logger logger = Logger.getLogger(wsDingmiforBaomi.class);
	/**
	 * 插入定密台账
	 */
	public int isertTZ(String xml) {
		PropertyConfigurator.configure(wsDingmiforBaomi.class.getClassLoader().getResource("").getPath()+"log.property");
		xml = xml.replace("&", "&amp;");
		String selectuserinfosql="select t.user_id,t.userid,t.user_name,t.department_name,t.department_number from secu_user t where t.userid = ? ";
		String selectuserinfosql2="select t.user_id,t.userid,t.user_name from secu_user t where t.user_id = ? ";
		String insertdmdmzwjtzsql="insert into BMB_DMDZWJTZ(UUID,MJBH,FILENAME,SECRET_LEVEL,DINGMITIME,CREATEPERSONLOGINID,CREATEPERSONID," +
				"CREATEPERSONNAME,DEPT_ID,DEPT_NAME,COMPUTERNAME,IP,MAC,DEADLINE,STATE,PROJECTID,CATALOGID,DINGMIYIJU,KNOWLEDGE_RANGE,BZ,ZRR_ID,ZRR_NAME,DMJZRQ) " +
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		String desc="";
		int result =1;
		
		ReadXML readXml = new ReadXML();
		
		String UUID = readXml.getParamValue(xml, "UUID", "root/data/UUID").trim(); //文件ID
		String MJBH = readXml.getParamValue(xml, "MJBH", "root/data/MJBH").trim(); //密级编号
		String FILENAME = readXml.getParamValue(xml, "FILENAME", "root/data/FILENAME").trim(); //文件名称
		String SECRET_LEVEL = readXml.getParamValue(xml, "SECRET_LEVEL", "root/data/SECRET_LEVEL").trim(); //文件密级
		String DINGMITIME = readXml.getParamValue(xml, "DINGMITIME", "root/data/DINGMITIME").trim(); //定密时间
		String CREATEPERSONLOGINID = readXml.getParamValue(xml, "CREATEPERSONLOGINID", "root/data/CREATEPERSONLOGINID").trim(); //申请人登陆id
		String COMPUTERNAME = readXml.getParamValue(xml, "COMPUTERNAME", "root/data/COMPUTERNAME").trim(); //计算机名
		String IP = readXml.getParamValue(xml, "IP", "root/data/IP").trim(); //计算机IP
		String MAC = readXml.getParamValue(xml, "MAC", "root/data/MAC").trim(); //计算机MAC
		String DEADLINE = readXml.getParamValue(xml, "DEADLINE", "root/data/DEADLINE").trim(); //保密期限
		String PROJECTID = readXml.getParamValue(xml, "PROJECTID", "root/data/PROJECTID").trim(); //项目ID
		String CATALOGID = readXml.getParamValue(xml, "CATALOGID", "root/data/CATALOGID").trim(); //目录ID
		String DINGMIYIJU = readXml.getParamValue(xml, "DINGMIYIJU", "root/data/DINGMIYIJU").trim(); //定密依据
		String KNOWLEDGE_RANGE = readXml.getParamValue(xml, "KNOWLEDGE_RANGE", "root/data/KNOWLEDGE_RANGE").trim(); //知悉范围
		String DMJZRQ = readXml.getParamValue(xml, "DMJZRQ", "root/data/DMJZRQ").trim(); //定密截止日期
		String ZRR_ID = readXml.getParamValue(xml, "ZRR_ID", "root/data/ZRR_ID").trim(); //备注
		String BZ = readXml.getParamValue(xml, "BZ", "root/data/BZ").trim(); //备注
		
		DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstuserselect = null;
		PreparedStatement pmstuserselect2 = null;
		PreparedStatement pmstinsert = null;
		ResultSet rsSel = null;
		ResultSet rsSel2 = null;
		
		try{
			logger.info("插入定密台账接口入参："+xml);
			conn = db.getConnection("proxool.test");
			
			if (!"".equals(CREATEPERSONLOGINID) && CREATEPERSONLOGINID != null && !"null".equals(CREATEPERSONLOGINID)) {
				pmstuserselect = conn.prepareStatement(selectuserinfosql);
				pmstuserselect.setString(1, CREATEPERSONLOGINID);
				rsSel = pmstuserselect.executeQuery();
			}
				
			if(rsSel.next()) {
				String USER_NAME = rsSel.getString(3);
				String USER_ID = rsSel.getString(1);
				String DEPT_NAME = rsSel.getString(4);
				String DEPT_ID = rsSel.getString(5);
				
				
				pmstinsert = conn.prepareStatement(insertdmdmzwjtzsql);
				pmstinsert.setString(1, UUID);
				pmstinsert.setString(2, MJBH);
				pmstinsert.setString(3, FILENAME);
				pmstinsert.setString(4, SECRET_LEVEL);
				pmstinsert.setString(5, DINGMITIME);
				pmstinsert.setString(6, CREATEPERSONLOGINID);
				pmstinsert.setString(7, USER_ID);
				pmstinsert.setString(8, USER_NAME);
				pmstinsert.setString(9, DEPT_ID);
				pmstinsert.setString(10, DEPT_NAME);
				pmstinsert.setString(11, COMPUTERNAME);
				pmstinsert.setString(12, IP);
				pmstinsert.setString(13, MAC);
				pmstinsert.setString(14, DEADLINE);
				pmstinsert.setString(15, "1");
				pmstinsert.setString(16, PROJECTID);
				pmstinsert.setString(17, CATALOGID);
				pmstinsert.setString(18, DINGMIYIJU);
				pmstinsert.setString(19, KNOWLEDGE_RANGE);
				pmstinsert.setString(20, BZ);
				pmstinsert.setString(21, "");
				pmstinsert.setString(22, "");
				pmstinsert.setString(23, DMJZRQ);
				result = pmstinsert.executeUpdate();
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
			if (rsSel2 != null) {
				try {
					rsSel2.close();
					rsSel2 = null;
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
			if (pmstuserselect2 != null) {
				try {
					pmstuserselect2.close();
					pmstuserselect2 = null;
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
			if(result==1){
				logger.info("生成定密台账成功结束");
			}else{
				logger.info("生成定密台账失败结束");
			}
		
			DBManager.close(conn);

		}
		
		WriteOutXML wox = new WriteOutXML();
	    String outxmldata="";
		try {
			outxmldata = wox.ExecElementToXml(result,desc);
        	logger.info("插入定密台账："+outxmldata);

		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return result;
	}
	
	/**
	 * 获取定密依据接口
	 */
	
	public List<DingMiYiJu> getDmyj() {
		PropertyConfigurator.configure(wsDingmiforBaomi.class.getClassLoader().getResource("").getPath()+"log.property");
		String selectdmyjsql="select distinct t1.id, t1.name, t1.parent_id, t2.project_level, t2.file_level,t1.ordinal,t2.security_year_limit,t2.knowledge_range  "+
        " from SECULEVEL_FORMULATION_CATALOG t1, SECULEVEL_FORMULATION_ITEM t2  where t1.id = t2.catalog_id(+) order by t1.ordinal ";
		DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstselect = null;
		ResultSet rsSel = null;
		List<DingMiYiJu> t1 = new ArrayList<DingMiYiJu>();
		try{
			conn = db.getConnection("proxool.test");
			pmstselect = conn.prepareStatement(selectdmyjsql);
			rsSel = pmstselect.executeQuery();
			while(rsSel.next()){
				DingMiYiJu t2 = new DingMiYiJu();
				t2.setId(rsSel.getString(1));
				t2.setName(rsSel.getString(2));
				t2.setParent_id(rsSel.getString(3));
				t2.setProject_level(rsSel.getString(4));
				t2.setFile_level(rsSel.getString(5));
				t2.setOrdinal(rsSel.getString(6));
				t2.setSecurity_year_limit(rsSel.getString(7));
				t2.setKnowledge_range(rsSel.getString(8));
				t1.add(t2);
			}
		}catch(SQLException e){
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
			logger.info("获取定密依据结束");
			DBManager.close(conn);

		}
		
		return t1;
	}
	/**
	 * 获取父节点查询定密依据接口
	 */
	
	public List<DingMiYiJu> getDmyjByPid(String pid) {
		PropertyConfigurator.configure(wsDingmiforBaomi.class.getClassLoader().getResource("").getPath()+"log.property");
		String selectdmyjsql="select distinct t1.id, t1.name, t1.parent_id, t2.project_level, t2.file_level,t1.ordinal,t2.security_year_limit,t2.knowledge_range  "+
        " from SECULEVEL_FORMULATION_CATALOG t1, SECULEVEL_FORMULATION_ITEM t2  where t1.id = t2.catalog_id(+) and t1.parent_id = '"+pid+"' order by t1.ordinal ";
		DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstselect = null;
		ResultSet rsSel = null;
		List<DingMiYiJu> t1 = new ArrayList<DingMiYiJu>();
		try{
			conn = db.getConnection("proxool.test");
			pmstselect = conn.prepareStatement(selectdmyjsql);
			rsSel = pmstselect.executeQuery();
			while(rsSel.next()){
				DingMiYiJu t2 = new DingMiYiJu();
				t2.setId(rsSel.getString(1));
				t2.setName(rsSel.getString(2));
				t2.setParent_id(rsSel.getString(3));
				t2.setProject_level(rsSel.getString(4));
				t2.setFile_level(rsSel.getString(5));
				t2.setOrdinal(rsSel.getString(6));
				t2.setSecurity_year_limit(rsSel.getString(7));
				t2.setKnowledge_range(rsSel.getString(8));
				t1.add(t2);
			}
		}catch(SQLException e){
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
			logger.info("获取父节点定密依据结束");
			DBManager.close(conn);

		}
		
		return t1;
	}
	
	/**
	 * 获取id查询定密依据接口
	 */
	
	public List<DingMiYiJu> getDmyjById(String id) {
		PropertyConfigurator.configure(wsDingmiforBaomi.class.getClassLoader().getResource("").getPath()+"log.property");
		String selectdmyjsql="select distinct t1.id, t1.name, t1.parent_id, t2.project_level, t2.file_level,t1.ordinal,t2.security_year_limit,t2.knowledge_range  "+
        " from SECULEVEL_FORMULATION_CATALOG t1, SECULEVEL_FORMULATION_ITEM t2  where t1.id = t2.catalog_id(+) and t1.id = '"+id+"' order by t1.ordinal ";
		DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstselect = null;
		ResultSet rsSel = null;
		List<DingMiYiJu> t1 = new ArrayList<DingMiYiJu>();
		try{
			conn = db.getConnection("proxool.test");
			pmstselect = conn.prepareStatement(selectdmyjsql);
			rsSel = pmstselect.executeQuery();
			while(rsSel.next()){
				DingMiYiJu t2 = new DingMiYiJu();
				t2.setId(rsSel.getString(1));
				t2.setName(rsSel.getString(2));
				t2.setParent_id(rsSel.getString(3));
				t2.setProject_level(rsSel.getString(4));
				t2.setFile_level(rsSel.getString(5));
				t2.setOrdinal(rsSel.getString(6));
				t2.setSecurity_year_limit(rsSel.getString(7));
				t2.setKnowledge_range(rsSel.getString(8));
				t1.add(t2);
			}
		}catch(SQLException e){
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
			logger.info("获取定密依据结束");
			DBManager.close(conn);

		}
		
		return t1;
	}
	
	/**
	 * 获取关键字查询定密依据接口
	 */
	
	public List<DingMiYiJu> getDmyjByName(String name) {
		PropertyConfigurator.configure(wsDingmiforBaomi.class.getClassLoader().getResource("").getPath()+"log.property");
		String selectdmyjsql="select distinct t1.id, t1.name, t1.parent_id, t2.project_level, t2.file_level,t1.ordinal,t2.security_year_limit,t2.knowledge_range  "+
        " from SECULEVEL_FORMULATION_CATALOG t1, SECULEVEL_FORMULATION_ITEM t2  where t1.id = t2.catalog_id(+) and t1.name like '%"+name+"%'  and t2.file_level is not null order by t1.ordinal ";
		DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstselect = null;
		ResultSet rsSel = null;
		List<DingMiYiJu> t1 = new ArrayList<DingMiYiJu>();
		try{
			conn = db.getConnection("proxool.test");
			pmstselect = conn.prepareStatement(selectdmyjsql);
			rsSel = pmstselect.executeQuery();
			while(rsSel.next()){
				DingMiYiJu t2 = new DingMiYiJu();
				t2.setId(rsSel.getString(1));
				t2.setName(rsSel.getString(2));
				t2.setParent_id(rsSel.getString(3));
				t2.setProject_level(rsSel.getString(4));
				t2.setFile_level(rsSel.getString(5));
				t2.setOrdinal(rsSel.getString(6));
				t2.setSecurity_year_limit(rsSel.getString(7));
				t2.setKnowledge_range(rsSel.getString(8));
				t1.add(t2);
			}
		}catch(SQLException e){
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
			logger.info("根据关键字获取定密依据结束");
			DBManager.close(conn);

		}
		
		return t1;
	}
	
	/**
	 * 获取项目名称和密级接口
	 */
	public List<JgbBmxm> getBmxm(String xmmc) {
		PropertyConfigurator.configure(wsDingmiforBaomi.class.getClassLoader().getResource("").getPath()+"log.property");
    	String selectbmxmsql = "select t.id, t.bmxm_title, t.bmxm_grade, t.bmxm_type, t.bmxm_model "+
                 "from jgb_bmxm t  where t.bmxm_title like '%"+xmmc+"%'";
    	System.out.println(selectbmxmsql);
    	DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstselect = null;
		ResultSet rsSel = null;
		List<JgbBmxm> t1 = new ArrayList<JgbBmxm>();
		try{
			conn = db.getConnection("proxool.test");
			pmstselect = conn.prepareStatement(selectbmxmsql);
			rsSel = pmstselect.executeQuery();
			while(rsSel.next()){
				JgbBmxm t2 = new JgbBmxm();
				t2.setId(rsSel.getString(1));
				t2.setBmxmTitle(rsSel.getString(2));
				t2.setBmxmGrade(rsSel.getString(3));
				t2.setBmxmType(rsSel.getString(4));
				t2.setBmxmModel(rsSel.getString(5));
				t1.add(t2);
			}
		}catch(SQLException e){
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
			logger.info("根据项目名称获取保密项目结束");
			DBManager.close(conn);

		}

		return t1;
	}
	
	
	
	/**
	 * 获取项目名称和密级接口
	 */
	public List<JgbBmxm> getBmxmById(int id) {
		PropertyConfigurator.configure(wsDingmiforBaomi.class.getClassLoader().getResource("").getPath()+"log.property");
    	String selectbmxmsql = "select t.id, t.bmxm_title, t.bmxm_grade, t.bmxm_type, t.bmxm_model "+
                 "from jgb_bmxm t  where t.id = "+id;
    	System.out.println(selectbmxmsql);
    	DBManager  db = DBManager.getInstanse();
		Connection conn = null;
		PreparedStatement pmstselect = null;
		ResultSet rsSel = null;
		List<JgbBmxm> t1 = new ArrayList<JgbBmxm>();
		try{
			conn = db.getConnection("proxool.test");
			pmstselect = conn.prepareStatement(selectbmxmsql);
			rsSel = pmstselect.executeQuery();
			while(rsSel.next()){
				JgbBmxm t2 = new JgbBmxm();
				t2.setId(rsSel.getString(1));
				t2.setBmxmTitle(rsSel.getString(2));
				t2.setBmxmGrade(rsSel.getString(3));
				t2.setBmxmType(rsSel.getString(4));
				t2.setBmxmModel(rsSel.getString(5));
				t1.add(t2);
			}
		}catch(SQLException e){
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
			logger.info("根据项目ID获取保密项目结束");
			DBManager.close(conn);

		}

		return t1;
	}

}
