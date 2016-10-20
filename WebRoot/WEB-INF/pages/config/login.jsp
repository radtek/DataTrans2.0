<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<script language="JavaScript" src="<%=path%>/scripts/jquery-1.4.min.js"></script>
	<link href="<%=path%>/styles/common.css" rel="stylesheet" type="text/css" />
</head>
<body style="margin: 1%">
	<center><br><h1>现勘数据上报程序<s:property value="version" /></h1></center>
	
	<form action="<%=path%>/config_logon.action" method="post">
		<table width="100%" border="0" cellpadding="1" cellspacing="1" class="form_table" align="center">
			<tr class="list_single">
				<td width="40%" style="text-align: right">登录密码：</td>
				<td width="60%">
					<input type="password" class="field required" name="password">&nbsp;&nbsp;
					<button type="submit">登录</button>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
