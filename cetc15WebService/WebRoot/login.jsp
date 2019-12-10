<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>保密系统</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body onload="autocommit()">
     <form id="loginform" name="loginform" method="post" action="<%=(String)request.getAttribute("baomiurl")%>/Baomi/LoginAction.do"  target="_blank">
         <input type="hidden" name="uid" id ="username"  value="<%=(String)request.getAttribute("username") %>" />
         <input type="hidden"  id ="password" name="password" value="<%=(String)request.getAttribute("password") %>" />
     </form>
  </body>
  
   <script>
     function autocommit(){
     document.forms[0].submit();
     }
   </script>
</html>
