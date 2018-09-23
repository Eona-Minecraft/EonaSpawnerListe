package eonaminecraft;

import java.sql.ResultSet;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * GER: Stellt Methoden bereit, die dem Plugin helfen Spawner zu verwalten
 * ENG: Offers methods for handling the spawner management
 * @author Bloodrayne
 *
 */
public class SpawnerListe{

	private final String CREATE_NEW_ENTRY = "INSERT INTO spawner_liste (uuid, anzahl) VALUES ('%uuid%',0)";
	private final String UPDATE_ADD_SPAWNER = "UPDATE spawner_liste SET anzahl = anzahl + 1 WHERE uuid='%uuid%' ";
	private final String UPDATE_DEC_SPAWNER = "UPDATE spawner_liste SET anzahl = anzahl - 1 WHERE uuid='%uuid%' ";
	private final String DELETE_ENTRY = "DELETE FROM spawner_liste WHERE uuid='%uuid%' ";
	private final String SELECT_GET_SPAWNER = "SELECT anzahl FROM spawner_liste WHERE uuid='%uuid%' ";
	private final String SELECT_IS_REGISTRED = "SELECT COUNT(*) FROM spawner_liste WHERE uuid='%uuid%'";
	
	
	private MainPlugin plugin = null;
	private MyEconomy myEco = new MyEconomy();

	public MainPlugin getPlugin() {
		return plugin;
	}

	public void addSpawner(UUID id){
		try {
			if(!isUserRegistred(id)){
				createNewEntry(id);
			}
			MySQLDB x = new MySQLDB(plugin.getLogger());
			x.setConnectionData(plugin.getMyConfiguration().getDbHost(), plugin.getMyConfiguration().getDbUser(), plugin.getMyConfiguration().getDbPass(), plugin.getMyConfiguration().getDbName());
			x.openConnections();
			x.execute(UPDATE_ADD_SPAWNER.replace("%uuid%",id.toString()));
			x.closeConnections();
		} catch (Exception e) {
			plugin.logInfo(e.getMessage());
			plugin.logInfo(e.getStackTrace().toString());
		}
	}
	
	public void decSpawner(UUID id){
		try {
			if(!isUserRegistred(id)){
				createNewEntry(id);
			}
			MySQLDB x = new MySQLDB(plugin.getLogger());
			x.setConnectionData(plugin.getMyConfiguration().getDbHost(), plugin.getMyConfiguration().getDbUser(), plugin.getMyConfiguration().getDbPass(), plugin.getMyConfiguration().getDbName());
			x.openConnections();
			x.execute(UPDATE_DEC_SPAWNER.replace("%uuid%",id.toString()));
			x.closeConnections();
		} catch (Exception e) {
			plugin.logInfo(e.getMessage());
			plugin.logInfo(e.getStackTrace().toString());
		}
	}
	
	public void deleteEntry(UUID id){
		try {
			if(!isUserRegistred(id)){
				createNewEntry(id);
			}
			MySQLDB x = new MySQLDB(plugin.getLogger());
			x.setConnectionData(plugin.getMyConfiguration().getDbHost(), plugin.getMyConfiguration().getDbUser(), plugin.getMyConfiguration().getDbPass(), plugin.getMyConfiguration().getDbName());
			x.openConnections();
			x.execute(DELETE_ENTRY.replace("%uuid%",id.toString()));
			x.closeConnections();
		} catch (Exception e) {
			plugin.logInfo(e.getMessage());
			plugin.logInfo(e.getStackTrace().toString());
		}
	}
	
	
	public boolean isUserRegistred(UUID id) {
		try {
			int anzahl = 0;
			MySQLDB x = new MySQLDB(plugin.getLogger());
			x.setConnectionData(plugin.getMyConfiguration().getDbHost(), plugin.getMyConfiguration().getDbUser(), plugin.getMyConfiguration().getDbPass(), plugin.getMyConfiguration().getDbName());
			x.openConnections();
			ResultSet rs = x.query(SELECT_IS_REGISTRED.replace("%uuid%", id.toString()));
			
			while(rs.next()){
				anzahl = rs.getInt(1);
			}
			rs.close();
			x.closeConnections();
			return anzahl > 0;
		} catch (Exception e) {
			plugin.logInfo(e.getMessage());
			plugin.logInfo(e.getStackTrace().toString());
			return false;
		}
	}


	public void setPlugin(MainPlugin plugin) {
		this.plugin = plugin;
		myEco.initEconomy(plugin);
	}
	
	public void createNewEntry(UUID id){
		try {
			if(plugin.getMyConfiguration().isDebug()){
				
			}
			MySQLDB x = new MySQLDB(plugin.getLogger());
			x.setConnectionData(plugin.getMyConfiguration().getDbHost(), plugin.getMyConfiguration().getDbUser(), plugin.getMyConfiguration().getDbPass(), plugin.getMyConfiguration().getDbName());
			x.openConnections();
			x.execute(CREATE_NEW_ENTRY.replace("%uuid%",id.toString()));
			x.closeConnections();
		} catch (Exception e) {
			plugin.logInfo(e.getMessage());
		}
	}
	
	
	public int getAnzahlSpawnerOfPlayer(UUID id){
		try {
			int anzahl = 0;
			MySQLDB x = new MySQLDB(plugin.getLogger());
			x.setConnectionData(plugin.getMyConfiguration().getDbHost(), plugin.getMyConfiguration().getDbUser(), plugin.getMyConfiguration().getDbPass(), plugin.getMyConfiguration().getDbName());
			x.openConnections();
			ResultSet rs = x.query(SELECT_GET_SPAWNER.replace("%uuid%", id.toString()));
			
			while(rs.next()){
				anzahl = rs.getInt(1);
			}
			rs.close();
			x.closeConnections();
			return anzahl;
		} catch (Exception e) {
			plugin.logInfo(e.getMessage() );
			return 0;
		}
	}
	
	
	
	public void givePlayerMobSpawner(Player x){
		x.getInventory().addItem(new ItemStack(Material.SPAWNER));
	}

	public void addSpawner2Player(Player x){
		if(getAnzahlSpawnerOfPlayer(x.getUniqueId()) <= plugin.getMyConfiguration().getSpawnerlimit()){
			if(myEco.getCurrentBalanceofPlayer(x) >= plugin.getMyConfiguration().getSpawnerpreis()){
				addSpawner(x.getUniqueId());
				givePlayerMobSpawner(x);
				myEco.decreaseBalanceOfPlayer(x, plugin.getMyConfiguration().getSpawnerpreis());
				x.sendMessage("Du hast einen Spawner gekauft");
			}else{
				x.sendMessage("Du hast nicht genügend Geld.");
				x.sendMessage("Du hast: "  + myEco.getCurrentBalanceofPlayer(x)  + myEco.getCurrencyPlural());
				x.sendMessage("Du brauchst: "  + plugin.getMyConfiguration().getSpawnerpreis()  + myEco.getCurrencyPlural());
			}
		}else{
			x.sendMessage("Du hast schon genug Spawner!");	
			
		}
	}

}
