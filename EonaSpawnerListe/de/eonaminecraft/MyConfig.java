package eonaminecraft;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;

public class MyConfig {

	private String dbHost = "";
	private String dbName = "";
	private String dbUser = "";
	private String dbPass = "";
	private int spawnerpreis = 0;
	
	
	public String getDbHost() {
		return dbHost;
	}
	public String getDbName() {
		return dbName;
	}
	public String getDbUser() {
		return dbUser;
	}
	public String getDbPass() {
		return dbPass;
	}
	public int getSpawnerpreis() {
		return spawnerpreis;
	}
	
	
	public void readConfig(FileConfiguration x){
		dbHost = x.getString("dbhost");
		dbUser = x.getString("dbuser");
		dbPass = x.getString("dbpass");
		dbName = x.getString("dbname");
		spawnerpreis = x.getInt("preis");
	}
	
	
	public void showConfig(Logger l){
		l.info("Host: " + getDbHost());
		l.info("Name: " + getDbName());
 		l.info("User: " + getDbUser());
		l.info("Spawner-Preis: " + getSpawnerpreis());
	}
}
