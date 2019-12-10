package com.suncreate.webService.service;

import java.util.List;




public interface IwsCreateBarcode {
	/**
	 * 文件申请打印接口
	 * @param xmlData
	 * @return
	 */
	String GenPDF417Barcode (String xmlData);
	
	

}