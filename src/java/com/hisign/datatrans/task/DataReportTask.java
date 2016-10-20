package com.hisign.datatrans.task;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hisign.datatrans.common.BaseService;
import com.hisign.datatrans.domain.Constants;
import com.hisign.datatrans.domain.DataSourceInfo;
import com.hisign.datatrans.domain.DataSourceInfoCache;
import com.hisign.datatrans.domain.ThreadPoolCache;
import com.hisign.datatrans.service.DataReportService;
import com.hisign.datatrans.service.DataReportThread;
import com.hisign.datatrans.utils.ConfigUtil;
import com.hisign.datatrans.utils.TabXmlParser;

/**
 * 现场信息上报
 * 
 * @author Hong
 */
@Service("dataReportTask")
public class DataReportTask extends BaseService {

	/**
	 * 数据上报配置
	 */
	private static Map tabConfig = null;

	/**
	 * 现场信息数据上报业务
	 */
	@Resource
	private DataReportService dataReportService;

	/**
	 * 数据上报
	 */
	public void dataReport() {
		if (!Constants.TRUE.equals(ConfigUtil.getConfig("dataReportSwitch"))) {
			log.info("现场信息上报任务未开启.");
			return;
		}

		// 任务编号
		long taskNum = System.currentTimeMillis();
		log.info("数据上报程序启动,任务编号[" + taskNum + "]");

		// 读取出库表配置信息
		if (null == tabConfig) {
			try {
				tabConfig = TabXmlParser.getTabConfig("outStoreConfigFileName");
			} catch (Exception e) {
				log.info("读取出库表配置信息错误,程序终止[" + taskNum + "]");
				e.printStackTrace();
				return;
			}
		}

		// 获取所有数据库连接信息
		List<DataSourceInfo> dataSourceInfoList = DataSourceInfoCache.INSTANCE.getAll();

		try {
			for (DataSourceInfo dataSourceInfo : dataSourceInfoList) {
				if (Constants.TRUE.equals(dataSourceInfo.getOpenFlag()) && dataSourceInfo.isReport() == false) {
					DataReportThread t = new DataReportThread(taskNum, dataReportService, dataSourceInfo, tabConfig);
					ThreadPoolCache.INSTANCE.getThreadPool().execute(t);
				} else {
					log.info(dataSourceInfo.getUnitName() + "上报开关:["+dataSourceInfo.getOpenFlag()+"],上报状态:["+dataSourceInfo.isReport()+"]");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
