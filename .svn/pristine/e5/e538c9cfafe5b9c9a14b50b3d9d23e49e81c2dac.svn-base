package com.hisign.datatrans.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class BaseAction {
	/**
	 * 日志
	 */
	public Logger log = Logger.getLogger(this.getClass());
	/**
	 * request session
	 */
	public HttpServletRequest request = ServletActionContext.getRequest();
	public HttpServletResponse response = ServletActionContext.getResponse();
	public String webRoot = request.getContextPath();

	/**
	 * 默认跳转URL
	 */
	public final static String DEFAULT = "default";

	private String viewpath;

	/**
	 * Redirect方式跳转URL
	 * 
	 * @param vpath
	 * @return
	 */
	public String redirect(String vpath) {
		viewpath = vpath;
		return "redirect";
	}

	public String getViewpath() {
		return viewpath;
	}

	/**
	 * 设置响应内容
	 * 
	 * @param obj
	 * @throws IOException
	 */
	public void setResponse(Object obj) throws IOException {
		ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
		PrintWriter out = ServletActionContext.getResponse().getWriter();
		out.print(obj);
		out.flush();
		out.close();
	}
}
