package com.hisign.datatrans.domain;

import java.util.Comparator;

public class Config implements Comparator {

	private String key;
	private String value;
	private String name;
	private String index;
	private String valueLength;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public int compare(Object o1, Object o2) {
		Config c1 = (Config) o1;
		Config c2 = (Config) o2;
		return c1.getIndex().compareTo(c2.getIndex());
	}
	public String getValueLength() {
		return valueLength;
	}
	public void setValueLength(String valueLength) {
		this.valueLength = valueLength;
	}
	
}
