package com.hisign.datatrans.service;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hisign.datatrans.domain.Constants;
import com.hisign.datatrans.domain.DataSourceInfo;
import com.hisign.datatrans.domain.DataSourceInfoCache;

/**
 * 数据上报线程
 * 
 * @author Hong
 */
public class DataReportThread extends Thread {

	/**
	 * 任务号
	 */
	private long taskNum;

	/**
	 * 数据上报业务类
	 */
	private DataReportService dataReportService;

	/**
	 * 上报数据库配置信息
	 */
	private DataSourceInfo dataSourceInfo;

	/**
	 * 数据上报配置信息
	 */
	private Map tabConfig;

	protected final Log log = LogFactory.getLog(getClass());

	public DataReportThread(long taskNum, DataReportService dataReportService, DataSourceInfo dataSourceInfo,
			Map tabConfig) {
		this.taskNum = taskNum;
		this.dataReportService = dataReportService;
		this.dataSourceInfo = dataSourceInfo;
		this.tabConfig = tabConfig;
	}

	public void run() {
		log.info(dataSourceInfo.getUnitName() + "开始上报[" + taskNum + "]");
		dataSourceInfo.setReport(true);

		try {
			dataReportService.dataReport(dataSourceInfo.getUnitName(),
					DataSourceInfoCache.INSTANCE.getDataSource(dataSourceInfo.getId()),
					DataSourceInfoCache.INSTANCE.getDataSource(), tabConfig);
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		} finally {
			dataSourceInfo.setReport(false);
			log.info(dataSourceInfo.getUnitName() + "上报完成[" + taskNum + "]");
		}

	}

}
