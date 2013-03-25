package com.mozilla.bandar.dnt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import com.mozilla.bandar.interval.DeviceType;
import com.mozilla.bandar.interval.TimeFrame;

public class Data {

	private String deviceType;
	private String intervalCode;
	private Map<Date, Float> dailyNumbers = new HashMap<Date, Float>();
	private String jsonData;

	public Data(String deviceType, String intervalCode) {
		this.deviceType = deviceType;
		this.intervalCode = intervalCode;
		if (TimeFrame.valueOf(intervalCode).equals(TimeFrame.DAILY)) {
			if (DeviceType.valueOf(deviceType).equals(DeviceType.DESKTOP)) {
				readDesktopDailyNumbers(deviceType);
			}
		}

	}
	
	public String displayDailyNumbers(String deviceType) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Date, Float> d : dailyNumbers.entrySet()) {
			sb.append(d.getKey() + "-" + d.getValue());
		}
		return sb.toString();
	}
	
	private void readDesktopDailyNumbers(String deviceType) {
		this.dailyNumbers = readMockDailyData(deviceType);
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public Map<Date, Float> getDailyNumbers() {
		return dailyNumbers;
	}

	public void setDailyNumbers(Map<Date, Float> dailyNumbers) {
		this.dailyNumbers = dailyNumbers;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	protected HashMap<Date, Float> readMockDailyData(String deviceType) {
		HashMap<Date, Float> mockData = new HashMap<Date, Float>();
		try {
			String dt = "2013-01-01";  // Start date
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(dt));
			Random r = new Random();
			int Low = 0;
			int High = 20;		
			for (int i = 0; i < 15; i++) {
				c.add(Calendar.DATE, 1);  // number of days to add
				dt = sdf.format(c.getTime());  // dt is now the new date			
				mockData.put(new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(dt), new Float(r.nextInt(High-Low) + Low));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mockData;
	}

	public String getIntervalCode() {
		return intervalCode;
	}

	public void setIntervalCode(String intervalCode) {
		this.intervalCode = intervalCode;
	}
}
