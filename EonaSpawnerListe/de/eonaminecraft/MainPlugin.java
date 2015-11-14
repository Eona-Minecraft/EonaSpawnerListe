package eonaminecraft;

import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MainPlugin extends JavaPlugin{
	
	private MyConfig mycfg = new MyConfig();
	private MyEconomy myEco= new MyEconomy(); 
	private SpawnerListe liste = new SpawnerListe();
	
	
	public void onEnable(){
		//Config laden
		
		logInfo("Lade Config");
		mycfg.setPlugin(this);
		mycfg.readConfig();
		logInfo("Config geladen");
		
		//Hooks setzen
		liste.setPlugin(this);
		myEco.initEconomy(this);
		
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(label.equalsIgnoreCase("sl")){
			if(args.length > 0){
				if(args[0] == "admin"){
					if(sender.isOp() || !(sender instanceof Player) || sender.hasPermission("spawnerliste.admin")){
						if(args.length > 1){
							//Durch admin cmds gehen
							switch(args[1]){
							case "add":
								if(args.length > 2){
									Player p = getServer().getPlayer(args[2]);
									if(p == null){
										sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
									}else{
										liste.addSpawner(p.getUniqueId());
										sender.sendMessage("Spieler '" + p.getDisplayName() + "' wurde 1 Spawner hinzugefügt");
									}
								}else{
									sender.sendMessage("Spieler wurde nicht angegeben");
								}
								break;
							case "dec":
								if(args.length > 2){
									Player p = getServer().getPlayer(args[2]);
									if(p == null){
										sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
									}else{
										liste.decSpawner(p.getUniqueId());
										sender.sendMessage("Spieler '" + p.getDisplayName() + "' wurde 1 Spawner entfernt");
									}
								}else{
									sender.sendMessage("Spieler wurde nicht angegeben");
								}
								break;
							case "register":
								if(args.length > 2){
									Player p = getServer().getPlayer(args[2]);
									if(p == null){
										sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
									}else{
										liste.createNewEntry(p.getUniqueId());
										sender.sendMessage("Spieler '" + p.getDisplayName() + "' hinzugefügt");
									}
								}else{
									sender.sendMessage("Spieler wurde nicht angegeben");
								}
								break;
							case "unregister":
								if(args.length > 2){
									Player p = getServer().getPlayer(args[2]);
									if(p == null){
										sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
									}else{
										liste.deleteEntry(p.getUniqueId());
										sender.sendMessage("Spieler '" + p.getDisplayName() + "' entfernt");
									}
								}else{
									sender.sendMessage("Spieler wurde nicht angegeben");
								}
								break;
							case "reload":
								sender.sendMessage("Lese Config neu ein");
								mycfg.readConfig();
								sender.sendMessage("Config neu eingelesen");
								break;
							case "give":
								if(args.length > 2){
									Player p = getServer().getPlayer(args[2]);
									if(p == null){
										sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
									}else{
										liste.givePlayerMobSpawner(p);
										sender.sendMessage("Spieler '" + p.getDisplayName() + "' wurde um 1 Spawner bereichert");
									}
								}else{
									sender.sendMessage("Spieler wurde nicht angegeben");
								}
								break;
							case "get":
								if(args.length > 2){
									Player p = getServer().getPlayer(args[2]);
									if(p == null){
										sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
									}else{
										sender.sendMessage("Spieler '" + p.getDisplayName() + "' hat " + Color.AQUA + liste.getAnzahlSpawnerOfPlayer(p.getUniqueId()) + Color.WHITE + " Spawner" );
									}
								}else{
									sender.sendMessage("Spieler wurde nicht angegeben");
								}
								break;
							default:
								showAdminCmd(sender);
							}
						}else{
							showAdminCmd(sender);
						}
					}else{
						sender.sendMessage("Du hast keine Berechtigung für diesen Befehl");
					}
				}else{
					if(sender instanceof Player){
						if(sender.hasPermission("spawnerliste.user")){
							switch(args[0]){
							case "getSpawnerPreis":
								sender.sendMessage("Der Spawnerpreis beträgt " + Color.AQUA + mycfg.getSpawnerpreis() + Color.WHITE + myEco.getCurrencyPlural());
								break;
							case "getSpawnerLimit":
								sender.sendMessage("Das Spawnerlimit liegt bei " + Color.AQUA + mycfg.getSpawnerlimit() + Color.WHITE + " Spawner!");
								break;
							case "getSpawner":
								liste.addSpawner2Player((Player)sender);
								break;
							}
						}else{
							sender.sendMessage("Du hast keine Berechtigung auf diesen Befehl");
						}
					}else{
						sender.sendMessage(Color.RED + "Diese Bfehle können nicht in der Konsole ausgeführt werden");
					}
				}
			}else{
				//Version anzeigen und hilfe
				
			}
			return true;
		}
		
		return false;
	}
	
	public MyConfig getMyConfiguration(){
		return mycfg;
	}
	
	public void logInfo(String info){
		this.getLogger().info(info);
	}
	
	private void showAdminCmd(CommandSender sender){
		sender.sendMessage(Color.YELLOW + "Admin-Commands:");
		sender.sendMessage("/sl admin <cmd>");
		sender.sendMessage(Color.GREEN + "add <Spieler>" + Color.WHITE +": Gibt dem Spieler einen Spawner in der DB" );
		sender.sendMessage(Color.GREEN + "dec <Spieler>" + Color.WHITE +": Entfernt dem Spieler einen Spawner in der DB" );
		sender.sendMessage(Color.GREEN + "register <Spieler>" + Color.WHITE +": Fügt einen Spieler der DB hinzu" );
		sender.sendMessage(Color.GREEN + "unregister <Spieler>" + Color.WHITE +": Entfernt den Spieler aus der DB" );
		sender.sendMessage(Color.GREEN + "give <Spieler>" + Color.WHITE +": Gibt dem Spieler einen Spawner" );
		sender.sendMessage(Color.GREEN + "get <Spieler>" + Color.WHITE +": Gibt die Anzahl Spawner aus" );
		sender.sendMessage(Color.GREEN + "reload" + Color.WHITE +": Liest die Config neu ein" );
	}
}


