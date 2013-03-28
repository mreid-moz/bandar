package com.mozilla.main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class QuickTest {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		JSONArray list1 = new JSONArray();
		JSONObject obj =new JSONObject();
		obj.put("date","2013-01-01");
		obj.put("perc","0.0034");
		list1.add(obj);
		obj.put("date", "2013-02-03");
		obj.put("perc", "0.9");
		
		list1.add(obj);
		
		System.out.print(obj.toString());
	}

}
