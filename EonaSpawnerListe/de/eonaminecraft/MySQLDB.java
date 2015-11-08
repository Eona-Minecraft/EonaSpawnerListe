package eonaminecraft;

import java.sql.*;
import java.util.logging.Logger;


public class MySQLDB {

	
	private Connection connection = null;
	private ConnectionData a = new ConnectionData();
	private boolean opened = false;
	public Logger l = null;
	
	
	public MySQLDB(Logger x){
		l = x;
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setConnectionData(ConnectionData x){
		a = x;
	}
	
	public void setConnectionData(String h, String u, String p, String n){
		ConnectionData x = new ConnectionData();
		x.setDbHost(h);
		x.setDbName(n);
		x.setDbPass(p);
		x.setDbUser(u);
		setConnectionData(x);
	}
	
	public void openConnections(){
		try{
			this.connection = DriverManager.getConnection("jdbc:mysql://" + a.getDbHost() + "/" + a.getDbName() + "?user=" + a.getDbUser() + "&password=" + a.getDbPass());
			opened = true;
		}catch(Exception e){
			l.info("Konnte keine Verbindung zur Datenbank aufbauen: ");
			l.info(e.getMessage());
		}
	}
	
	public boolean isOpened(){
		return opened;
	}
	
	public ResultSet query(String sql){
		if(isOpened()){
			try{
				return connection.createStatement().executeQuery(sql);
			}catch(Exception e){
				l.info("Konnte Abfrage nicht ausführen: " + sql );
				l.info(e.getMessage());
			}
		}else{
			l.info("Konnte Abfrage nicht ausführen: " + sql );
			l.info("Datenbank-Verbindung nicht offen!");
		}
		return null;
	}
	
	
	public int execute(String sql){
		if(isOpened()){
			try{
				return connection.createStatement().executeUpdate(sql);
			}catch(Exception e){
				l.info("Konnte Abfrage nicht ausführen: " + sql );
				l.info(e.getMessage());
			}
		}else{
			l.info("Konnte Abfrage nicht ausführen: " + sql );
			l.info("Datenbank-Verbindung nicht offen!");
		}
		return -1;
	}
	
	public void closeConnections(){
		if(isOpened()){
			try{
				connection.close();
				l.info("DB-Verbindung geschlossen");
			}catch(Exception e){
				l.info("Konnte DB-Verbindung nicht schließen:");
				l.info(e.getMessage());
			}
		}else{
			l.info("Konnte DB-Verbindung nicht schließen, da nicht geöffnet!");
		}
			
	}
	
	
	
	
	
}
