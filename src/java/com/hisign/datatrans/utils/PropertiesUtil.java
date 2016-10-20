/*
 * PropertiesUtil.java Created on 2006-8-24
 * Copyright 2006@CBI Tech. 
 * All right reserved. 
 */
package com.hisign.datatrans.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 实现任意properties文件读写 支持各种字符集
 * 
 * @Time 下午04:09:27
 * @author zhangwei
 */
public class PropertiesUtil extends ArrayList {
	// 默认字符集
	public static final String DEF_ENCODING = "UTF-8";
	// 设置字符集
	private String encoding = "UTF-8";

	private String fileName;

	public PropertiesUtil(String fileName, String encoding) {
		try {
			this.setFileName(fileName);
			this.setCharacterEncoding(encoding);
			if (!isFileExist(fileName))
				this.write("");
			this.addAll(Arrays.asList(read(fileName, encoding).split("\n")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 设置字符集
	 * 
	 * @param encoding
	 * @throws UnsupportedEncodingException
	 */
	private void setCharacterEncoding(String encoding)
			throws UnsupportedEncodingException {
		// TestInsertInvestigation the encoding is valid
		new String("".getBytes("iso8859_1"), encoding);
		// Getting here means we're valid, so set the encoding
		this.encoding = encoding;
	}

	public static boolean isFileExist(String fileName) {
		return new File(fileName).isFile();
	}

	/**
	 * read file as single strings
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String read(String fileName, String encoding)
			throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String s;
		while ((s = in.readLine()) != null) {
			// 为什么转换了反而会是乱码
			sb.append(s);// new String(s.getBytes("iso8859_1"), encoding));
			sb.append("\n");
		}
		in.close();
		return sb.toString();
	}

	/**
	 * write file as single strings
	 * 
	 * @param text
	 * @throws IOException
	 */
	public void write(String text) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				fileName)));
		out.print(text);
		out.close();
	}

	/**
	 * save the content to file
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				fileName)));
		String tmp;
		for (int i = 0; i < size(); i++) {
			tmp = get(i) + "";
			// tmp = new String(tmp.getBytes("iso8859_1"),encoding);
			out.println(tmp);
		}
		out.close();
	}

	/**
	 * set properties file with a par key and value
	 * 
	 * @param key
	 * @param val
	 * @throws UnsupportedEncodingException
	 */
	public void setProperties(String key, String val) {
		int ipos = findKey(key);
		if (ipos >= 0)
			this.set(ipos, key + "=" + val);
		else
			this.add(key + "=" + val);
	}

	/**
	 * 查找KEY的序号
	 * 
	 * @param key
	 * @return
	 */
	public int findKey(String key) {
		try {
			String tmp;
			for (int i = 0; i < size(); i++) {
				tmp = get(i) + "";
				tmp = new String(tmp.getBytes("iso8859_1"), DEF_ENCODING);
				if (tmp.indexOf(key) == 0) {
					return i;
				}
			}
		} catch (Exception e) {
		}
		return -1;
	}

	/**
	 * 增加备注
	 * 
	 * @param memo
	 */
	public void setMemo(String key, String memo) {
		if ("".equals(key)) {
			this.add("#" + memo);
			return;
		}
		String tmp;
		int ret = findKey(key);
		if (ret == -1) {// 如果没有找到
			this.add("#" + memo);
			this.add(key + "=");
		} else {
			int ipos = ret - 1;
			if (ipos < 0)
				this.add(ipos, "#" + memo);
			else {
				tmp = this.get(ipos) + "";
				if ("#".equals(tmp.substring(0, 1)))
					this.set(ipos, "#" + memo);
				else
					this.add(ipos + 1, "#" + memo);
			}
		}
	}

	public void setTitle(String title) {
		String tmp = this.get(0) + "";
		if (tmp == null || tmp.length() == 0)
			tmp = "";
		else
			tmp = tmp.substring(0, 1);

		if ("#".equals(tmp))
			this.set(0, "#" + title);
		else {
			this.add(0, "");
			this.add(0, "#" + title);
		}
	}

	/**
	 * get the value of a key
	 * 
	 * @param key
	 * @return
	 */
	public String getProperties(String key) {
		return getProperties(key, "");
	}

	public String getProperties(String key, String defaultStr) {
		String tmp, ret;
		try {
			for (int i = 0; i < size(); i++) {
				tmp = get(i) + "";
				tmp = new String(tmp.getBytes("iso8859_1"), DEF_ENCODING);
				if (tmp.indexOf(key) == 0) {
					ret = tmp.substring(key.length() + 1);
					return ret;
				}
			}
		} catch (Exception e) {
		}
		return defaultStr;
	}

	public String getFileName() {
		return fileName;
	}

	private void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public static void main(String[] args) throws Exception {
		String path = "F:\\eclipse\\workspace\\csims_src\\src\\resources\\hibernate.properties";
		PropertiesUtil pro = new PropertiesUtil(path, "utf-8");
		pro.setProperties("hibernate.connection.password", "zhangwei8101");
		pro.save();
		// System.out.println(pro.read());
		pro = null;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
