package com.hisign.datatrans.domain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池缓存类
 * 
 * @author Hong
 */
public enum ThreadPoolCache {

	INSTANCE;

	// 创建一个可缓存的线程池
	ExecutorService pool = null;

	public ExecutorService getThreadPool() {
		if (null == pool) {
			pool = Executors.newCachedThreadPool();
		}

		return pool;
	}

}
