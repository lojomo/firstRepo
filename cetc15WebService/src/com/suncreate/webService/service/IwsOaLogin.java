package com.suncreate.webService.service;

import java.util.List;




public interface IwsOaLogin {
	/**
	 * 生成OA访问系统私钥
	 * @param xmlData
	 * @return
	 */
	String createKeyForOaLogin (String xmlData);

}