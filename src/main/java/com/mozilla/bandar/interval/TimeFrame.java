package com.mozilla.bandar.interval;

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
