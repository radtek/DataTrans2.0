package com.hisign.datatrans.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * 数据库配置信息缓存类
 * 
 * @author Hong
 */
public enum DataSourceInfoCache {

	INSTANCE;

	/**
	 * 上报数据库连接信息列表
	 */
	private static Map<String, DataSourceInfo> children = new HashMap<String, DataSourceInfo>();
	
	/**
	 * 本地数据库
	 */
	private static DataSource dataSource;

	/**
	 * 放入数据
	 * 
	 * @param key
	 * @param dataSource
	 */
	public void put(DataSourceInfo dataSourceInfo) {
		children.put(dataSourceInfo.getId(), dataSourceInfo);
	}

	/**
	 * 取出数据
	 * 
	 * @param key
	 * @param dataSource
	 */
	public DataSourceInfo get(String key) {
		return children.get(key);
	}
	
	/**
	 * 获取数据库
	 * 
	 * @param key
	 * @param dataSource
	 */
	public DataSource getDataSource(String key) {
		return children.get(key).getDataSource();
	}
	
	/**
	 * 获取数据库
	 * 
	 * @param key
	 * @param dataSource
	 */
	public List<DataSourceInfo> getAll() {
		List<DataSourceInfo> result = new ArrayList<DataSourceInfo>();
		
		for (Map.Entry<String, DataSourceInfo> e : children.entrySet()) {
			result.add(e.getValue());
		}
		
		return result;
	}

	/**
	 * 删除数据
	 * 
	 * @param key
	 * @param dataSource
	 */
	public void remove(String key) {
		children.remove(key);
	}

	public static DataSource getDataSource() {
		return dataSource;
	}

	public static void setDataSource(DataSource dataSource) {
		DataSourceInfoCache.dataSource = dataSource;
	}

}
