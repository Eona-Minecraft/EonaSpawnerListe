package eonaminecraft;

import io.netty.handler.codec.http.HttpContentEncoder.Result;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Formatter;
import java.util.logging.Logger;

import net.minecraft.server.v1_8_R1.Blocks;
import net.minecraft.server.v1_8_R1.Item;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnerListe extends JavaPlugin {

	private MySQLDB conn = new MySQLDB();
	private Logger l = null;
	private MyConfig cfg = new MyConfig();
	private MyEconomy ec = new MyEconomy();
	
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
				l.info("Datenbankverbindung geöffnet.");
				l.info("Initialisiere Hook ins Economy-System via VaultApi");
				ec.initEconomy(this);
				if(ec.isEconInit()){
					l.info("Hook ins Economy-System erfolgreich");
				}else{
					l.info("Hook ins Economy-System nicht erfolgreich");
				}
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
	//TODO: Spawnerlimit abfragen und drauf prüfen
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		boolean erg = false;
		Player x = getPlayerOfName(sender.getName());
		double balance = 0;
		switch(label){
		case "getSpawner":
			if(ec.isEconInit()){
				balance  = ec.getEconomy().getBalance(x);
				if(balance >= cfg.getSpawnerpreis()){
					ec.getEconomy().withdrawPlayer(x, (double) cfg.getSpawnerpreis());
					x.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.MOB_SPAWNER, 1));
				}else{
					sender.sendMessage("Du hast nicht genügend Geld! ");
					sender.sendMessage("Was du hast:     " + String.format("%.2f", balance));
					sender.sendMessage("Was du brauchst: " + cfg.getSpawnerpreis());
				}
			}else{
				sender.sendMessage("Konnte Befehl nicht ausführen. Das Economy-System ist nicht initialisiert!");
			}
			erg = true;
		case "getSpawnerPreis":
			sender.sendMessage("Um einen Spawner zu kaufen brauchst du " + cfg.getSpawnerpreis() + " EONA-Cash!");
			erg = true;
		case "spawnerliste":
			if(args.length > 0){
				erg = true;
				if(args[0] == "version"){
					sender.sendMessage("Spawnerliste Version: ");
				}else if(args.length > 1){
					switch(args[0]){
					case "inc":
						
						break;
					case "dec":
						break;
					case "show":
						break;
					case "reset":
						break;
					}
				}else{
					sender.sendMessage("Keine oder zu wenige Argumente!");
					sender.sendMessage("Syntax: /spawnerliste <version|dec|inc|show|reset> [playername]");
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
	
	
	private Player getPlayerOfUUID(String uuid){
		return this.getServer().getPlayer(uuid);
	}
	
	private Player getPlayerOfName(String name){
		return this.getServer().getPlayerExact(name);
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
