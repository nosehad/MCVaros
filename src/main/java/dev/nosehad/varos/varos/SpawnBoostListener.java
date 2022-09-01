package dev.nosehad.varos.varos;

import dev.nosehad.varos.Main;
import dev.nosehad.varos.utils.Math;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.KeybindComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;


public class SpawnBoostListener implements Listener {
    private final Varos server;
    private final List<Player> flying = new ArrayList<>();

    public SpawnBoostListener () {
        this.server = Varos.current;
        Bukkit.getScheduler().runTaskTimer( Main .getInstance (), () -> Bukkit.getServer ().getOnlinePlayers ().forEach( ( player) -> {
            if (player.getGameMode() == GameMode.SURVIVAL) {
                Location location = player.getLocation ();
                player.setAllowFlight( Math.inRange ( location.getX ( ) , server.getSpawn ( ).getLocation ( ).getX ( ) , server.spawnradius ) && Math.inRange ( location.getY ( ) , server.getSpawn ( ).getLocation ( ).getY ( ) , server.spawnradius ) && Math.inRange ( location.getZ ( ) , server.getSpawn ( ).getLocation ( ).getZ ( ) , server.spawnradius ));
                if (this.flying.contains(player) && !player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir()) {
                    player.setAllowFlight(false);
                    player.setGliding(false);
                    Bukkit.getScheduler().runTaskLater( Main.getInstance (), () -> this.flying.remove( player) , 1L);
                }

            }
        }) , 0L, 3L);
    }

    @EventHandler
    public void onDoubleJump(PlayerToggleFlightEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            event.setCancelled(true);
            event.getPlayer().setGliding(true);
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, (new ComponentBuilder("Drücke ")).append(new KeybindComponent("key.swapOffhand")).append(" um dich zu boosten!").create());
            this.flying.add(event.getPlayer());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && (event.getCause() == DamageCause.FALL || event.getCause() == DamageCause.FLY_INTO_WALL) && this.flying.contains((Player) event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSwapItem(PlayerSwapHandItemsEvent event) {
            if (event.getPlayer().isGliding()) {
                event.getPlayer ( ).setVelocity ( event.getPlayer ( ).getLocation ( ).getDirection ( ).multiply ( 5 ) );
            }
            event.getPlayer().setFoodLevel(20);
            event.setCancelled(true);
    }


    @EventHandler
    public void onToggleGlide(EntityToggleGlideEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && this.flying.contains((Player ) event.getEntity())) {
            event.setCancelled(true);
        }

    }
}

