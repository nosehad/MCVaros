package dev.nosehad.varos.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class console {
    public static void send ( Player  player ,  String msg) {
        player.sendMessage ( "§8⬝§r " + ChatColor.of( "#0000fc") +
                                "V"  + ChatColor.of( "#3f00fc") +
                                "A"  + ChatColor.of( "#7f00fd") +
                                "R"  + ChatColor.of( "#bf00fe") +
                                "O"  + ChatColor.of( "#ff00ff") +
                                "S"  + " §r§8⬝ §7" + msg );
    }

    //<span class="colorize_fun"><span style="color:#0000fc;">V
    // </span><span style="color:#3f00fc;">a
    // </span><span style="color:#7f00fd;">r
    // </span><span style="color:#bf00fe;">o
    // </span><span style="color:#ff00ff;">s</span></span>
}
