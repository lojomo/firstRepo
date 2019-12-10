package com.suncreate.webService.service;

import java.util.List;

import com.suncreate.webService.model.DingMiYiJu;
import com.suncreate.webService.model.JgbBmxm;

public interface IwsDingmiforBaomi {
	public int isertTZ(String xml);
	public List<DingMiYiJu> getDmyj();
	public List<JgbBmxm> getBmxm(String xmmc);
	public List<JgbBmxm> getBmxmById(int id);
	public List<DingMiYiJu> getDmyjByPid(String pid);
	public List<DingMiYiJu> getDmyjById(String id);
	public List<DingMiYiJu> getDmyjByName(String name);
}
