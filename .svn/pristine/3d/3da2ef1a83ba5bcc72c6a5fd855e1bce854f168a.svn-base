package com.hisign.datatrans.utils;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.hisign.datatrans.domain.DataSourceInfo;

/**
 * 数据源工具
 * 
 * @author Hong
 */
public class DataSourceUtil {

	/**
	 * 创建数据源
	 * 
	 * @param driverClassName
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static DataSource createDataSource(DataSourceInfo dataSourceInfo) throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(dataSourceInfo.getDbUrl());
		dataSource.setUsername(dataSourceInfo.getDbUserName());
		dataSource.setPassword(dataSourceInfo.getDbPassword());
		
		dataSource.setDriverClassName(ConfigUtil.getConfig("jdbc.properties", "datasource.driverClassName"));
		dataSource.setInitialSize(Integer.parseInt(ConfigUtil.getConfig("jdbc.properties", "datasource.initSize")));
		dataSource.setMaxActive(Integer.parseInt(ConfigUtil.getConfig("jdbc.properties", "datasource.maxActive")));
		dataSource.setMinIdle(Integer.parseInt(ConfigUtil.getConfig("jdbc.properties", "datasource.maxIdle")));
		dataSource.setMaxWait(Integer.parseInt(ConfigUtil.getConfig("jdbc.properties", "datasource.maxWait")));
		dataSource.setValidationQuery(ConfigUtil.getConfig("jdbc.properties", "datasource.validationQuery"));
		dataSource.setTestWhileIdle(Boolean.parseBoolean(ConfigUtil.getConfig("jdbc.properties",
				"datasource.testWhileIdle")));
		dataSource.setTestOnBorrow(Boolean.parseBoolean(ConfigUtil.getConfig("jdbc.properties",
				"datasource.testOnBorrow")));
		dataSource.setTestOnReturn(Boolean.parseBoolean(ConfigUtil.getConfig("jdbc.properties",
				"datasource.testOnReturn")));
		dataSource.setTimeBetweenEvictionRunsMillis(Integer.parseInt(ConfigUtil.getConfig("jdbc.properties",
				"datasource.timeBetweenEvictionRunsMillis")));
		dataSource.setMinEvictableIdleTimeMillis(Integer.parseInt(ConfigUtil.getConfig("jdbc.properties",
				"datasource.minEvictableIdleTimeMillis")));
		dataSource.setPoolPreparedStatements(Boolean.parseBoolean(ConfigUtil.getConfig("jdbc.properties",
				"datasource.poolPreparedStatements")));
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(ConfigUtil.getConfig(
				"jdbc.properties", "datasource.maxPoolPreparedStatementPerConnectionSize")));
		dataSource.setFilters(ConfigUtil.getConfig("jdbc.properties", "datasource.filters"));
		
		return dataSource;
	}
	
	/**
	 * 创建数据源
	 * 
	 * @param driverClassName
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static DataSource createDataSource(String dbUrl, String dbUserName, String dbPassword) throws SQLException {
		DataSourceInfo dataSourceInfo = new DataSourceInfo();
		dataSourceInfo.setDbUrl(dbUrl);
		dataSourceInfo.setDbUserName(dbUserName);
		dataSourceInfo.setDbPassword(dbPassword);
		
		return createDataSource(dataSourceInfo);
	}
}
