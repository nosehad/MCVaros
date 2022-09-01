package dev.nosehad.varos.varos;

import dev.nosehad.varos.Main;
import dev.nosehad.varos.User.userdata;
import dev.nosehad.varos.packet.ReflectionPayloadPacket;
import dev.nosehad.varos.scoreboard.VarosScoreboard;
import dev.nosehad.varos.utils.randomHash;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public interface nosehad {
    static void setupScoreboard(Player user) {
        Varos server = Varos.current;
        Scoreboard scoreboard = user.getScoreboard();
        scoreboard.getTeams ().forEach ( Team::unregister );
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(server.isInTeam ( player )) {
                NoseTeam noseTeam = server.getTeam ( player );
                String color = noseTeam.getColor ();
                Team team = scoreboard.registerNewTeam ( randomHash.generate ( ) );
                team.color ( NamedTextColor.nearestTo ( Objects.requireNonNull ( TextColor.fromHexString ( color ) ) ) );
                team.suffix ( Component.text ( " " + color, TextColor.fromHexString ( color ) ) );
                Bukkit.getLogger ().info ( "creating: " + team.getName () + " player: " + player.getName ()  );
                team.addEntry ( player.getName () );
            }
        }
    }

    /*
            players.forEach ( uuid -> {
            if(Bukkit.getPlayer ( uuid ) != null) {
               Player player = Bukkit.getPlayer ( uuid );
               Scoreboard scoreboard = Bukkit.getServer ().getScoreboardManager ().getMainScoreboard ();
               Team team = scoreboard.getTeam ( name );
               if ( team == null ) {
                   team = scoreboard.registerNewTeam ( name );
                   team.setAllowFriendlyFire ( false );
                   team.setCanSeeFriendlyInvisibles ( true );
                   team.color ( NamedTextColor.nearestTo ( Objects.requireNonNull ( TextColor.fromHexString ( color ) ) ) );
                   team.suffix (Component.text ( " " + color, TextColor.fromHexString ( color ) ) );
               }

                assert player != null;
                Bukkit.getServer ().getScoreboardManager ().getMainScoreboard ().getTeams ().forEach ( team1 -> {
                    if(team1.getEntries ().contains ( player.getName () )) {
                        team1.removeEntry ( player.getName () );
                    }
                } );
                team.addEntry ( player.getName () );
            }
        } );
     */

    static void setupData(Player player) {
        Varos server = Varos.current;
        userdata u = null;

        // send brand
        new ReflectionPayloadPacket ( Main.getInstance () ).send ( player, "nosehad" );

        //register if not already done
        if ( !server.registeredPlayers.contains ( player.getUniqueId ( ) ) ) {

            //reset data
            player.setBedSpawnLocation ( null );
            player.getInventory ( ).clear ( );
            player.getActivePotionEffects ( ).forEach ( effect -> {
                player.removePotionEffect ( effect.getType ( ) );
            } );
            player.setScoreboard ( Bukkit.getScoreboardManager ( ).getNewScoreboard ( ) );
            player.setHealth ( 20 );
            player.setSaturation ( 20 );
            player.setFoodLevel ( 20 );
            server.getSpawn ( ).tp ( player );
            server.register ( player );

            //reset teams
            Bukkit.getServer ().getScoreboardManager ().getMainScoreboard ().getTeams ().forEach ( team1 -> {
                if(team1.getEntries ().contains ( player.getName () )) {
                    team1.removeEntry ( player.getName () );
                }
            } );
        }

        if(server.old_combat) {
            Objects.requireNonNull ( player.getAttribute ( Attribute.GENERIC_ATTACK_SPEED ) ).setBaseValue ( 100D );
        } else {
            Objects.requireNonNull ( player.getAttribute ( Attribute.GENERIC_ATTACK_SPEED ) ).setBaseValue ( 4D );
        }

        if ( userdata.getData ( player.getUniqueId ( ) ) != null ) {
            u = userdata.getData ( player.getUniqueId ( ) );
        }
        else {
            u = new userdata ( player.getUniqueId ( ) );
        }

        //protection
        u.setProtection ( server.protection );

        //scoreboard
        if ( Objects.equals ( u.getAdditionalString ( ) , "info" ) ) {
            new VarosScoreboard ( player );
        }

        //load teams
        Bukkit.getOnlinePlayers ().forEach ( nosehad::setupScoreboard );
    }
}

