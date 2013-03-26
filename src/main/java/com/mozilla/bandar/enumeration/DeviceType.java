package com.mozilla.bandar.enumeration;

public enum DeviceType {
	DESKTOP("DESKTOP"), MOBILE("MOBILE");
	private String deviceCode;
	
	private DeviceType(String s) {
		deviceCode = s;
	}
 
	public String getIntervalCode() {
		return deviceCode;
	}	
	
	
}
