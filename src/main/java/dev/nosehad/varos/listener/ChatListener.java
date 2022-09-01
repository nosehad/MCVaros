package dev.nosehad.varos.listener;

import dev.nosehad.varos.varos.Varos;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler
    public void onPlayerChat( AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = p.hasPermission("Chat.color") ? ChatColor.translateAlternateColorCodes( '&', e.getMessage()) : e.getMessage();
        msg = msg.replaceAll("%", "§");
        if( Varos.current.isInTeam(p) ) {
            e.setFormat(  ChatColor.of(Varos.current.getTeam ( p ).getColor ()) + p.getName () + " §7»§7 " + msg);
        } else {
            e.setFormat( ChatColor.DARK_GRAY + p.getName () + " §7»§7 " + msg);
        }
    }
}
