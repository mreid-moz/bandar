package com.mozilla.main;

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

import org.apache.commons.collections.keyvalue.MultiKey;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class QuickTest {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		LinkedHashMap<String, LinkedHashMap<String, String>> data = new LinkedHashMap<String, LinkedHashMap<String,String>>();
		
		try{
			// Open the file that is the first 
			FileInputStream fstream = new FileInputStream("data/desktop.aggregate.daily.txt");
			
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
			System.err.println("Unable to read daily file: " + e.getMessage());
		}
		
		
		

		
		
		ObjectMapper mapper = new ObjectMapper();
		List<Object> list = new ArrayList<Object>();

		Map<String, Object> userInMap = new HashMap<String, Object>();

		Map<String, Object> boo = new HashMap<String, Object>();
		
		for (Entry<String, LinkedHashMap<String, String>> dd : data.entrySet()) {
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
		
//
//		boo.put("name", "mkyong");
//		boo.put("age", 29);
//		list.add(boo);
//		
//		boo = new HashMap<String, Object>();
//		boo.put("name", "jim");
//		boo.put("age", 39);		
//		list.add(boo);
//
//		userInMap.put("GLOBAL", list);
//		userInMap.put("US", list);
//

		try {
			System.err.println(mapper.writeValueAsString(userInMap));
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





	}

}
