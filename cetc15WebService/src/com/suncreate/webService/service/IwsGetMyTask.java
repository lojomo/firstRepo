package com.suncreate.webService.service;

import java.util.List;




public interface IwsGetMyTask {
	/**
	 * OA获取登录人待办任务
	 * @param xmlData
	 * @return
	 */
	//String sendRequestGetMyTaskJob (String xmlData);
	
	String getMyTaskCount (String xmlData);

}