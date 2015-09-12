package eonaminecraft;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class SpawnerListe extends JavaPlugin {

	private MySQLDB conn = new MySQLDB();
	private Logger l = null;
	private MyConfig cfg = new MyConfig();
	
	public void onEnable(){
		this.l = this.getLogger();
		conn.l = l;
		l.info("Erstelle ConfigFile falls es noch nicht existiert");
		createConfigIfNotExists();
		l.info("Lese Config ein");
		cfg.readConfig(getConfig());
		cfg.showConfig(l);
		l.info("Lese DB-Config ein.");
		ConnectionData x = getConnectionDataFromConfig();
		if(x != null){
			conn.setConnectionData(getConnectionDataFromConfig()); 
			l.info("Öffne Verbindung");
			conn.openConnections();
			if (conn.isOpened()){
				l.info("Datenbankverbindung ge�ffnet.");
			}
		}else{
			l.severe("DB-Verbindungsdaten fehlerhaft!");
			this.getPluginLoader().disablePlugin(this);
		}
	}
	
	
	public void onDisable(){
		conn.closeConnections();
	}
	
	
	private ConnectionData getConnectionDataFromConfig(){
		ConnectionData c = new ConnectionData();
		try{
			c.setDbHost(cfg.getDbHost());
			c.setDbUser(cfg.getDbUser());
			c.setDbPass(cfg.getDbPass());
			c.setDbName(cfg.getDbName());
		}catch(Exception e){
			l.info(e.getMessage());
		}
		return c;
	}
	
	private void createConfigIfNotExists(){
		if(!getDataFolder().exists()){
			try {
				getDataFolder().mkdirs();
				
			} catch (Exception e) {
				l.info(e.getMessage());
			}
		}
		File x = new File(getDataFolder().getAbsolutePath()  + File.separator + "config.yml");
		if(!x.exists()){
			try {
				getConfig().save(x);
			} catch (Exception e) {
				l.info(e.getMessage());
			}
		}
	}
	
	
	
	
}
