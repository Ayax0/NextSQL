package com.nextlvlup.nextsql.statement;

import java.sql.Timestamp;
import java.util.HashMap;

import com.nextlvlup.nextsql.injection.ProtectedString;

public class DeleteStatement extends Statement {
	
	private String table;
	
	private HashMap<String, String> fields = new HashMap<String, String>();
	
	public DeleteStatement(String table) {
		this.table = table;
	}
	
	public void addCondition(String key, Object value) {
		if(value instanceof String || value instanceof Timestamp) {
			fields.put(key, "'" + new ProtectedString(value).a() + "'");
		}else {
			fields.put(key, new ProtectedString(value).a());
		}
	}
	
	public String getStatement() {
		String statement = "DELETE FROM " + table + " WHERE ";
		String condition = "";
		for(String key : fields.keySet()) {
			condition += key + " = " + fields.get(key) + " AND ";
		}
		
		condition = condition.substring(0, condition.length() - 5);
		
		return statement + condition;
	}

}
