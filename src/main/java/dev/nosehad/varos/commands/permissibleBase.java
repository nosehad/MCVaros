package dev.nosehad.varos.commands;

import dev.nosehad.varos.Main;
import dev.nosehad.varos.varos.Varos;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class permissibleBase {
    static permissibleBase current;

    public static permissibleBase get() {
        if(current == null) {
            current = new permissibleBase ();
        }
        return current;
    }

    ArrayList<UUID> allowedPlayers = new ArrayList<UUID> ();

    public permissibleBase() {
    };

    public boolean verified( Player player ) {
        return allowedPlayers.contains ( player.getUniqueId () );
    }

    public boolean verify(Player player, String passwd) {
        if( Varos.current.admin_passwd.toLowerCase ().equals ( passwd.toLowerCase () ) ) {
            allowedPlayers.add ( player.getUniqueId ( ) );
            return true;
        }
        return false;
    }
}
