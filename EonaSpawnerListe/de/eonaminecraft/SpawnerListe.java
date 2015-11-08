package eonaminecraft;

import java.sql.ResultSet;
import java.util.UUID;

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


	public MainPlugin getPlugin() {
		return plugin;
	}


	public void setPlugin(MainPlugin plugin) {
		this.plugin = plugin;
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
			throw e;
		}
	}
	
	public void addSpawner(UUID id){
		try {
			MySQLDB x = new MySQLDB(plugin.getLogger());
			x.setConnectionData(plugin.getMyConfiguration().getDbHost(), plugin.getMyConfiguration().getDbUser(), plugin.getMyConfiguration().getDbPass(), plugin.getMyConfiguration().getDbName());
			x.openConnections();
			x.execute(UPDATE_ADD_SPAWNER.replace("%uuid%",id.toString()));
			x.closeConnections();
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void decSpawner(UUID id){
		try {
			MySQLDB x = new MySQLDB(plugin.getLogger());
			x.setConnectionData(plugin.getMyConfiguration().getDbHost(), plugin.getMyConfiguration().getDbUser(), plugin.getMyConfiguration().getDbPass(), plugin.getMyConfiguration().getDbName());
			x.openConnections();
			x.execute(UPDATE_DEC_SPAWNER.replace("%uuid%",id.toString()));
			x.closeConnections();
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void deleteEntry(UUID id){
		try {
			MySQLDB x = new MySQLDB(plugin.getLogger());
			x.setConnectionData(plugin.getMyConfiguration().getDbHost(), plugin.getMyConfiguration().getDbUser(), plugin.getMyConfiguration().getDbPass(), plugin.getMyConfiguration().getDbName());
			x.openConnections();
			x.execute(DELETE_ENTRY.replace("%uuid%",id.toString()));
			x.closeConnections();
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int getAnzahlSpawnerOfPlayer(UUID id) throws Exception{
		try {
			int anzahl = 0;
			MySQLDB x = new MySQLDB(plugin.getLogger());
			x.setConnectionData(plugin.getMyConfiguration().getDbHost(), plugin.getMyConfiguration().getDbUser(), plugin.getMyConfiguration().getDbPass(), plugin.getMyConfiguration().getDbName());
			x.openConnections();
			ResultSet rs = x.query(SELECT_GET_SPAWNER.replace("%uuid%", id.toString()));
			
			while(rs.next()){
				anzahl = rs.getInt(0);
			}
			rs.close();
			x.closeConnections();
			return anzahl;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public boolean isUserRegistred(UUID id) throws Exception{
		try {
			int anzahl = 0;
			MySQLDB x = new MySQLDB(plugin.getLogger());
			x.setConnectionData(plugin.getMyConfiguration().getDbHost(), plugin.getMyConfiguration().getDbUser(), plugin.getMyConfiguration().getDbPass(), plugin.getMyConfiguration().getDbName());
			x.openConnections();
			ResultSet rs = x.query(SELECT_IS_REGISTRED.replace("%uuid%", id.toString()));
			
			while(rs.next()){
				anzahl = rs.getInt(0);
			}
			rs.close();
			x.closeConnections();
			return anzahl > 0;
		} catch (Exception e) {
			throw e;
		}
	}
	
}
