package com.nextlvlup.nextsql.statement;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nextlvlup.nextsql.injection.ProtectedString;

public class SelectStatement extends Statement {
	
	private String table;
	
	private List<String> fields = new ArrayList<String>();
	private HashMap<String, String> conditions = new HashMap<String, String>();
	
	public SelectStatement(String table) {
		this.table = table;
	}
	
	public void addFieldSelection(String field) {
			fields.add(new ProtectedString(field).a());
	}
	
	public void addCondition(String key, Object value) {
		if(value instanceof String || value instanceof Timestamp) {
			conditions.put(key, "'" + new ProtectedString(value).a() + "'");
		}else {
			conditions.put(key, new ProtectedString(value).a());
		}
	}
	
	public String getStatement() {
		String statement = "SELECT ";
		String condition = "";
		if(fields.isEmpty()) {
			statement += "*";
		}else {
			for(String field : fields) {
				statement += field + ",";
			}
			statement = statement.substring(0, statement.length() - 1);
		}
		statement+= " FROM " + table;
		
		if(!conditions.isEmpty()) {
			statement+= " WHERE ";
			for(String key : conditions.keySet()) {
				condition += key + " = " + conditions.get(key) + " AND ";
			}
			condition = condition.substring(0, condition.length() - 5);
		}
		
		return statement + condition;
	}

}
