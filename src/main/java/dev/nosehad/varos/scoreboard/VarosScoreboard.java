package dev.nosehad.varos.scoreboard;

import dev.nosehad.varos.varos.Varos;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class VarosScoreboard extends ScoreboardBuilder {
    public VarosScoreboard ( Player player) {
            super ( player , ( net.md_5.bungee.api.ChatColor.of( "#0000fc") +
                    "V"  + net.md_5.bungee.api.ChatColor.of( "#3f00fc") +
                    "A"  + net.md_5.bungee.api.ChatColor.of( "#7f00fd") +
                    "R"  + net.md_5.bungee.api.ChatColor.of( "#bf00fe") +
                    "O"  + net.md_5.bungee.api.ChatColor.of( "#ff00ff") +
                    "S"));
    }

    public void createScoreboard() {
        this.setScore("§4 ", 11);
        this.setScore("§7Worldborder:", 10);
        this.setScore( "§7 > §cX§9Z§7: §f" + Varos.current.getWorldBorder () , 9);
        this.setScore("§7 ", 8);
        this.setScore("§7Your Lives: ", 7);
        if(Varos.current.playerLifes.get ( player.getUniqueId () ) >= 1) {
            this.setScore ( "§7 > §c" + Varos.current.getLifeString ( player.getUniqueId ( ) ) , 6 );
        } else {
            this.setScore ( "§7 > §8☠" , 6 );
        }
        this.setScore("§1 ", 5);
        this.setScore("§7Protection:", 4);
        this.setScore("§7 > §b" + Varos.current.getProtectionTimeString (), 3);
        this.setScore("§f ", 2);
        this.setScore(ChatColor.DARK_PURPLE+ "~Varos 0.0.3", 1);
    }
}
