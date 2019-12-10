<%@page contentType="text/html;charset=UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<html>
    <head>
        <meta http-equiv=Content-Type content="text/html; charset=UTF-8" />
        <title>错误说明-11所保密管理系统</title>
        <style>
            body {
                font-size: 12px;
                color: #000000;
            }

            table {
                font-size: 12px;
            }
        </style>
        <SCRIPT language="javascript">
           
           
        </SCRIPT>
    </head>
   
    <body >
        <p>
            <br>
            <br>
            <br>
        </p>
        <table width="360" height="200" border="0" align="center"
               cellpadding="0" cellspacing="0">
            <tr>
                <td align="center" valign="middle">
                    <table width="360" height="200" border="0" cellpadding="0"
                           cellspacing="0">
                        <tr>
                            <td valign="top"
                                background="<%=basePath%>images/bg_fail.gif">
                                <table width="85%" border="0" align="center" cellpadding="0"
                                       cellspacing="0">
                                    <tr>
                                        <td height="100">
                                            &nbsp;
                                        </td>
                                    </tr>

                                    <tr>
                                        <td height="60">
											提示信息：用户名或密码错误,或用户尚未被启用！
                                                        
                                        </td>
                                    </tr>
                                    
                                </table>
                            </td>
                        </tr>
                    </table>
                   
                  
                </td>
            </tr>
        </table>
    </body>
</html>