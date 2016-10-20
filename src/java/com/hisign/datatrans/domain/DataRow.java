package com.hisign.datatrans.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

/**
 * @author lin
 */
public class DataRow implements java.io.Serializable {

	public static final long serialVersionUID = -1404822103401105211L;

	// 行表基本信息
	private TableInfo tableInfo = new TableInfo();

	/*
	 * 存储 表一行数据;
	 */
	private Map columnData = new HashMap<String, Object>();

	/*
	 * 添加一个字段数据
	 */
	public void addColumnData(String columnName, Object value) {
		this.columnData.put(columnName, value);
	}

	public Object getColumnData(String columnName) {
		return this.columnData.get(columnName);
	}
	
	private boolean isPicture = false;
	private boolean isAttachment = false;
    private String category;

	// 初始化某表某行记录信息
	public boolean initDataRow(ResultSet rs) throws SQLException, IOException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			String columnName = metaData.getColumnName(i+1);
			int columnType = metaData.getColumnType(i+1);
			Object columnValue = null;
			// 存放列类型
			this.getTableInfo().addColumnType(columnName, columnType);
			this.getTableInfo().addColumn(columnName);
			
			if (columnType == Types.BLOB) {
				Blob blob = rs.getBlob(columnName);
				
				if (null != blob) {
					InputStream inputStream = null;
					try {
						inputStream = blob.getBinaryStream();
						columnValue = IOUtils.toByteArray(inputStream);
					} catch (SQLException e) {
						throw e;
					} catch (IOException e) {
						throw e;
					} finally {
						if (null != inputStream) {
							try {
								inputStream.close();
							} catch (IOException e) {
							}
						}
					}
				}
				
				this.getTableInfo().setTableType(1);
				this.getTableInfo().setBlobClobColumnName(columnName);
			} else if (columnType == Types.CLOB) {
				Reader r = null;
				BufferedReader br = null;
				
				Clob clob = rs.getClob(columnName);
				if (null != clob) {
					String tmp = null;
					String content = "";
					
					try {
						r = clob.getCharacterStream();
						br = new BufferedReader(r);
						tmp = br.readLine();
						while (tmp != null) {
							content += tmp + "\r\n";
							tmp = br.readLine();
						}
						columnValue = content;
					} catch (SQLException e) {
						throw e;
					} catch (IOException e) {
						throw e;
					} finally {
						if (null != r) {
							try {
								r.close();
							} catch (IOException e) {
							}
						}
						if (null != br) {
							try {
								br.close();
							} catch (IOException e) {
							}
						}
					}
				}
				
				this.getTableInfo().setTableType(2);
				this.getTableInfo().setBlobClobColumnName(columnName);
			} else if (columnType == Types.DATE || columnType == Types.TIME || columnType == Types.TIMESTAMP) {
				columnValue = rs.getTimestamp(columnName);
			}else {
				columnValue = rs.getObject(columnName);
			}
			this.addColumnData(columnName, columnValue);
		}
		return true;
	}
	
	public String getDeleteSQL() {
		StringBuffer sql = new StringBuffer("DELETE FROM ");
		String tableName = this.getTableInfo().getTableName();
		String uniqueKey = this.getTableInfo().getUniqueKey();
		sql.append(tableName).append(" WHERE ").append(uniqueKey).append("='").append(this.getColumnData(uniqueKey)).append("'");
		return sql.toString();
	}
	
	public String getInsertSQL() {
		StringBuffer columnsName = new StringBuffer("INSERT INTO ").append(tableInfo.getTableName()).append("(");
		StringBuffer columnsValue = new StringBuffer(") VALUES (");
		for (int i = 0; i < tableInfo.getColumns().size(); i++) {
			String columnName = tableInfo.getColumns().get(i);
			Integer columnType = (Integer) tableInfo.getColumnType(columnName);
			columnsName.append(columnName + ","); 
			columnsValue.append(converColumnValue(columnType, this.getColumnData(columnName)) + ","); 
		}
		return columnsName.substring(0, columnsName.length() - 1) + columnsValue.substring(0, columnsValue.length() - 1) + ")";
	}
	
	public String converColumnValue(int type, Object value) {
		String temp = "";
		if (value == null) {
			temp = "null";
		} else {
			if ((type == Types.CHAR) || (type == Types.VARCHAR)) {
				temp = "'" + String.valueOf(value).replace("'", "''") + "'";
			} else if ((type == Types.DOUBLE) || (type == Types.FLOAT) || (type == Types.INTEGER) || (type == Types.NUMERIC)) {
				temp = String.valueOf(value);
			} else if ((type == Types.DATE) || (type == Types.TIME) || (type == Types.TIMESTAMP)) {
				if (String.valueOf(value).length() >= 19) {
					temp = "to_date('" + String.valueOf(value).substring(0, 19) + "','yyyy-mm-dd HH24:MI:SS')";
				} else if (String.valueOf(value).length() >= 16) {
					temp = "to_date('" + String.valueOf(value).substring(0, 16) + "','yyyy-mm-dd HH24:MI')";
				} else if (String.valueOf(value).length() >= 13) {
					temp = "to_date('" + String.valueOf(value).substring(0, 13) + "','yyyy-mm-dd HH24')";
				} else {
					temp = "to_date('" + String.valueOf(value).substring(0, 10) + "','yyyy-mm-dd')";
				}
			} else if (type == Types.BLOB) {
				temp = "empty_blob()";
			} else if (type == Types.CLOB) {
				temp = "empty_clob()";
			}
		}
		return temp;
	}
	

	public TableInfo getTableInfo() {
		return tableInfo;
	}

	public void setTableInfo(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}

	public Map getColumnData() {
		return columnData;
	}

	public void setColumnData(Map columnData) {
		this.columnData = columnData;
	}

	public boolean isPicture() {
		return isPicture;
	}

	public void setPicture(boolean isPicture) {
		this.isPicture = isPicture;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isAttachment() {
		return isAttachment;
	}

	public void setAttachment(boolean isAttachment) {
		this.isAttachment = isAttachment;
	}

}
