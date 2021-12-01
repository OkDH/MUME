/**
 * 
 */
package com.ocko.aventador.constant;

/**
 * @author ok
 *
 */
public enum InfiniteType {

	v1("v1"),
	v2("v2"),
	v2_1("v2.1");
	
	private String versionName;
	InfiniteType(String versionName) {
		this.versionName = versionName;
	}
	
	public String versionName() {
		return versionName;
	}
}
