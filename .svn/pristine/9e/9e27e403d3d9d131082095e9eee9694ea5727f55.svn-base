package com.hisign.datatrans.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author lin
 */
public class TableInfo implements java.io.Serializable {

	public static final long serialVersionUID = -8693822103401105211L;

	// 表名
	private String tableName = "";
	
	// 唯一约束
	private String uniqueKey;
	
	// 上报级别
	private String reportLevel;
	
	// 表种类 0代表普通表类型；1代表带有BLOB; 2 代表CLOB字段类型的；
	private int tableType=0;
	
	//
	private String blobClobColumnName="";
	
	// 所有字段名和类型
	private Map columnType = new HashMap<String, Object>();
	
	//存储列名列表
    private List<String> columns = new ArrayList<String>();
    
    public void addColumn(String columnName) {
    	this.columns.add(columnName);
    }
    
	public void addColumnType(String columnName, Object columnType) {
		this.columnType.put(columnName, columnType);
	}
	
	public Object getColumnType(String columnName) {
		return this.columnType.get(columnName);
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public String getReportLevel() {
		return reportLevel;
	}

	public void setReportLevel(String reportLevel) {
		this.reportLevel = reportLevel;
	}

	public int getTableType() {
		return tableType;
	}

	public void setTableType(int tableType) {
		this.tableType = tableType;
	}

	public String getBlobClobColumnName() {
		return blobClobColumnName;
	}

	public void setBlobClobColumnName(String blobClobColumnName) {
		this.blobClobColumnName = blobClobColumnName;
	}

	public List<String> getColumns() {
		return columns;
	}

	
}
