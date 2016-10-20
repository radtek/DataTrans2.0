package com.hisign.datatrans.utils;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.xml.sax.InputSource;

/**
 * tabConfig.xml文件解析器
 * @author hisign
 */
@SuppressWarnings("unchecked")
public class TabXmlParser {
	
	public static Map getTabConfig(String configName) throws Exception {
		String outStoreConfigFileName = ConfigUtil.getConfig(configName);
		InputStream inputStream = TabXmlParser.class.getClassLoader().getResourceAsStream(outStoreConfigFileName);
		List list = parse(inputStream);
		return (Map)list.get(0);
	}

	public static List parse(InputStream inputStream) throws Exception {
		InputSource source = new InputSource(inputStream);
		SAXBuilder sber = new SAXBuilder();
		org.jdom.Document doc = sber.build(source);
		org.jdom.Element root = doc.getRootElement();

		// 获取根节点下的所有mainTab节点
		List mainTabList = XPath.selectNodes(root, "/tabConfig/mainTab");
		List mainTabs = new ArrayList();

		for (int i = 0; i < mainTabList.size(); i++) {
			org.jdom.Element e = (org.jdom.Element) mainTabList.get(i);
			// 将xml节点信息封装成Map对象
			mainTabs.add(getTabMap(e));
		}
		return mainTabs; 
	}
	
	/**
	 * 将xml节点信息封装成Map对象
	 * @param e xml节点
	 * @return
	 */
	private static Map getTabMap(org.jdom.Element e) {
		Map mainTab = new HashMap();// 存放mainTab节点信息
		List subTabList = new ArrayList();// 存放subTab节点信息
		mainTab.put("subTabList", subTabList);
		// 获取节点下所有属性名称和属性值，放入mainTab对象
		List<Attribute> attrs = e.getAttributes();
		for (Attribute attr : attrs) {
			mainTab.put(attr.getName(), attr.getValue());
		}
		// 获取节点下所有子节点
		List children = e.getChildren();
		for (int j = 0; j < children.size(); j++) {
			org.jdom.Element e1 = (org.jdom.Element) children.get(j);
			if ("subTab".equalsIgnoreCase(e1.getName())) {
				// 获取mainTab下的subTab节点并放入subTabList对象
				subTabList.add(getTabMap(e1));
			} else {
				// 将子节点的属性名称和属性值也放入mainTab对象
				mainTab.put(e1.getName(), e1.getValue());
			}
		}
		return mainTab;
	}

}
