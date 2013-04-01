package com.mozilla.bandar.enumeration;


public enum GeoType {
	GLOBAL("GLOBAL"), US_STATES("US_STATES"), AGGREGATE("AGGREGATE");
	private String deviceCode;
	
	private GeoType(String s) {
		deviceCode = s;
	}
 
	public String getIntervalCode() {
		return deviceCode;
	}	
	
	
}
