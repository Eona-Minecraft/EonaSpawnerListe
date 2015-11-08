package eonaminecraft;

import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class MyEconomy {

	private Economy econ = null;
	
	public void initEconomy(MainPlugin x){
		if (x.getServer().getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<Economy> rsp = x.getServer().getServicesManager().getRegistration(Economy.class);
	        if (rsp != null) {
	            econ = rsp.getProvider();
	        }
        }
	}
	
	public boolean isEconInit(){
		return econ != null;
	}
	
	
	public Economy getEconomy(){
		if(isEconInit()){
			return econ;
		}else{
			return null;
		}
	}
}
