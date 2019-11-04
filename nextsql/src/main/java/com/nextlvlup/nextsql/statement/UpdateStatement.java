package com.nextlvlup.nextsql.statement;

import java.sql.Timestamp;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nextlvlup.nextsql.injection.ProtectedString;

public class UpdateStatement extends Statement{
	
	private String table;
	
	private HashMap<String, String> fields = new HashMap<String, String>();
	private HashMap<String, String> conditions = new HashMap<String, String>();
	
	public UpdateStatement(String table) {
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
		} else {
			if(value != null) addValue(key, value);
		}
	}
	
	public void addCondition(String key, Object value) {
		if(value instanceof String) {
			conditions.put(key, "'" + new ProtectedString(value).a() + "'");
		}else {
			conditions.put(key, new ProtectedString(value).a());
		}
	}
	
	public String getStatement() {
		String statement = "UPDATE " + table + " SET";
		String update = "";
		String condition = "";
		if(!fields.isEmpty()) {
			for(String key : fields.keySet()) {
				update += " " + key + " = " + fields.get(key) + ",";
			}
			update = update.substring(0, update.length() - 1);
		}
		
		if(!conditions.isEmpty()) {
			condition += " WHERE";
			for(String con : conditions.keySet()) {
				condition += " " + con + " = " + conditions.get(con) + " AND";
			}
			condition = condition.substring(0, condition.length() - 4);
		}
		
		return statement + update + condition;
	}

}
