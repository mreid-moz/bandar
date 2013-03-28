package com.mozilla.bandar.dnt;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
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
	private Map<String, String> dailyNumbers = new LinkedHashMap<String, String>();
	private String jsonData;
	private String baseFilePath;

	public Data(String deviceType, String intervalCode, String baseFilePath) {
		this.deviceType = deviceType;
		this.intervalCode = intervalCode;
		this.baseFilePath = baseFilePath;

		LOGGER.debug("INFO: INVOKING DATA CLASS");

		if (TimeFrame.valueOf(intervalCode).equals(TimeFrame.DAILY)) {
			if (DeviceType.valueOf(deviceType).equals(DeviceType.DESKTOP)) {

				LOGGER.debug("Loading data for deviceType: " + deviceType + " TimeFrame: " + intervalCode);
				readDesktopDailyNumbers(deviceType);
			}
		}

	}


	@SuppressWarnings("unchecked")
	public String displayDailyNumbers(String deviceType) {
		JSONArray data_list = new JSONArray();
		JSONObject obj;
		for (Map.Entry<String, String> d : dailyNumbers.entrySet()) {
			obj  = new JSONObject();;
			obj.put("date", d.getKey());
			obj.put("perc", d.getValue());
			data_list.add(obj);
		}
		return data_list.toJSONString();
	}

	private void readDesktopDailyNumbers(String deviceType) {
		this.dailyNumbers = readDataFromFile(deviceType, baseFilePath + Constants.DNT_DESKTOP_DAILY_FILE);
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public Map<String, String> getDailyNumbers() {
		return dailyNumbers;
	}

	public void setDailyNumbers(Map<String, String> dailyNumbers) {
		this.dailyNumbers = dailyNumbers;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}


	protected LinkedHashMap<String, String> readDataFromFile(String deviceType, String filePath) {
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
