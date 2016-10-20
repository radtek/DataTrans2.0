package com.hisign.datatrans.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.sql.DataSource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hisign.datatrans.common.BaseAction;
import com.hisign.datatrans.domain.Config;
import com.hisign.datatrans.domain.Constants;
import com.hisign.datatrans.domain.DataSourceInfo;
import com.hisign.datatrans.domain.DataSourceInfoCache;
import com.hisign.datatrans.service.DataReportService;
import com.hisign.datatrans.utils.ConfigUtil;
import com.hisign.datatrans.utils.DataSourceUtil;
import com.hisign.datatrans.utils.MD5Util;
import com.hisign.datatrans.utils.MessageUtil;

/**
 * 数据上报信息配置
 * 
 * @author Hong
 */
@Controller
@Scope("prototype")
public class ConfigAction extends BaseAction {

	@Resource
	private DataReportService dataReportService;

	/**
	 * 参数列表
	 */
	private List<Config> configList = new ArrayList<Config>();

	/**
	 * 系统版本号
	 */
	private String version = ConfigUtil.getConfig("version");

	/**
	 * 参数配置信息对象
	 */
	private Config config;

	/**
	 * 数据库连接信息列表
	 */
	private List<DataSourceInfo> dataSourceInfoList;
	
	/**
	 * 数据库连接配置信息
	 */
	private DataSourceInfo dataSourceInfo;
	
	/**
	 * 登录密码
	 */
	private String password;

	/**
	 * 参数配置首页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String index() {
		if (!isLogin()) {
			return redirect("config_login.action");
		}
		
		// 读取配置信息列表
		Map<String, String> configMap = ConfigUtil.getPropertiesMap();
		for (Map.Entry<String, String> m : configMap.entrySet()) {
			String translation = MessageUtil.getConfig(m.getKey());
			if (StringUtils.isBlank(MessageUtil.getConfig(m.getKey()))) {
				continue;
			}

			Config config = new Config();
			config.setKey(m.getKey());
			config.setValue(m.getValue());
			String[] arr = translation.split("_");
			config.setIndex(arr[0]);
			config.setValueLength(arr[1]);
			config.setName(arr[2]);
			configList.add(config);
		}
		Collections.sort(configList, new Config());

		// 读取数据库信息列表
		dataSourceInfoList = DataSourceInfoCache.INSTANCE.getAll();

		return DEFAULT;
	}
	
	/**
	 * 登录
	 * @return
	 */
	public String login() {
		return DEFAULT;
	}
	
	/**
	 * 登录
	 * @return
	 */
	public String logon() {
		if (StringUtils.isNotBlank(password) && MD5Util.getDigest(password).toLowerCase().equals(ConfigUtil.getConfig("password"))) {
			this.request.getSession().setAttribute(Constants.LOGIN_STATUS, Constants.TRUE);
			return redirect("config_index.action");
		} else {
			return redirect("config_login.action");
		}
	}
	
	public boolean isLogin() {
		String loginStatus = (String) this.request.getSession().getAttribute(Constants.LOGIN_STATUS);
		
		if (Constants.TRUE.equals(loginStatus)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 修改参数信息
	 * 
	 * @throws IOException
	 */
	public void modify() throws IOException {
		if (!isLogin()) {
			this.setResponse(Constants.FAILURE);
		}
		
		String regex = ConfigUtil.getConfig("regex_" + config.getKey());
		
		if (StringUtils.isNotBlank(regex)) {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(config.getValue());
			if (!m.matches()) {
				this.setResponse(Constants.FAILURE);
				return;
			}
		}
		
		try {
			ConfigUtil.saveAppPropties(config.getKey(), config.getValue());
			ConfigUtil.init();
			this.setResponse(Constants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			this.setResponse(Constants.FAILURE);
		}
	}
	
	/**
	 * 修改数据库连接信息
	 * @throws IOException
	 */
	public void modifyDbInfo() throws IOException {
		if (!isLogin()) {
			this.setResponse(Constants.FAILURE);
		}
		try {
			dataReportService.modifyDataSourceInfo(dataSourceInfo);
			this.setResponse(Constants.SUCCESS);
		} catch (SQLException e) {
			e.printStackTrace();
			this.setResponse(Constants.FAILURE);
		}
	}
	
	/**
	 * 删除数据库连接信息
	 * @throws IOException
	 */
	public void deleteDbInfo() throws IOException {
		if (!isLogin()) {
			this.setResponse(Constants.FAILURE);
		}
		try {
			dataReportService.deleteDataSourceInfo(dataSourceInfo);
			this.setResponse(Constants.SUCCESS);
		} catch (SQLException e) {
			e.printStackTrace();
			this.setResponse(Constants.FAILURE);
		}
	}
	
	/**
	 * 新加数据库连接信息
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public String addDbInfo() throws SQLException {
		if (!isLogin()) {
			return redirect("config_login.action");
		}
		
		dataReportService.addDataSourceInfo(dataSourceInfo);
		
		return redirect("config_index.action");
	}
	
	/**
	 * 测试连接各地市数据库
	 * @throws IOException 
	 * 
	 * @throws Exception
	 */
	public void testDBConnect() throws IOException {
		if (!isLogin()) {
			return;
		}
		
		int totalCount = 0;
		try {
			DataSource dataSource = null;
			
			if (StringUtils.isNotBlank(dataSourceInfo.getId())) {
				dataSource = DataSourceInfoCache.INSTANCE.getDataSource(dataSourceInfo.getId());
			} else {
				dataSource = DataSourceUtil.createDataSource(dataSourceInfo);
			}
			
			totalCount = dataReportService.findDataReportTotalCount(dataSource);
			dataSourceInfo.setDataReportCount(totalCount);
			dataSourceInfo.setIfConnect(Constants.TRUE);
		} catch (SQLException e) {
			dataSourceInfo.setIfConnect(Constants.FALSE);
			e.printStackTrace();
		}
		
		JSONObject j = JSONObject.fromObject(dataSourceInfo);
		this.setResponse(j.toString());
	}

	public DataReportService getDataReportService() {
		return dataReportService;
	}

	public void setDataReportService(DataReportService dataReportService) {
		this.dataReportService = dataReportService;
	}

	public List<Config> getConfigList() {
		return configList;
	}

	public void setConfigList(List<Config> configList) {
		this.configList = configList;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public List<DataSourceInfo> getDataSourceInfoList() {
		return dataSourceInfoList;
	}

	public void setDataSourceInfoList(List<DataSourceInfo> dataSourceInfoList) {
		this.dataSourceInfoList = dataSourceInfoList;
	}

	public DataSourceInfo getDataSourceInfo() {
		return dataSourceInfo;
	}

	public void setDataSourceInfo(DataSourceInfo dataSourceInfo) {
		this.dataSourceInfo = dataSourceInfo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
