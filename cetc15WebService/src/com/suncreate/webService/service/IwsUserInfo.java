package com.suncreate.webService.service;

import java.util.List;

import com.suncreate.webService.model.UserInfoESB;


public interface IwsUserInfo {
	String getuserInfoStr(UserInfoESB userInfoESB);
	String getuserInfo(UserInfoESB user);
	List<UserInfoESB> userNameList(String user_id);
	UserInfoESB userinfo(String user_id);

}
