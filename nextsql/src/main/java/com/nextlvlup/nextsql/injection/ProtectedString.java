package com.nextlvlup.nextsql.injection;

public class ProtectedString {
	
	private String string;
	
	public ProtectedString(Object string) {
		if(string instanceof String) {
			this.string = ((String)string).replaceAll("'", "");
		}else {
			this.string = new ProtectedString(string.toString()).a();
		}
	}
	
	public String a() {
		return string;
	}

}
