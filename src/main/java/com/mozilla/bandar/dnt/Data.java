package com.mozilla.bandar.dnt;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozilla.bandar.constant.Constants;
import com.mozilla.bandar.enumeration.DeviceType;
import com.mozilla.bandar.enumeration.TimeFrame;

public class Data {
	private static final Logger LOGGER = LoggerFactory.getLogger(Data.class);
	private String deviceType;
	private String intervalCode;
	private Map<String, String> dailyDesktopNumbers = new LinkedHashMap<String, String>();
	private Map<String, String> weeklyDesktopNumbers = new LinkedHashMap<String, String>();
	private Map<String, String> dailyMobileNumbers = new LinkedHashMap<String, String>();
	private Map<String, String> weeklyMobileNumbers = new LinkedHashMap<String, String>();
	
	private String jsonData;

	public Data() {

		LOGGER.debug("INFO: INVOKING DATA CLASS");


	}

	public void readData(String deviceType, String intervalCode, String baseFilePath) {
		if (TimeFrame.valueOf(intervalCode).equals(TimeFrame.DAILY)) {
			if (DeviceType.valueOf(deviceType).equals(DeviceType.DESKTOP)) {
				LOGGER.debug("Loading data for deviceType: " + deviceType + " TimeFrame: " + intervalCode);
				this.dailyDesktopNumbers = readDntNumbers(baseFilePath + Constants.DNT_DESKTOP_DAILY_FILE);
			}
			if (DeviceType.valueOf(deviceType).equals(DeviceType.MOBILE)) {
				LOGGER.debug("Loading data for deviceType: " + deviceType + " TimeFrame: " + intervalCode);
				this.dailyMobileNumbers = readDntNumbers(baseFilePath + Constants.DNT_MOBILE_DAILY_FILE);
			}
		}
		if (TimeFrame.valueOf(intervalCode).equals(TimeFrame.WEEKLY)) {
			if (DeviceType.valueOf(deviceType).equals(DeviceType.DESKTOP)) {
				LOGGER.debug("Loading data for deviceType: " + deviceType + " TimeFrame: " + intervalCode);
				this.weeklyDesktopNumbers = readDntNumbers(baseFilePath + Constants.DNT_DESKTOP_WEEKLY_FILE);
			}
			if (DeviceType.valueOf(deviceType).equals(DeviceType.DESKTOP)) {
				LOGGER.debug("Loading data for deviceType: " + deviceType + " TimeFrame: " + intervalCode);
				this.weeklyMobileNumbers = readDntNumbers(baseFilePath + Constants.DNT_MOBILE_WEEKLY_FILE);
			}
		}

	}


	@SuppressWarnings("unchecked")
	public String displayNumbers(String deviceType, String intervalCode) {
		JSONArray data_list = new JSONArray();
		JSONObject obj;
		Map<String, String> numbers = new LinkedHashMap<String, String>();
		
		if (TimeFrame.valueOf(intervalCode).equals(TimeFrame.DAILY)) {
			if (DeviceType.valueOf(deviceType).equals(DeviceType.DESKTOP)) {
				//lets shallow copy for outputting data
				numbers = new LinkedHashMap<String, String>(dailyDesktopNumbers); 
			}
			if (DeviceType.valueOf(deviceType).equals(DeviceType.MOBILE)) {
				//lets shallow copy for outputting data
				numbers = new LinkedHashMap<String, String>(dailyMobileNumbers); 
			}
		}
		if (TimeFrame.valueOf(intervalCode).equals(TimeFrame.WEEKLY)) {
			if (DeviceType.valueOf(deviceType).equals(DeviceType.DESKTOP)) {
				//lets shallow copy for outputting data
				numbers = new LinkedHashMap<String, String>(weeklyDesktopNumbers); 
			}
			if (DeviceType.valueOf(deviceType).equals(DeviceType.MOBILE)) {
				//lets shallow copy for outputting data
				numbers = new LinkedHashMap<String, String>(weeklyMobileNumbers); 
			}
			
		}
		
		for (Map.Entry<String, String> d : numbers.entrySet()) {
			obj  = new JSONObject();;
			obj.put(Constants.JSON_DNT_KEY_DATE, d.getKey());
			obj.put(Constants.JSON_DNT_KEY_PERCENTAGE, d.getValue());
			data_list.add(obj);
		}
		
		return data_list.toJSONString();
	}

	private LinkedHashMap<String, String> readDntNumbers(String filePath) {
		return readDataFromFile(filePath);
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public Map<String, String> getDailyDesktopNumbers() {
		return dailyDesktopNumbers;
	}

	public void setDailyDesktopNumbers(Map<String, String> dailyDesktopNumbers) {
		this.dailyDesktopNumbers = dailyDesktopNumbers;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}


	protected LinkedHashMap<String, String> readDataFromFile(String filePath) {
		LinkedHashMap<String, String> dailyDNT = new LinkedHashMap<String, String>();
		try{
			// Open the file that is the first 
			FileInputStream fstream = new FileInputStream(filePath);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				String[] splitTab = strLine.split("\t");
				dailyDNT.put(splitTab[0], splitTab[1]);
			}
			//Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
			LOGGER.error("Unable to read daily file", e.getMessage());
		}


		return dailyDNT;
	}

	public String getIntervalCode() {
		return intervalCode;
	}

	public void setIntervalCode(String intervalCode) {
		this.intervalCode = intervalCode;
	}
}
