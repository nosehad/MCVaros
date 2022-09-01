package dev.nosehad.varos.varos;


import dev.nosehad.varos.User.userdata;
import dev.nosehad.varos.utils.Math;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class ProtectionListener implements Listener {

    @EventHandler
    public void onDamage( EntityDamageEvent event ) {
        if ( !Varos.current.hasStarted () ) {
            event.setCancelled ( true );
        } else if (Varos.current.protection) {
            if(event.getEntity ().getType () == EntityType.PLAYER)
            event.setCancelled ( true );
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if ( !Varos.current.hasStarted () ) {
            Varos s = Varos.current;
            Player p = event.getPlayer ( );
            Location location = p.getLocation ( );
            if ( !(Math.inRange ( location.getX ( ) , s.getSpawn ().getLocation ( ).getX ( ) , s.spawnradius ) && Math.inRange ( location.getY ( ) , s.getSpawn ().getLocation ( ).getY ( ) , s.spawnradius ) && Math.inRange ( location.getZ ( ) , s.getSpawn ().getLocation ( ).getZ ( ) , s.spawnradius )) ) {
                event.setCancelled ( true );
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEvent ( PlayerInteractEvent event ) {
        if ( !Varos.current.hasStarted ( ) ) {
            event.setCancelled ( true );
        }
    }

    @EventHandler
    public void onBreak ( BlockBreakEvent e ) {
        if ( !Varos.current.hasStarted ( ) ) {
            e.setCancelled ( true );
        }
    }

    @EventHandler
    public void onPlace ( BlockPlaceEvent e ) {
        if ( !Varos.current.hasStarted ( ) ) {
            e.setCancelled ( true );
        }
    }

    @EventHandler
    public void onClick ( InventoryClickEvent e ) {
        if ( !Varos.current.hasStarted ( ) ) {
            e.setCancelled ( true );
        }
    }

    @EventHandler
    public void onPickup ( PlayerAttemptPickupItemEvent e ) {
        if ( !Varos.current.hasStarted ( ) ) {
            e.setCancelled ( true );
        }
    }

    @EventHandler
    public void onDrop ( PlayerDropItemEvent e ) {
        if ( !Varos.current.hasStarted ( ) ) {
            e.setCancelled ( true );
        }
    }

    @EventHandler
    public void onFood ( FoodLevelChangeEvent e ) {
        if ( e.getEntity ( ).getType ( ) == EntityType.PLAYER ) {
            if ( !Varos.current.hasStarted ( ) ) {
                e.setCancelled ( true );
            }
        }
    }

    @EventHandler
    public void onNetherEntry ( PlayerPortalEvent e ) {
        e.setCancelled ( true );
    }
}


