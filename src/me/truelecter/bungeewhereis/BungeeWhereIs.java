package me.truelecter.bungeewhereis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.Level;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BungeeWhereIs extends Plugin{
	Properties prop = new Properties();
	String propName = "whereIs.properties";
	public void loadprop(){
		try{
			prop.load(new FileInputStream(propName));
		}catch(FileNotFoundException e){
			this.getLogger().log(Level.INFO, "prop.properties doesnt exist!");
		}catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	private void setprop(String key, String value){
		prop.setProperty(key, value);
	}
	
	private String readprop(String pName, String dName){
		return prop.getProperty(pName, dName);
	}

	private String serverName(String sn){
		return readprop(sn, sn);
	}
	
	@SuppressWarnings("deprecation")
	private void commandListener(CommandSender sender, String[] args){
		ProxiedPlayer p1;
		if (args.length == 0) {
			sender.sendMessage(ChatColor.GOLD + "Использование: /f <имена игроков>");
        } else {
        	for (int i=0; i<args.length; i++){
        		p1 = getProxy().getPlayer(args[i]);
        		if (p1 != null) {
        			sender.sendMessage(ChatColor.GOLD + args[i] + ChatColor.GREEN + " онлайн"+ ChatColor.RESET + " " + readprop("bungeeWhereIs_separator", "на") + " " + ChatColor.BLUE + serverName(p1.getServer().getInfo().getName().toString()));
        		} else {
        			sender.sendMessage(ChatColor.GOLD + args[i] + ChatColor.RED + " оффлайн");
        		}
        	}
        }
	}
	public void storeprop(){
		try{
			prop.store(new FileOutputStream(propName), null); //comments can be also null
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this,new Command("whereis"){	
        	@Override
        	public void execute(CommandSender sender, String[] args) {
        		commandListener(sender, args);
            }
        });    
        ProxyServer.getInstance().getPluginManager().registerCommand(this,new Command("f"){	
        	@Override
        	public void execute(CommandSender sender, String[] args) {
        		commandListener(sender, args);
            }
        });    
        ProxyServer.getInstance().getPluginManager().registerCommand(this,new Command("find"){	
        	@Override
        	public void execute(CommandSender sender, String[] args) {
        		commandListener(sender, args);
            }
        }); 
        ProxyServer.getInstance().getPluginManager().registerCommand(this,new Command("ssn","whereis.admin"){	
        	@SuppressWarnings("deprecation")
			@Override
        	public void execute(CommandSender sender, String[] args) {
        		if (args.length == 2){
        			setprop(args[0], args[1]);
        		} else {
        			sender.sendMessage("А надо так: /ssn <serverName> <visibleName>");
        		}
        	}
        });
        ProxyServer.getInstance().getPluginManager().registerCommand(this,new Command("cns","whereis.admin"){	
        	@SuppressWarnings("deprecation")
			@Override
        	public void execute(CommandSender sender, String[] args) {
        		if (args.length > 0){
        			StringBuilder str = new StringBuilder();
        			for (String s : args){
        				str.append(s+" ");
        			}
        			setprop("bungeeWhereIs_separator",str.toString().substring(0, str.toString().length()-1));
        		} else {
        			sender.sendMessage("А надо так: /cns <separator>");
        		}
        	}
        });
	}
	public void onDisable(){
		storeprop();
	}
}
