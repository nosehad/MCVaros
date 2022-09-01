package dev.nosehad.varos.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Position implements Serializable {
    public static Position valueOf( Location location ) {
        return new Position ( location.getWorld (), location.getX (), location.getY (), location.getZ (), location.getYaw (), location.getPitch ( ) );
    }

    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float pitch;
    private final float yaw;

    public Position( @NotNull final World world, final double x, final double y, final double z, final float yaw, final float pitch) {
        this.world = world.getName ();

        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public void tp( Player player ) {
        player.teleport ( new Location ( Bukkit.getWorld ( this.world ) , this.x, this.y , this.z , this.yaw , this.pitch ) );
    }

    public Location getLocation () {
        return new Location ( Bukkit.getWorld ( this.world ) , this.x, this.y , this.z , this.yaw , this.pitch );
    }
}
