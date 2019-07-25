package com.nextlvlup.nextsql.database;

import java.util.HashMap;

public class DatabasePool {
	
	private String HOST;
	private int PORT;
	private String USERNAME;
	private String PASSWORD;
	
	protected HashMap<String, DatabaseConnection> CONNECTIONS = new HashMap<String, DatabaseConnection>();
	
	public DatabasePool(String host, int port, String username, String password) {
		this.HOST = host;
		this.PORT = port;
		this.USERNAME = username;
		this.PASSWORD = password;
	}
	
	public String getHost() {
		return HOST;
	}
	
	public int getPort() {
		return PORT;
	}
	
	public String getUsername() {
		return USERNAME;
	}
	
	protected String getPassword() {
		return PASSWORD;
	}
	
	public DatabaseConnection getConnection(String database) {
		if(CONNECTIONS.containsKey(database)) return CONNECTIONS.get(database);
		return new DatabaseConnection(this, database);
	}
	
	public void destroy() {
		for(DatabaseConnection con : CONNECTIONS.values()) {
			con.close();
		}
	}

}
