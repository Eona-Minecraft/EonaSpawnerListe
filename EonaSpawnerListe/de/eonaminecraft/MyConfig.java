package eonaminecraft;

import java.util.logging.Logger;

/**
 * GER: Definiert mithilfe der Bukkit-Configuration eine eigene Configuration
 * ENG: Defines an own configuration object based on the bukkit configuration
 * @author Bloodrayne
 *
 */
public class MyConfig {

	/**
	 * GER: Pfad zum Datenbank-Host in der config.yml
	 * ENG: Path to the Value for Database-Host in the config.yml
	 */
	private final String path2Host = "storage.db.host";
	
	/**
	 * GER: Pfad zum Datenbank-User in der config.yml
	 * ENG: Path to the Value for Database-User in the config.yml
	 */
	private final String path2User = "storage.db.user";
	
	/**
	 * GER: Pfad zum Datenbank-Passwort in der config.yml
	 * ENG: Path to the Value for Database-Password in the config.yml
	 */
	private final String path2Pass = "storage.db.pass";
	
	/**
	 * GER: Pfad zum Datenbank-Name in der config.yml
	 * ENG: Path to the Value for Database-Name in the config.yml
	 */
	private final String path2Name = "storage.db.name";
	
	/**
	 * GER: Pfad zum Spawner-Limit in der config.yml
	 * ENG: Path to the Value for the maximal amount of spawners in the config.yml
	 */
	private final String path2SpawnerLimit = "options.maxSpawner";
	
	/**
	 * GER: Pfad zum Spawner-Preis in der config.yml
	 * ENG: Path to the Value for cost of one spawner in the config.yml
	 */
	private final String path2SpawnerPreis = "options.spawnerPreis";
	
	/**
	 * GER: Interne Variable für den Datenbank-Host
	 * ENG: internal variable for the database host
	 */
	private String dbHost = "";
	
	/**
	 * GER: Interne Variable für den Datenbank-Name
	 * ENG: internal variable for the database name
	 */
	private String dbName = "";
	
	/**
	 * GER: Interne Variable für den Datenbank-User
	 * ENG: internal variable for the database user
	 */
	private String dbUser = "";

	/**
	 * GER: Interne Variable für den Datenbank-Passwort
	 * ENG: internal variable for the database password
	 */
	private String dbPass = "";
	
	/**
	 * GER: Interne Variable für den Spawnerpreis
	 * ENG: internal variable for the price of the Spawner
	 */
	private int spawnerpreis = 0;
	
	/**
	 * GER: Interne Variable für den Spawner-Limit
	 * ENG: internal variable for the spawner limit
	 */
	private int spawnerlimit = 10;
	
	/**
	 * GER: Interne Variable für die Referenz auf die Plugin-Klasse
	 * ENG: internal variable for the reference to the plugin-class
	 */
	private MainPlugin plugin = null;
	
	/**
	 * GER: Gibt die Referenz von der Plugin-Klasse zurück
	 * ENG: Returns the reference of the plugin class
	 * @return
	 */
	public MainPlugin getPlugin() {
		return plugin;
	}
	
	/**
	 * GER: Setzt die Referenz zur Plugin-Klasse
	 * ENG: Sets the reference to the plugin-class
	 * @param plugin
	 */
	public void setPlugin(MainPlugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * GER: Gibt den Datenbank-Hostname zurück
	 * ENG: Returns the hostname of the database
	 * @return
	 */
	public String getDbHost() {
		return dbHost;
	}
	
	/**
	 * GER: Gibt den Datenbank-Name zurück
	 * ENG: Returns the name of the database
	 * @return
	 */
	public String getDbName() {
		return dbName;
	}
	
	/**
	 * GER: Gibt den Datenbank-User zurück
	 * ENG: Returns the username of the database
	 * @return
	 */
	public String getDbUser() {
		return dbUser;
	}
	
	/**
	 * GER: Gibt den Datenbank-Passwort zurück
	 * ENG: Returns the password of the database
	 * @return
	 */
	public String getDbPass() {
		return dbPass;
	}
	
	/**
	 * GER: Gibt den SpawnerPreis zurück
	 * ENG: Returns the price of one spawner
	 * @return
	 */
	public int getSpawnerpreis() {
		return spawnerpreis;
	}
		
	/**
	 * GER: Gibt den Spawner-Limit zurück
	 * ENG: Returns the limit of spawners
	 * @return
	 */
	public int getSpawnerlimit() {
		return spawnerlimit;
	}
	
	/**
	 * GER: Erstellt die erste Config (Standard-Config)
	 * ENG: Creates the very first configuration (default config)
	 */
	public void initConfig(){
		plugin.getConfig().addDefault(this.path2Host, "localhost");
		plugin.getConfig().addDefault(this.path2User, "spawnerliste");
		plugin.getConfig().addDefault(this.path2Pass, "spawnerliste");
		plugin.getConfig().addDefault(this.path2Name, "spawnerliste");
		plugin.getConfig().addDefault(this.path2SpawnerLimit, 10);
		plugin.getConfig().addDefault(this.path2SpawnerPreis, 100000);
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();
		plugin.reloadConfig();
	}
	
	/**
	 * GER: Liest die Config aus dem Bukkit-Config-Objekt. Falls nicht vorhanden wird die 
	 *      Standard-Config erstellt
	 * ENG: Read the config from the config-Object of Bukkit. If no config exists, it creates 
	 *      the default config
	 */
	public void readConfig(){
		initConfig();
		
		if(plugin.getConfig().contains(this.path2Host)){
			this.dbHost = plugin.getConfig().getString(this.path2Host);
		}
		
		if(plugin.getConfig().contains(this.path2User)){
			this.dbUser = plugin.getConfig().getString(this.path2User);
		}
		
		if(plugin.getConfig().contains(this.path2Pass)){
			this.dbPass = plugin.getConfig().getString(this.path2Pass);
		}
		
		if(plugin.getConfig().contains(this.path2Name)){
			this.dbName = plugin.getConfig().getString(this.path2Name);
		}
		
		if(plugin.getConfig().contains(this.path2SpawnerLimit)){
			this.spawnerlimit = plugin.getConfig().getInt(this.path2SpawnerLimit);
		}
		
		if(plugin.getConfig().contains(this.path2SpawnerPreis)){
			this.spawnerpreis = plugin.getConfig().getInt(this.path2SpawnerPreis);
		}
	}
		
	/**
	 * GER: Zeigt die Config in der Console an
	 * ENG: Shows the Config in the console
	 */
	public void showConfig(){
		Logger l = plugin.getLogger();
		l.info("Host: " + getDbHost());
		l.info("Name: " + getDbName());
 		l.info("User: " + getDbUser());
		l.info("Spawner-Preis: " + getSpawnerpreis());
		l.info("Spawner-Limit: " + getSpawnerlimit());
	}
}
