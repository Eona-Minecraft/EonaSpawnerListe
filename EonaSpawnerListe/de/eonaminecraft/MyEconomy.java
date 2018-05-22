package eonaminecraft;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class MyEconomy {

	private Economy econ = null;
	private MainPlugin plugin = null;
	
	public void initEconomy(MainPlugin x){
		this.plugin = x;
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
	
	
	public double getCurrentBalanceofPlayer(Player x){
		if(!isEconInit()){
			plugin.logInfo("Economy wurde nicht initialisiert!");
			return -1;
		}else{
			double erg = 0;
			erg = econ.getBalance(x);
			return erg;
		}
	}
	
	public void setBalanceOfPlayer(Player x,double newamount) {
		if(!isEconInit()){
			plugin.logInfo("Economy wurde nicht initialisiert!");
		}else{
			econ.withdrawPlayer(x, getCurrentBalanceofPlayer(x));
			econ.depositPlayer(x, newamount);
		}
	}
	
	public void decreaseBalanceOfPlayer(Player x, double decValue){
		if(!isEconInit()){
			plugin.logInfo("Economy wurde nicht initialisiert!");
		}else{
			setBalanceOfPlayer(x, getCurrentBalanceofPlayer(x) - decValue);
		}
	}
	
	public void addBalanceOfPlayer(Player x, double addValue){
		if(!isEconInit()){
			plugin.logInfo("Economy wurde nicht initialisiert!");
		}else{
			setBalanceOfPlayer(x, getCurrentBalanceofPlayer(x) + addValue);
		}
	}
	
	public String getCurrencySingular(){
		if(!isEconInit()){
			plugin.logInfo("Economy wurde nicht initialisiert!");
			return "-1";
		}else{
			return econ.currencyNameSingular();
		}
	}
	
	public String getCurrencyPlural(){
		if(!isEconInit()){
			plugin.logInfo("Economy wurde nicht initialisiert!");
			return "-1";
		}else{
			return econ.currencyNamePlural();
		}
	}
	
}
