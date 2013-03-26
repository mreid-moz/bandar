package com.mozilla.bandar.enumeration;

public enum TimeFrame {
	DAILY("D"), WEEKLY("W"), MONTHLY("M"), YEARLY("Y");
	private String intervalCode;
	
	private TimeFrame(String s) {
		intervalCode = s;
	}
 
	public String getIntervalCode() {
		return intervalCode;
	}	
	
	
}
