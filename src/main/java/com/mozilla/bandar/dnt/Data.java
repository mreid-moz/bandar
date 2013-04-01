package com.mozilla.bandar.dnt;

import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozilla.bandar.constant.Constants;
import com.mozilla.bandar.enumeration.DeviceType;
import com.mozilla.bandar.enumeration.GeoType;
import com.mozilla.bandar.enumeration.TimeFrame;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Data {
	private static final Logger LOGGER = LoggerFactory.getLogger(Data.class);
	private String deviceType;
	private String intervalCode;
	private LinkedHashMap<String, LinkedHashMap<String, String>> dailyDesktopNumbers = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	private LinkedHashMap<String, LinkedHashMap<String, String>> weeklyDesktopNumbers = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	private LinkedHashMap<String, LinkedHashMap<String, String>> dailyMobileNumbers = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	private LinkedHashMap<String, LinkedHashMap<String, String>> weeklyMobileNumbers = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	private LinkedHashMap<String, LinkedHashMap<String, String>> dailyUSDesktopNumbers = new LinkedHashMap<String, LinkedHashMap<String, String>>();

	
	private String jsonData;

	public Data() {

		LOGGER.debug("INFO: INVOKING DATA CLASS");


	}

	public void readData(String deviceType, String intervalCode, String geo, String baseFilePath) {
		if (TimeFrame.valueOf(intervalCode).equals(TimeFrame.DAILY)) {
			if (DeviceType.valueOf(deviceType).equals(DeviceType.DESKTOP)) {
				if (GeoType.valueOf(geo).equals(GeoType.AGGREGATE)) {
					LOGGER.debug("Loading data for deviceType: " + deviceType + " TimeFrame: " + intervalCode + " GeoType: " + geo);
					this.dailyDesktopNumbers = readDntNumbers(baseFilePath + Constants.DNT_DESKTOP_AGGREGATE_DAILY_FILE);
				}
				if (GeoType.valueOf(geo).equals(GeoType.US_STATES)) {
					LOGGER.debug("Loading data for deviceType: " + deviceType + " TimeFrame: " + intervalCode + " GeoType: " + geo);
					//this.dailyUSDesktopNumbers = readDntNumbers(baseFilePath + Constants.DNT_DESKTOP_AGGREGATE_DAILY_FILE);
				}
				
				
				
			}
			if (DeviceType.valueOf(deviceType).equals(DeviceType.MOBILE)) {
				if (GeoType.valueOf(geo).equals(GeoType.AGGREGATE)) {
					LOGGER.debug("Loading data for deviceType: " + deviceType + " TimeFrame: " + intervalCode);
					this.dailyMobileNumbers = readDntNumbers(baseFilePath + Constants.DNT_MOBILE_AGGREGATE_DAILY_FILE);
				}
			}
		}
		if (TimeFrame.valueOf(intervalCode).equals(TimeFrame.WEEKLY)) {
			if (DeviceType.valueOf(deviceType).equals(DeviceType.DESKTOP)) {
				LOGGER.debug("Loading data for deviceType: " + deviceType + " TimeFrame: " + intervalCode);
				this.weeklyDesktopNumbers = readDntNumbers(baseFilePath + Constants.DNT_DESKTOP_AGGREGATE_WEEKLY_FILE);
			}
			if (DeviceType.valueOf(deviceType).equals(DeviceType.DESKTOP)) {
				LOGGER.debug("Loading data for deviceType: " + deviceType + " TimeFrame: " + intervalCode);
				this.weeklyMobileNumbers = readDntNumbers(baseFilePath + Constants.DNT_MOBILE_AGGREGATE_WEEKLY_FILE);
			}
		}

	}


	@SuppressWarnings("unchecked")
	public String displayNumbers(String deviceType, String intervalCode) {
		Map<String, LinkedHashMap<String, String>> numbers = new LinkedHashMap<String, LinkedHashMap<String, String>>();

		if (TimeFrame.valueOf(intervalCode).equals(TimeFrame.DAILY)) {
			if (DeviceType.valueOf(deviceType).equals(DeviceType.DESKTOP)) {
				//lets shallow copy for outputting data
				numbers = new LinkedHashMap<String, LinkedHashMap<String, String>>(dailyDesktopNumbers); 
			}
			if (DeviceType.valueOf(deviceType).equals(DeviceType.MOBILE)) {
				//lets shallow copy for outputting data
				numbers = new LinkedHashMap<String, LinkedHashMap<String, String>>(dailyMobileNumbers); 
			}
		}
		if (TimeFrame.valueOf(intervalCode).equals(TimeFrame.WEEKLY)) {
			if (DeviceType.valueOf(deviceType).equals(DeviceType.DESKTOP)) {
				//lets shallow copy for outputting data
				numbers = new LinkedHashMap<String, LinkedHashMap<String, String>>(weeklyDesktopNumbers); 
			}
			if (DeviceType.valueOf(deviceType).equals(DeviceType.MOBILE)) {
				//lets shallow copy for outputting data
				numbers = new LinkedHashMap<String, LinkedHashMap<String, String>>(weeklyMobileNumbers); 
			}

		}

		ObjectMapper mapper = new ObjectMapper();
		List<Object> list = new ArrayList<Object>();

		Map<String, Object> userInMap = new HashMap<String, Object>();

		Map<String, Object> boo = new HashMap<String, Object>();
		
		for (Entry<String, LinkedHashMap<String, String>> dd : numbers.entrySet()) {
			String p = dd.getKey();
			System.out.println("DAA: " + p);
			list = new ArrayList<Object>();
			for (Entry<String, String> d : dd.getValue().entrySet()) {
				boo = new HashMap<String, Object>();				
				boo.put("date", d.getKey());
				boo.put("perc", d.getValue());
				list.add(boo);
			}
			
			userInMap.put(p, list);

			
		}

		try {
			return mapper.writeValueAsString(userInMap);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "ERROR";
	}

	private LinkedHashMap<String, LinkedHashMap<String, String>> readDntNumbers(String filePath) {
		return readDataFromFile(filePath);
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}


	protected LinkedHashMap<String, LinkedHashMap<String, String>> readDataFromFile(String filePath) {
		LinkedHashMap<String, LinkedHashMap<String, String>> data = new LinkedHashMap<String, LinkedHashMap<String,String>>();
		
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
				LinkedHashMap<String, String> d = new LinkedHashMap<String, String>();
				if (data.containsKey(splitTab[1])) {
					d = data.get(splitTab[1]);
				}
				d.put(splitTab[0], splitTab[2]);
				data.put(splitTab[1], d);
			}
			//Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
			LOGGER.error("Error reading file: " + filePath);
		}


		return data;
	}

	public String getIntervalCode() {
		return intervalCode;
	}

	public void setIntervalCode(String intervalCode) {
		this.intervalCode = intervalCode;
	}
}
