package com.mozilla.bandar.json;

import org.apache.commons.lang3.StringUtils;

public class ErrorJson {
	
	public String invalidArgumentResponse(String param, String value, String message) {
		if (StringUtils.isBlank(value)) {
			value = "null";
		}
		String jsonString = "{\"error\": {\"param\": \"" + param + "\",\"value\": \"" + value + "\", \"message\": \"" + message + "\"}}";

		return jsonString;
	}
}
