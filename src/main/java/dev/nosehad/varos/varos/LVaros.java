package dev.nosehad.varos.varos;

import dev.nosehad.varos.utils.console;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.UUID;

// I have to use deprecated methods to use custom color codes
public class LVaros implements Listener {

    //event handlers
    @EventHandler
    public void onDeath( PlayerDeathEvent event )
    {
        Varos server = Varos.current;
        Player p = event.getPlayer ();
        UUID u = p.getUniqueId ();

        if(server.getLifes ( u ) <= 1) {
            server.decreaseLife ( u );
            p.playSound ( p.getLocation ( ) , Sound.ENTITY_PLAYER_DEATH , 1 , 1 );
            p.setGameMode ( GameMode.SPECTATOR );
            console.send ( p, "§cYou died and lost your last live :(");
            Firework fw = ( Firework )  p.getWorld ().spawnEntity ( p.getLocation ( ) , EntityType.FIREWORK );
            ItemStack is = new ItemStack ( Material.FIREWORK_ROCKET , 1 );
            FireworkMeta fm = ( FireworkMeta ) is.getItemMeta ( );
            fm.setPower ( 5 );
            fm.addEffect ( FireworkEffect.builder ( ).withColor ( Color.RED ).with ( FireworkEffect.Type.CREEPER ).withTrail ( ).build ( ) );
            fw.setFireworkMeta ( fm );
        } else {
            server.decreaseLife ( u );
            p.playSound ( p.getLocation ( ) , Sound.ENTITY_PLAYER_DEATH , 1 , 1 );
            console.send ( p, "§7You died and have " + server.getLifeString ( u ) + " lives left." );
            boolean success = false;
            if ( server.allow_bed_respawn ) {
                if(p.getBedSpawnLocation () != null) {
                    success = true;
                    p.getActivePotionEffects ().forEach ( effect -> {
                        p.removePotionEffect ( effect.getType () );
                    } );
                    p.setHealth ( 20 );
                    p.setSaturation ( 20 );
                    p.setFoodLevel ( 20 );
                    p.teleport ( p.getBedSpawnLocation () );
                }
            } if(!success) {
                p.getActivePotionEffects ().forEach ( effect -> {
                    p.removePotionEffect ( effect.getType () );
                } );
                p.setHealth ( 20 );
                p.setSaturation ( 20 );
                p.setFoodLevel ( 20 );
                server.getSpawn ().tp ( p );
            }
        }
        event.setCancelled ( true );
    }

    @EventHandler
    public void onJoin( PlayerJoinEvent event ) {
        Player player = event.getPlayer ();
        nosehad.setupData ( event.getPlayer () );
        if( Varos.current.isInTeam(player) ) {
            event.setJoinMessage ( "§a+ §8" + player.getName () + " §7(" + ChatColor.of(Varos.current.getTeam ( player ).getColor ()) + Varos.current.getTeam ( player ).getColor () + "§7)" );
        } else {
            event.setJoinMessage ( "§a+ §8" + player.getName () + " §7(§8NONE§7)" );
        }
    }

    @EventHandler
    public void onQuit( PlayerQuitEvent event ) {        Player player = event.getPlayer ();
        if( Varos.current.isInTeam(player) ) {
            event.setQuitMessage ("§c- §8" + player.getName () + " §7(" + ChatColor.of( Varos.current.getTeam ( player ).getColor ()) + Varos.current.getTeam ( player ).getColor () + "§7)" );
        } else {
            event.setQuitMessage (  "§c- §8" + player.getName () + " §7(§8NONE§7)" );
        }
    }
}
