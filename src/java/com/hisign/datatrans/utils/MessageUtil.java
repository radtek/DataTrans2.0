/*
 * ConfigUtil1.java Created on 2014-08-12
 * ALL Rights Reserved.
 */
package com.hisign.datatrans.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.hisign.datatrans.domain.Constants;

/**
 * 可编程的热部署
 * 功能：如果你没有修改配置文件，那么就直接从内存中取配置项，而无需每次都去读配置文件；
 * 如果你修改了配置文件，他会重新读一次配置文件，而无需重新reload应用。 
 * 具体实现：第一次读配置文件的时候，记录文件被修改的时间，并且将配置内容读到内容缓存； 
 * 以后每次读配置的时候，先去读文件被修改的时间，和记录下来的时间做对比，
 * 如果一致，就直接读内存缓存的配置项，否则的话，就重新从硬盘读配置项，
 * 并且更新文件修改时间的变量。
 * 其中：global.properties是配置文件，可以放到任意CLASSPATH路径下面，例如WEB-INF/classes下面
 */
public class MessageUtil {
    private static Properties props = null;    
    private static File configFile = null; 
    private static long fileLastModified = 0L; 
    
    private static String configFileName = "message.properties";
    
    public static void init() { 
        URL url = ConfigUtil.class.getClassLoader().getResource(configFileName); 
        configFile = new File(url.getFile()); 
        fileLastModified = configFile.lastModified();      
        props = new Properties(); 
        load(); 
    } 
   
    private static void load() { 
        try { 
            props.load(new FileInputStream(configFile)); 
            fileLastModified = configFile.lastModified(); 
        } catch (IOException e) {            
            throw new RuntimeException(e); 
        } 
    } 

    public static String getConfig(String key) { 
        if ((configFile == null) || (props == null)) init(); 
        if (configFile.lastModified() > fileLastModified) load(); 
        return props.getProperty(key);
    } 
    
    public static String getConfig(String configFileName,String key){
        URL url = ConfigUtil.class.getClassLoader().getResource(configFileName); 
        File config = new File(url.getFile()); 
        Properties properties = new Properties();
        try{
            properties.load(new FileInputStream(config)); 
        }catch (IOException e) {            
            throw new RuntimeException(e); 
        } 
        return properties.getProperty(key); 
    }
    /**
	 * 设置系统配置文件 的 安装初始化标记：已初始化
	 */
    public static void saveAppPropties(String key, String value) {
		URL apurl = ConfigUtil.class.getClassLoader().getResource(Constants.APP_MESSAGES_KEY);
		File appconfig = new File(apurl.getFile());
		PropertiesUtil apppro = new PropertiesUtil(appconfig.getAbsolutePath(),PropertiesUtil.DEF_ENCODING);
		apppro.setProperties(key,value);
		try {
			apppro.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    public static Map getPropertiesMap() {
    	Map map = new HashMap();
    	if ((configFile == null) || (props == null)) init(); 
        if (configFile.lastModified() > fileLastModified) load(); 
        Iterator iter = props.keySet().iterator();
        while (iter.hasNext()) {
        	String key = (String) iter.next();  
        	String value = props.getProperty(key);  
        	map.put(key, value);  
        }
		return map;
	}
    
}

