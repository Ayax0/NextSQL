package com.nextlvlup.nextsql.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import com.nextlvlup.nextsql.statement.SelectStatement;
import com.nextlvlup.nextsql.statement.Statement;

public class DatabaseConnection {
	
	private String DATABASE;
	private DatabasePool INSTANCE;
	private Connection CONNECTION;
	
	private TimerTask TIMEOUT;
	
	public DatabaseConnection(DatabasePool instance, String database) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			DATABASE = database;
			INSTANCE = instance;
			CONNECTION = DriverManager.getConnection("jdbc:mysql://" + 
					instance.getHost() + ":" + 
					instance.getPort() + "/" + 
					database, 
					instance.getUsername(), 
					instance.getPassword());
			
			INSTANCE.CONNECTIONS.put(database, this);
			resetTimeout();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void resetTimeout() {
		if(TIMEOUT != null) TIMEOUT.cancel();
		TIMEOUT = new TimerTask() {
			
			@Override
			public void run() {
				TIMEOUT = null;
				close();
			}
		};
		
		new Timer().schedule(TIMEOUT, 1000 * 60 * 5);
	}
	
	public void close(){
		try {
			if(CONNECTION != null) {
				if(CONNECTION.isValid(2000)) {
					CONNECTION.close();
					INSTANCE.CONNECTIONS.remove(DATABASE);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Result
	
	public ResultSet getResult(PreparedStatement ps){
		try {
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				return rs;
			}
			resetTimeout();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet getResult(String query){
		return getResult(getPreparedStatement(query));
	}
	
	public ResultSet getResult(SelectStatement statement){
		return getResult(statement.getStatement());
	}
	
	//Execute
	
	public void execute(PreparedStatement ps){
		try {
			ps.executeUpdate();
			resetTimeout();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void execute(String query){
		execute(getPreparedStatement(query));
	}
	
	public void execute(Statement statement){
		execute(statement.getStatement());
	}
	
	//Contains
	
		public boolean contains(String table, String key, Object value) {
			SelectStatement statement = new SelectStatement(table);
			statement.addCondition(key, value);
			
			ResultSet rs = getResult(statement);
			 if(rs!= null) {
				 return true ;
			 }else {
				 return false ;
			 }
		}
	
	//Statement
	
	public PreparedStatement getPreparedStatement(String query) {
		try {
			return CONNECTION.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
