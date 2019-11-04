package com.nextlvlup.nextsql.statement;

import java.sql.Timestamp;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nextlvlup.nextsql.injection.ProtectedString;

public class InsertStatement extends Statement {
	
	private String table;
	
	private HashMap<String, String> fields = new HashMap<String, String>();
	
	public InsertStatement(String table) {
		this.table = table;
	}
	
	public void addValue(String key, Object value) {
		if(value instanceof String || 
				value instanceof Timestamp || 
				value instanceof JSONObject ||
				value instanceof JSONArray) {
			fields.put(key, "'" + new ProtectedString(value).a() + "'");
		}else {
			fields.put(key, new ProtectedString(value).a());
		}
	}
	
	public void addValueIfNotNull(String key, Object value) {
		if(value instanceof String) {
			if(value != null && value != "") addValue(key, value);
		} 
		else if(value instanceof Integer) {
			if(value != null && (int) value != 0) addValue(key, value);
		}
		else if(value instanceof Double) {
			if(value != null && (double) value != 0) addValue(key, value);
		}
		else if(value instanceof Float) {
			if(value != null && (float) value != 0) addValue(key, value);
		}
		else {
			if(value != null) addValue(key, value);
		}
	}
	
	public String getStatement() {
		String statement = "INSERT INTO " + table + " (";
		String keys = "";
		String values = ") VALUES (";
		
		if(fields.size() == 0) return "INSERT INTO " + table + " VALUES ()";
		
		for(String key : fields.keySet()) {
			keys += key + ",";
			values += fields.get(key) + ",";
		}
		
		keys = keys.substring(0, keys.length() - 1);
		values = values.substring(0, values.length() - 1);
		
		values+= ")";
		
		return statement + keys + values;
	}

}
