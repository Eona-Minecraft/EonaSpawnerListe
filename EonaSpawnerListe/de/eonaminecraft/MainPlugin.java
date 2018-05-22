package eonaminecraft;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
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

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public Player getOnlinePlayer(String s){
		Player x = getServer().getPlayerExact(s);
		return x;
	}

	public OfflinePlayer getOfflinePlayer(String s){
		OfflinePlayer x = null;
		for (OfflinePlayer pl: getServer().getOfflinePlayers()
			 ) {
			if (pl.getName().equalsIgnoreCase(s)){
				x = pl;
				break;
			}
		}
		return  x;
	}

	private boolean isPlayerOnline(String s){
		return (getServer().getPlayerExact(s) != null);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(command.getName().equalsIgnoreCase("sl") || command.getName().equalsIgnoreCase("spawnerliste")){
			//printArgs2Console(args);
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("admin")){
					if(sender.isOp() || !(sender instanceof Player) || sender.hasPermission("spawnerliste.admin")){
						if(args.length > 1){
							//Durch admin cmds gehen
							switch(args[1]){
							case "add":
								if(args.length > 2){
									if(isPlayerOnline(args[2])){
										Player p = getOnlinePlayer(args[2]);
										if(p == null){
											sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
										}else{
											liste.addSpawner(p.getUniqueId());
											sender.sendMessage("Spieler '" + p.getDisplayName() + "' wurde 1 Spawner hinzugefügt");
										}
									}else{
										OfflinePlayer p = getOfflinePlayer(args[2]);
										if(p == null){
											sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
										}else{
											liste.addSpawner(p.getUniqueId());
											sender.sendMessage("Spieler '" + p.getName() + "' wurde 1 Spawner hinzugefügt");
										}
									}

								}else{
									sender.sendMessage("Spieler wurde nicht angegeben");
								}
								break;
							case "dec":
								if(args.length > 2){
									if(isPlayerOnline(args[2])){
										Player p = getOnlinePlayer(args[2]);
										if(p == null){
											sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
										}else{
											liste.decSpawner(p.getUniqueId());
											sender.sendMessage("Spieler '" + p.getDisplayName() + "' wurde 1 Spawner entfernt");
										}
									}else{
										OfflinePlayer p = getOfflinePlayer(args[2]);
										if(p == null){
											sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
										}else{
											liste.decSpawner(p.getUniqueId());
											sender.sendMessage("Spieler '" + p.getName() + "' wurde 1 Spawner entfernt");
										}
									}

								}else{
									sender.sendMessage("Spieler wurde nicht angegeben");
								}
								break;
							case "register":
								if(args.length > 2){
									if(isPlayerOnline(args[2])){
										Player p = getOnlinePlayer(args[2]);
										if(p == null){
											sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
										}else{
											liste.createNewEntry(p.getUniqueId());
											sender.sendMessage("Spieler '" + p.getDisplayName() + "' hinzugefügt");
										}
									}else{
										OfflinePlayer p = getOfflinePlayer(args[2]);
										if(p == null){
											sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
										}else{
											liste.createNewEntry(p.getUniqueId());
											sender.sendMessage("Spieler '" + p.getName() + "' hinzugefügt");
										}
									}

								}else{
									sender.sendMessage("Spieler wurde nicht angegeben");
								}
								break;
							case "unregister":
								if(args.length > 2){
									if(isPlayerOnline(args[2])){
										Player p = getOnlinePlayer(args[2]);
										if(p == null){
											sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
										}else{
											liste.deleteEntry(p.getUniqueId());
											sender.sendMessage("Spieler '" + p.getDisplayName() + "' entfernt");
										}
									}else{
										OfflinePlayer p = getOfflinePlayer(args[2]);
										if(p == null){
											sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
										}else{
											liste.deleteEntry(p.getUniqueId());
											sender.sendMessage("Spieler '" + p.getName() + "' entfernt");
										}
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
									if(isPlayerOnline(args[2])){
										Player p = getOnlinePlayer(args[2]);
										if(p == null){
											sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
										}else{
											liste.givePlayerMobSpawner(p);
											sender.sendMessage("Spieler '" + p.getDisplayName() + "' wurde um 1 Spawner bereichert");
										}
									}else{
										sender.sendMessage("Spieler '" + args[2] + "' nicht online!");
									}

								}else{
									sender.sendMessage("Spieler wurde nicht angegeben");
								}
								break;
							case "get":
								if(args.length > 2){
								    if(isPlayerOnline(args[2])){
                                        Player p = getOnlinePlayer(args[2]);
                                        if(p == null){
                                            sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
                                        }else{
                                            sender.sendMessage("Spieler '" + p.getDisplayName() + "' hat " + liste.getAnzahlSpawnerOfPlayer(p.getUniqueId())  + " Spawner" );
                                        }
                                    }else{
                                        OfflinePlayer p = getOfflinePlayer(args[2]);
                                        if(p == null){
                                            sender.sendMessage("Spieler '" + args[2] + "' nicht gefunden oder nicht online!");
                                        }else{
                                            sender.sendMessage("Spieler '" + p.getName() + "' hat " + liste.getAnzahlSpawnerOfPlayer(p.getUniqueId())  + " Spawner" );
                                        }
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
								sender.sendMessage("Der Spawnerpreis beträgt " + mycfg.getSpawnerpreis() + myEco.getCurrencyPlural());
								break;
							case "getSpawnerLimit":
								sender.sendMessage("Das Spawnerlimit liegt bei " + mycfg.getSpawnerlimit() + " Spawner!");
								break;
							case "getSpawner":
								liste.addSpawner2Player((Player)sender);
								break;
							}
						}else{
							sender.sendMessage("Du hast keine Berechtigung auf diesen Befehl");
						}
					}else{
						sender.sendMessage("Diese Befehle können nicht in der Konsole ausgeführt werden");
					}
				}
			}else{
				//Version anzeigen und hilfe
				
			}//args.length > 0
			return true;
		}//command.getName().equalsIgnoreCase("sl") || command.getName().equalsIgnoreCase("spawnerliste")
		
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
		sender.sendMessage("add <Spieler>: Gibt dem Spieler einen Spawner in der DB" );
		sender.sendMessage("dec <Spieler>: Entfernt dem Spieler einen Spawner in der DB" );
		sender.sendMessage("register <Spieler>: Fügt einen Spieler der DB hinzu" );
		sender.sendMessage("unregister <Spieler>: Entfernt den Spieler aus der DB" );
		sender.sendMessage("give <Spieler>: Gibt dem Spieler einen Spawner" );
		sender.sendMessage("get <Spieler>: Gibt die Anzahl Spawner aus" );
		sender.sendMessage("reload: Liest die Config neu ein" );
	}
	
	private void printArgs2Console(String args[]){
		for(int i = 0; i < args.length; i++){
			logInfo("Argument [" + i + "] = " + args[i]);
		}
	}
}


