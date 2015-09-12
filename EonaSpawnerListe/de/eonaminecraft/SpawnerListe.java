package eonaminecraft;

import io.netty.handler.codec.http.HttpContentEncoder.Result;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		boolean erg = false;
		switch(label){
		case "getSpawner":
			//TODO: GetSpawner mit Ekonomie implementieren
			erg = true;
		case "getSpawnerPreis":
			//TODO: GetSpawner mit Ekonomie implementieren
			erg = true;
		case "spawnerliste":
			if(args.length > 0){
				erg = true;
				switch(args[0]){
				
				}
			}else{
				erg = false;
			}
		}
		
		return erg;
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
	
	
	
	
	private final String CREATE_NEW_ENTRY = "INSERT INTO spawner_liste (uuid, anzahl) VALUES ('%uuid%',0)";
	private final String UPDATE_ADD_SPAWNER = "UPDATE spawner_liste SET anzahl = anzahl + 1 WHERE uuid='%uuid%' ";
	private final String UPDATE_DEC_SPAWNER = "UPDATE spawner_liste SET anzahl = anzahl - 1 WHERE uuid='%uuid%' ";
	private final String DELETE_ENTRY = "DELETE FROM spawner_liste WHERE uuid='%uuid%' ";
	private final String SELECT_GET_SPAWNER = "SELECT anzahl FROM spawner_liste WHERE uuid='%uuid%' ";
	private final String SELECT_IS_REGISTRED = "SELECT COUNT(*) FROM spawner_liste WHERE uuid='%uuid%'";
	
	
	private void addSpawnerTooUuid(String uuid){
		boolean erg = isUserRegistred(uuid);
		if(!erg){
			registerUser(uuid);
		}
		conn.execute(UPDATE_ADD_SPAWNER.replace("%uuid%", uuid));
	}
	
	private void registerUser(String uuid){
		conn.execute(CREATE_NEW_ENTRY.replace("%uuid%", uuid));
	}
	
	private boolean isUserRegistred(String uuid){
		ResultSet x = conn.query(SELECT_IS_REGISTRED.replace("%uuid%", uuid));
		boolean erg = false;
		try {
			while(x.next()){
				if(x.getInt(0) > 0){
					erg = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return erg;
	}
	
	private void decSpawnerToUuid(String uuid){
		boolean erg = isUserRegistred(uuid);
		if(!erg){
			registerUser(uuid);
		}
		conn.execute(UPDATE_DEC_SPAWNER.replace("%uuid%", uuid));
	}
	
	private int getAnzahlSpawnerFromUUID(String uuid){
		ResultSet x = conn.query(SELECT_GET_SPAWNER.replace("%uuid%", uuid));
		int erg = -1;
		try{
			while(x.next()){
				erg = x.getInt(0);
			}
		}catch(Exception e){
			
		}
		return erg;
	}
	
	private void deleteUUID(String uuid){
		conn.execute(DELETE_ENTRY.replace("%uuid%", uuid));
	}


}
