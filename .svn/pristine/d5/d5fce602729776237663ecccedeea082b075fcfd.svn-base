/**
 * added by zhangwei 2005-08-24
 */
package com.hisign.datatrans.utils;


/**
 * @author fabianofranz@users.sourceforge.net
 *
 * A singleton responsible to create valid uuid identifiers.
 */
public class UUIDService {

	private static final UUIDService instance = new UUIDService();

	static public UUIDService getInstance() {
		return instance;	
	}
	
	public String simpleHex(String separator) {
		return (String) new UUIDHexGenerator(separator).generate();
	}

	public String simpleHex() {
		return (String) new UUIDHexGenerator().generate();
	}
}
