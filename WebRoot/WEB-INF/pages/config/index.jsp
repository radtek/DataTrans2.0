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
	<script type="text/javascript">
		// 修改参数信息
		function saveConfig(key, oldvalue) {
			if (confirm("确认修改参数值吗？")) {
				var url = "<%=path%>/config_modify.action?time="+new Date();
				$.get(url, {"config.key":key, "config.value":$("#"+key).val()}, function(result) {
					alert(result);
					if ("failure"==result) $("#"+key).val(oldvalue);
	 			});
			}
		}
		
		function itemfocus(cnt) {
			$(".button").hide();
			$("#btn_" + cnt).show();
		}
		
		$(document).ready(function () {
			$("tr[name='xckyUrlTr']").each(function (i) {
				var id = $(this).find("#id").val();
				var openFlag = $(this).find("#openFlag").val();
				
				if (openFlag == 1) {
					var url = "<%=path%>/config_testDBConnect.action?time="+new Date();
					$.get(url, {"dataSourceInfo.id":id}, function (data) {
						if (data != null) {
							var x = eval("(" + data + ")");
							if (x.ifConnect == 1) {
								$("#ifConnect_"+(i+1)).html("连接成功");
							} else {
								$("#ifConnect_"+(i+1)).html("<font color='gray'>连接异常</font>");
							}
							$("#dataReportCount_"+(i+1)).html(x.dataReportCount);
						}
					});
				} else {
					$("#ifConnect_"+(i+1)).html("未检测");
					$("#dataReportCount_"+(i+1)).html("未检测");
				}
			});
			
			$("#toggleShow").toggle(function() {
				$(".toggleShow").show();
				$(this).html("隐藏未启用单位");
			}, function() {
				$(".toggleShow").hide();
				$(this).html("显示未启用单位");
			});
		})
		
		// 更新数据库信息
		function modifyDbInfo(e, id, openFlag) {
			var url = "<%=path%>/config_modifyDbInfo.action?time="+new Date(); 
			$.get(url, {"dataSourceInfo.id":id, "dataSourceInfo.openFlag":openFlag}, function(result) {
				if (result=='success') {
					location.reload();
				} else {
					alert(result);
				}
 			});
		}
		
		// 删除数据库信息
		function deleteDbInfo(id) {
			if (!confirm("是否确认操作？")) {
				return;
			}
			var url = "<%=path%>/config_deleteDbInfo.action?time="+new Date(); 
			$.get(url, {"dataSourceInfo.id":id}, function(result) {
				if (result=='success') {
					location.reload();
				} else {
					alert(result);
				}
 			});
		}
		
		// 新增数据库信息
		function addDbInfo() {
			var required = $(".required");
			var pass = true;
			
			for (var i = 0; i < required.length; i++) {
				if ($(required[i]).val() == '') {
					pass = false;
				}
			}
			
			if (!pass) {
				alert('信息填写不完整！');
				return;
			}
			
			var url = "<%=path%>/config_testDBConnect.action?time="+new Date();
			$.get(url, {"dataSourceInfo.dbUrl":$("#dbUrl").val(), "dataSourceInfo.dbUserName":$("#dbUserName").val(), 
				"dataSourceInfo.dbPassword":$("#dbPassword").val()}, function (data) {
				if (data != null) {
					var x = eval("(" + data + ")");
					if (x.ifConnect == 1) {
						$("#addXckyUrlIndexForm").submit();
					} else {
						alert("无法连接数据库，添加失败！");
					}
				}
			});
		}
	</script>
</head>
<body style="margin: 1%">
	<center><br><h1>现勘数据上报程序<s:property value="version" /></h1></center>
	
	<hr>
	
	<h4>参数配置</h4>
	<table width="100%" border="0" cellpadding="1" cellspacing="1" class="form_table" align="center">
		<tr class="list_title">
			<td width="5%">序号</td>
			<td width="30%">参数名称</td>
			<td width="50%">参数值</td>
			<td width="15%">操作</td>
		</tr>
		<s:iterator value="configList" status="m">
			<s:if test="#m.odd">
				<tr class="list_single">
			</s:if>
			<s:else>
				<tr class="list_double">
			</s:else>
			<td><s:property value="#m.count"/></td>
			<td>
				<s:if test="key.endsWith('Plan')">
					<s:property value="name" />&nbsp;&nbsp;<a href="#cron" style="color:red">配置说明</a>
				</s:if>
				<s:else>
					<s:property value="name" />
				</s:else>
			</td>
			<td>
				<input type="text" id='<s:property value="key"/>' value='<s:property value="value"/>' 
					class="longfield" onfocus="itemfocus('<s:property value="#m.count"/>')" maxlength='<s:property value="valueLength"/>'>
			</td>
			<td style="text-align: center;">
				<button id="btn_<s:property value="#m.count"/>" type="button" class="button"
					onclick="saveConfig('<s:property value="key"/>', '<s:property value="value"/>')" style="display: none">
					保存修改
				</button>
			</td>
			</tr>
		</s:iterator>
	</table>
	
	<hr />
	
	<h4>
		下级单位上报状态&nbsp;
		<a href="javascript:location.reload();"><img alt="" src="<%=path%>/images/arrow_last.gif" align="absmiddle" width="12" height="12">刷新列表</a>&nbsp;
		<img alt="" src="<%=path%>/images/Play.png" align="absmiddle"><a id="toggleShow" href="#" onclick="return false;">显示未启用单位</a>&nbsp;
		<a href="<%=path%>/druid"><img alt="" src="<%=path%>/images/diannao.gif" align="absmiddle" width="12" height="12">数据库监控</a>&nbsp;
		<a href="#" onclick="javascript:$('#addXckyUrlIndexDiv').show();"><img alt="" src="<%=path%>/images/nolines_plus.gif" align="absmiddle">添加上报单位</a>
	</h4>
	<div id="addXckyUrlIndexDiv" style="display:none">
	<form action="<%=path%>/config_addDbInfo.action" method="post" id="addXckyUrlIndexForm">
		<table width="100%" border="0" cellpadding="1" cellspacing="1" class="form_table" align="center">
			<tr class="list_single">
				<td width="30%" style="text-align: right">单位代码：</td>
				<td><input type="text" class="shortfield required" name="dataSourceInfo.unitCode" id="unitCode"></td>
			</tr>
			<tr class="list_single">
				<td width="10%" style="text-align: right">单位名称：</td>
				<td><input type="text" class="shortfield required" name="dataSourceInfo.unitName" id="unitName"></td>
			</tr>
			<tr class="list_single">
				<td width="10%" style="text-align: right">数据库URL：<br>如：	jdbc:oracle:thin:@10.8.8.10:1521:xcky&nbsp;</td>
				<td><input type="text" class="shortfield required" name="dataSourceInfo.dbUrl" id="dbUrl" value="jdbc:oracle:thin:@x.x.x.x:1521:xcky"></td>
			</tr>
			<tr class="list_single">
				<td width="10%" style="text-align: right">数据库用户名：</td>
				<td><input type="text" class="shortfield required" name="dataSourceInfo.dbUserName" id="dbUserName"></td>
			</tr>
			<tr class="list_single">
				<td width="10%" style="text-align: right">数据库密码：</td>
				<td><input type="text" class="shortfield required" name="dataSourceInfo.dbPassword" id="dbPassword"></td>
			</tr>
			<tr class="list_single">
				<td colspan="2" style="text-align: center"><button type="button" onclick="addDbInfo();">保存</button></td>
			</tr>
		</table>
	</form>
	</div>
	<table width="100%" border="0" cellpadding="1" cellspacing="1" class="form_table" align="center">
		<tr class="list_title">
			<td width="5%">序号</td>
			<td width="15%">单位</td>
			<td width="20%">数据库URL</td>
			<td width="10%">数据库用户</td>
			<td width="10%">数据库连接状态</td>
			<td width="10%">待出库现场</td>
			<td width="10%">上报状态</td>
			<td width="10%">上报开关</td>
			<td width="10%">操作</td>
		</tr>
		<s:iterator value="dataSourceInfoList" status="x">
			<tr class="list_single <s:if test='openFlag==0'>toggleShow</s:if>" name="xckyUrlTr" style="display:<s:if test='openFlag==0'>none</s:if>">
				<td>
					<s:property value="#x.count"/>
					<s:hidden name="unitCode"></s:hidden>
					<s:hidden name="id"></s:hidden>
					<s:hidden name="openFlag"></s:hidden>
				</td>
				<td>
					[<s:property value="unitCode"/>]
					<s:property value="unitName"/>
				</td>
				<td>
					<s:property value="dbUrl"/>
				</td>
				<td>
					<s:property value="dbUserName"/>
				</td>
				<td id="ifConnect_<s:property value="#x.count"/>">
					<font color="gray">连接中...</font>
				</td>
				<td id="dataReportCount_<s:property value="#x.count"/>">
				</td>
				<td>
					<s:property value="report"/>
				</td>
				<td class="openFlag">
					<s:if test="openFlag==1">
						开启
					</s:if>
					<s:else>
						关闭
					</s:else>
				</td>
				<td>
					<s:if test="openFlag==1">
						<button onclick="modifyDbInfo(this,'<s:property value="id"/>', '0')">关闭</button>
					</s:if>
					<s:else>
						<button onclick="modifyDbInfo(this,'<s:property value="id"/>', '1')">开启</button>
					</s:else>
					<button onclick="deleteDbInfo('<s:property value="id"/>')">删除</button>
				</td>
			</tr>
		</s:iterator>
	</table>
	
	<hr />
	
	<p>
		<b>数据上报规则说明</b><br>
		如：433443333333333333<br>
		共18位长度，每一位代表一类数据信息，每一位的取值为0,1,2,3,4，其中0表示不上报，1表示上报到分县局，2表示上报到市局，3表示上报到省厅，4表示上报到公安部<br>
		每一位代表的具体含义：<br>
		第1位：现场基本信息和现场分析意见 <br>
		第2位：现场图信息和现场照片信息<br>
		第3位：检验鉴定信息 <br>
		第4位：现场手印痕迹 <br>
		第5位：现场足印痕迹 <br>
		第6位：现场工具痕迹 <br>
		第7位：现场枪弹痕迹 <br>
		第8位：现场特殊痕迹 <br>
		第9位：生物物证信息<br>
		第10位：毒化物证信息 <br>
		第11位：理化物证信息 <br>
		第12位：文件物证信息 <br>
		第13位：电子物证信息 <br>
		第14位：视听物证信息 <br>
		第15位：其他物证信息<br>
		第16位：现场提取物品信息 <br>
		第17位：现场物证保管信息 <br>
		第18位：尸体信息<br>
	</p>
	
	<hr />
	
	<p>
		<a name="cron"><b>定时任务时间表达式: [秒] [分] [小时] [日] [月] [周] [年]</b></a><br>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" class="form_table" align="center">
			<tr class="list_double">
				<td>时间项</td><td>是否必填</td><td>允许填写的值</td><td>允许的通配符</td>
			</tr>
			<tr class="list_double">
				<td>秒</td><td>是</td><td>0-59</td><td>, - * /</td>
			</tr>
			<tr class="list_double">
				<td>分</td><td>是</td><td>0-59</td><td>, - * /</td>
			</tr>
			<tr class="list_double">
				<td>小时</td><td>是</td><td>0-23</td><td>, - * /</td>
			</tr>
			<tr class="list_double">
				<td>日</td><td>是</td><td>1-31</td><td>, - * ? / L W</td>
			</tr>
			<tr class="list_double">
				<td>月</td><td>是</td><td>1-12 或 JAN-DEC</td><td>, - * /</td>
			</tr>
			<tr class="list_double">
				<td>周</td><td>是</td><td>1-7 或 SUN-SAT</td><td>, - * ? / L #</td>
			</tr>
			<tr class="list_double">
				<td>年</td><td>否</td><td>empty 或 1970-2099</td><td>, - * /</td>
			</tr>
		</table>
	
		通配符说明：<br>
		* 表示所有值。例如:在分的字段上设置 "*"，表示每一分钟都会触发。<br>
		? 表示不指定值。使用的场景为不需要关心当前设置这个字段的值。例如：要在每月的10号触发一个操作，但不关心是周几，所以需要周位置的那个字段设置为"?" 具体设置为 0 0 0 10 * ? <br>
		- 表示区间。例如：在小时上设置 "10-12"，表示 10，11，12点都会触发。<br>
		, 表示指定多个值。例如在周字段上设置 "MON,WED,FRI" 表示周一，周三和周五触发。<br>
		/ 用于递增触发。如在秒上面设置"5/15" 表示从5秒开始，每增15秒触发(5,20,35,50)。在月字段上设置'1/3'所示每月1号开始，每隔三天触发一次。<br>
		L 表示最后的意思。在日字段设置上，表示当月的最后一天(依据当前月份，如果是二月还会依据是否是润年[leap]), 在周字段上表示星期六，相当于"7"或"SAT"。如果在"L"前加上数字，则表示该数据的最后一个。例如在周字段上设置"6L"这样的格式,则表示“本月最后一个星期五"。<br>
		W 表示离指定日期的最近那个工作日(周一至周五) 。 例如在日字段上设置"15W"，表示离每月15号最近的那个工作日触发。如果15号正好是周六，则找最近的周五(14号)触发, 如果15号是周未，则找最近的下周一(16号)触发.如果15号正好在工作日(周一至周五)，则就在该天触发。如果指定格式为 "1W",它则表示每月1号往后最近的工作日触发。如果1号正是周六，则将在3号下周一触发。(注，"W"前只能设置具体的数字,不允许区间"-")。<br> 
		<br>小提示：<br>
		'L'和 'W'可以一组合使用。如果在日字段上设置"LW",则表示在本月的最后一个工作日触发(一般指发工资) <br>
		# 序号(表示每月的第几个周几) 。例如在周字段上设置"6#3"表示在每月的第三个周六.注意如果指定"#5",正好第五周没有周六，则不会触发该配置(用在母亲节和父亲节再合适不过了)。 <br>
		<br>小提示：<br>
		周字段的设置，若使用英文字母是不区分大小写的 MON 与mon相同。<br>
		
		<br>常用示例: <br>
		0 0 12 * * ? 每天12点触发<br>
		0 15 10 ? * * 每天10点15分触发<br> 
		0 15 10 * * ? 每天10点15分触发<br>
		0 15 10 * * ? * 每天10点15分触发<br>
		0 15 10 * * ? 2005 2005年每天10点15分触发<br>
		0 * 14 * * ? 每天下午的2点到2点59分每分触发<br>
		0 0/5 14 * * ? 每天下午的2点到2点59分(整点开始，每隔5分触发) <br>
		0 0/5 14,18 * * ? 每天下午的2点到2点59分(整点开始，每隔5分触发)，每天下午的 18点到18点59分(整点开始，每隔5分触发) <br>
		
		0 0-5 14 * * ? 每天下午的2点到2点05分每分触发<br>
		0 10,44 14 ? 3 WED 3月分每周三下午的2点10分和2点44分触发<br>
		0 15 10 ? * MON-FRI 从周一到周五每天上午的10点15分触发<br>
		0 15 10 15 * ? 每月15号上午10点15分触发<br>
		0 15 10 L * ? 每月最后一天的10点15分触发<br>
		0 15 10 ? * 6L 每月最后一周的星期五的10点15分触发<br>
		0 15 10 ? * 6L 2002-2005 从2002年到2005年每月最后一周的星期五的10点15分触发<br>
		0 15 10 ? * 6#3 每月的第三周的星期五开始触发<br>
		0 0 12 1/5 * ? 每月的第一个中午开始每隔5天触发一次<br>
		0 11 11 11 11 ? 每年的11月11号11点11分触发<br>
	</p>
</body>
</html>
