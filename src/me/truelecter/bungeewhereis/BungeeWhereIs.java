package me.truelecter.bungeewhereis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.Level;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeWhereIs
  extends Plugin
{
  Properties prop = new Properties();
  String propName = "whereIs.properties";
  
  public void loadprop()
  {
    try
    {
      this.prop.load(new FileInputStream(this.propName));
    }
    catch (FileNotFoundException e)
    {
      getLogger().log(Level.INFO, "whereIs.properties doesnt exist!");
    }
    catch (Exception e1)
    {
      e1.printStackTrace();
    }
  }
  
  private void setprop(String key, String value)
  {
    this.prop.setProperty(key, value);
  }
  
  private String readprop(String pName, String dName)
  {
    return this.prop.getProperty(pName, dName);
  }
  
  private String serverName(String sn)
  {
    return readprop(sn, sn);
  }
  
  @SuppressWarnings("deprecation")
private void commandListener(CommandSender sender, String[] args)
  {
    if (args.length == 0) {
      sender.sendMessage(ChatColor.GOLD + "Использование: /f <имена игроков>");
    } else {
      for (int i = 0; i < args.length; i++)
      {
        ProxiedPlayer p1 = getProxy().getPlayer(args[i]);
        if (p1 != null)
        {
          sender.sendMessage(ChatColor.GOLD + args[i] + ChatColor.GREEN + " онлайн" + ChatColor.RESET + " " + readprop("bungeeWhereIs_separator", "на") + " " + ChatColor.BLUE + serverName(p1.getServer().getInfo().getName().toString()));
        }
        else
        {
          sender.sendMessage(ChatColor.GOLD + args[i] + ChatColor.RED + " оффлайн");
        }
      }
    }
  }
  
  public void storeprop()
  {
    try
    {
      this.prop.store(new FileOutputStream(this.propName), null);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  @SuppressWarnings("deprecation")
  public void onEnable()
  {
    ProxyServer.getInstance().getPluginManager().registerCommand(this, new Command("whereis")
    {
      public void execute(CommandSender sender, String[] args)
      {
        BungeeWhereIs.this.commandListener(sender, args);
      }
    });
    ProxyServer.getInstance().getPluginManager().registerCommand(this, new Command("f")
    {
      public void execute(CommandSender sender, String[] args)
      {
        BungeeWhereIs.this.commandListener(sender, args);
      }
    });
    ProxyServer.getInstance().getPluginManager().registerCommand(this, new Command("find")
    {
      public void execute(CommandSender sender, String[] args)
      {
        BungeeWhereIs.this.commandListener(sender, args);
      }
    });
    ProxyServer.getInstance().getPluginManager().registerCommand(this, new Command("ssn", "whereis.admin", new String[0])
    {
      public void execute(CommandSender sender, String[] args)
      {
        if (args.length == 2) {
          BungeeWhereIs.this.setprop(args[0], args[1]);
        } else {
          sender.sendMessage("А надо так: /ssn <serverName> <visibleName>");
        }
      }
    });
    ProxyServer.getInstance().getPluginManager().registerCommand(this, new Command("cns", "whereis.admin", new String[0])
    {
	public void execute(CommandSender sender, String[] args)
      {
        if (args.length == 1) {
          BungeeWhereIs.this.setprop("bungeeWhereIs_separator", args[0]);
        } else {
          sender.sendMessage("А надо так: /cns <separator>");
        }
      }
    });
  }
  
  public void onDisable()
  {
    storeprop();
  }
}
