package eonaminecraft;


import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;


import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		boolean erg = false;
		boolean done = false;
		Player x = getPlayerOfName(sender.getName());
		double balance = 0;
		switch(label.toLowerCase()){
		case "getspawner":
			erg = true;
			if(ec.isEconInit()){
				balance  = ec.getEconomy().getBalance(x);
				if(balance >= cfg.getSpawnerpreis()){
					if(!isUserRegistred(x.getUniqueId())){
						registerUser(x.getUniqueId());
					}
					if(getAnzahlSpawnerFromUUID(x.getUniqueId()) < cfg.getSpawnerlimit()){
						ec.getEconomy().withdrawPlayer(x, cfg.getSpawnerpreis());
						x.getInventory().addItem(new ItemStack(Material.MOB_SPAWNER,1));
						addSpawnerToUuid(x.getUniqueId());
						sender.sendMessage("1 Spawner gekauft!");
						done = true;
					}else{
						sender.sendMessage("Du hast schon zu viele Spawner.");
					}
				}else{
					if(!done){
						sender.sendMessage("Du hast nicht genügend Geld! ");
						sender.sendMessage("Was du hast:     " + String.format("%.2f", balance));
						sender.sendMessage("Was du brauchst: " + cfg.getSpawnerpreis());
					}
				}
			}else{
				sender.sendMessage("Konnte Befehl nicht ausführen. Das Economy-System ist nicht initialisiert!");
			}
			break;
		case "getspawnerpreis":
			erg = true;
			sender.sendMessage("Um einen Spawner zu kaufen brauchst du " + cfg.getSpawnerpreis() + " EONA-Cash!");
		case "spawnerliste":
			//sender.sendMessage(args[0]);
			if(args.length > 0){
				erg = true;
				if(args[0].equals("version")){
					sender.sendMessage("Spawnerliste Version: ");
				}else if (args[0].equals("reload")){
					conn.closeConnections();
					cfg.readConfig(getConfig());
					ec.initEconomy(this);
					conn.setConnectionData(getConnectionDataFromConfig());
					conn.openConnections();
					sender.sendMessage("Reload erfolgreich");
				}else if(args.length > 1){
					try{
						Player p = getPlayerOfName(args[1]);
						if(p == null){
							throw new Exception("Spieler '" + args[1] + "' nicht gefunden");
						}
						if(!isUserRegistred(p.getUniqueId())){
							registerUser(p.getUniqueId());
						}
						switch(args[0]){
						case "inc":
//							sender.sendMessage("DB:" + getAnzahlSpawnerFromUUID(p.getUniqueId()));
//							sender.sendMessage("LIMIT :" +cfg.getSpawnerlimit());
							if(getAnzahlSpawnerFromUUID(p.getUniqueId()) < cfg.getSpawnerlimit()){
								addSpawnerToUuid(p.getUniqueId());
								p.getInventory().addItem(new ItemStack(Material.MOB_SPAWNER,1));
								sender.sendMessage("1 Spawner an Spieler '" + p.getDisplayName() + "' gegeben");
							}else{
								sender.sendMessage("Spieler '" + p.getDisplayName() + "' hat schon zu viele Spawner");
							}
							break;
						case "dec":
							decSpawnerToUuid(p.getUniqueId());
							sender.sendMessage("1 Spawner von Spieler '" + p.getDisplayName() + "' entfernt");
							break;
						case "show":
							sender.sendMessage("Spieler '" + p.getDisplayName() + "' hat " + getAnzahlSpawnerFromUUID(p.getUniqueId()) + " von " + cfg.getSpawnerlimit() + " Spawner");
							break;
						case "reset":
							deleteUUID(p.getUniqueId());
							sender.sendMessage("Spieler '" + p.getDisplayName() + "' aus der SpawnerListe entfernt");
							break;
						default:
							sender.sendMessage("Keine oder zu wenige Argumente!");
							sender.sendMessage("Syntax: /spawnerliste <version|dec|inc|show|reload> [playername]");
						}
					}catch(Exception e){
						sender.sendMessage(e.getMessage());
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
	
	
//	private Player getPlayerOfUUID(String uuid){
//		return this.getServer().getPlayer(uuid);
//	}
//	
	private Player getPlayerOfName(String name){
		return this.getServer().getPlayerExact(name);
	}
	
	
	
	
	private final String CREATE_NEW_ENTRY = "INSERT INTO spawner_liste (uuid, anzahl) VALUES ('%uuid%',0)";
	private final String UPDATE_ADD_SPAWNER = "UPDATE spawner_liste SET anzahl = anzahl + 1 WHERE uuid='%uuid%' ";
	private final String UPDATE_DEC_SPAWNER = "UPDATE spawner_liste SET anzahl = anzahl - 1 WHERE uuid='%uuid%' ";
	private final String DELETE_ENTRY = "DELETE FROM spawner_liste WHERE uuid='%uuid%' ";
	private final String SELECT_GET_SPAWNER = "SELECT anzahl FROM spawner_liste WHERE uuid='%uuid%' ";
	private final String SELECT_IS_REGISTRED = "SELECT COUNT(*) FROM spawner_liste WHERE uuid='%uuid%'";
	
	
	private void addSpawnerToUuid(UUID id){
		if(!(getAnzahlSpawnerFromUUID(id) >= cfg.getSpawnerlimit())){
			conn.execute(UPDATE_ADD_SPAWNER.replace("%uuid%", id.toString()));
		}
	}
	
	
	private void registerUser(UUID id){
		try{
			conn.execute(CREATE_NEW_ENTRY.replace("%uuid%", id.toString()));
		}catch(Exception e){
			l.info(e.getMessage());
//			l.info("SQL: " + CREATE_NEW_ENTRY.replace("%uuid%", id.toString()));
		}
	}
	
	private boolean isUserRegistred(UUID id){
//		l.info("SQL: " + SELECT_IS_REGISTRED.replace("%uuid%", id.toString()));
		ResultSet x = conn.query(SELECT_IS_REGISTRED.replace("%uuid%", id.toString()));
		boolean erg = false;
		try {
			while(x.next()){
				if(x.getInt(1) > 0){
					erg = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return erg;
	}
	
	private void decSpawnerToUuid(UUID id){
		if(!(getAnzahlSpawnerFromUUID(id) <= 0)){
			conn.execute(UPDATE_DEC_SPAWNER.replace("%uuid%", id.toString()));
		}
	}
	
	private int getAnzahlSpawnerFromUUID(UUID id){
//		l.info(SELECT_GET_SPAWNER.replace("%uuid%", id.toString()));
		ResultSet x = conn.query(SELECT_GET_SPAWNER.replace("%uuid%", id.toString()));
		int erg = -1;
		try{
			while(x.next()){
				erg = x.getInt(1);
			}
		}catch(Exception e){
			l.info(e.getMessage());
		}
		return erg;
	}
	
	private void deleteUUID(UUID id){
		conn.execute(DELETE_ENTRY.replace("%uuid%", id.toString()));
	}


}
