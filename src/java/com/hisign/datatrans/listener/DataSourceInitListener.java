package com.hisign.datatrans.listener;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import com.alibaba.druid.util.JdbcUtils;
import com.hisign.datatrans.domain.DataSourceInfo;
import com.hisign.datatrans.domain.DataSourceInfoCache;
import com.hisign.datatrans.utils.ConfigUtil;
import com.hisign.datatrans.utils.DataSourceUtil;

/**
 * 数据源初始化
 * 
 * @author Hong
 */
public class DataSourceInitListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {

	}

	public void contextInitialized(ServletContextEvent arg0) {

		String url = ConfigUtil.getConfig("jdbc.properties", "datasource.url");
		String username = ConfigUtil.getConfig("jdbc.properties", "datasource.username");
		String password = ConfigUtil.getConfig("jdbc.properties", "datasource.password");

		Connection conn = null;
		try {
			// 本地库
			DataSource dataSource = DataSourceUtil.createDataSource(url, username, password);
			DataSourceInfoCache.INSTANCE.setDataSource(dataSource);

			conn = dataSource.getConnection();
			List<Map<String, Object>> dbInfoList = JdbcUtils.executeQuery(conn, "select * from xcky_url_index", new ArrayList());

			// 下级库
			for (Map<String, Object> dbInfo : dbInfoList) {
				DataSourceInfo dataSourceInfo = new DataSourceInfo();
				dataSourceInfo.setId((String) dbInfo.get("ID"));
				dataSourceInfo.setUnitName((String) dbInfo.get("UNIT_NAME"));
				dataSourceInfo.setUnitCode((String) dbInfo.get("UNIT_CODE"));
				dataSourceInfo.setDbUrl((String) dbInfo.get("DB_URL"));
				dataSourceInfo.setDbUserName((String) dbInfo.get("DB_USERNAME"));
				dataSourceInfo.setDbPassword((String) dbInfo.get("DB_PASSWORD"));
				dataSourceInfo.setOpenFlag((String) dbInfo.get("OPEN_FLAG"));
				dataSourceInfo.setDataSource(DataSourceUtil.createDataSource(dataSourceInfo));

				DataSourceInfoCache.INSTANCE.put(dataSourceInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}
}
