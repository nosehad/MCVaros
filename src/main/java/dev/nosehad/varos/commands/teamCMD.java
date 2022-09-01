package dev.nosehad.varos.commands;

import dev.nosehad.varos.utils.console;
import dev.nosehad.varos.varos.NoseTeam;
import dev.nosehad.varos.varos.Varos;
import dev.nosehad.varos.varos.nosehad;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class teamCMD implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand ( @NotNull CommandSender sender , @NotNull Command cmd , @NotNull String label , String[] args ) {
        if ( sender instanceof Player player ) {
            Varos server = Varos.current;
            if ( args.length == 1 ) {
                switch (args[0]) {
                    case "create":
                        if ( !server.isInTeam ( player ) ) {
                            server.createTeam ( player );
                            console.send ( player , "§7You have created your team." );
                        }
                        else {
                            console.send ( player , "§7You §ccannot §7create a team, since you are already in one." );
                        }
                        break;
                    case "leave":
                        if ( server.isInTeam ( player ) ) {
                            server.getTeam ( player ).leave ( player );
                            console.send ( player , "§7You've left the team." );
                        }
                        else {
                            console.send ( player , "§7You cannot leave a team, if you aren't member of one." );
                        }
                        break;
                    case "list":
                        if ( server.isInTeam ( player ) ) {
                            console.send ( player , "§7Players of your team §7(" + ChatColor.of( Varos.current.getTeam ( player ).getColor ()) + Varos.current.getTeam ( player ).getColor () + "§7):" );
                            NoseTeam t = server.getTeam ( player );
                            t.getPlayers ( ).forEach ( uuid -> {
                                if ( uuid == t.getCreator ( ).getUniqueId ( ) ) {
                                    player.sendMessage ( "    §7-§f" + Bukkit.getOfflinePlayer ( uuid ).getName ( ) + " §7(§dCreator§7)" );
                                }
                                else {
                                    player.sendMessage ( "    §7-§f" + Bukkit.getOfflinePlayer ( uuid ).getName ( ) + " §7(§6Member§7)" );
                                }
                            } );
                        }
                        else {
                            console.send ( player , "§7You have to be member of a team to do that." );
                        }
                        break;
                }
            }
            else if( args.length == 2 ) {
                switch (args[0]) {
                    case "accept":
                        if ( server.isInTeam ( player ) ) {
                            if(Bukkit.getPlayer ( args[1] ) != null) {
                                server.getTeam ( player ).acceptRequest ( Objects.requireNonNull ( Bukkit.getPlayer ( args[1] ) ).getUniqueId () );
                            } else {
                                console.send ( player, "§7Player §a" + args[1] + " §7wasn't found." );
                            }
                        } else {
                            console.send ( player , "§7You have to be a team leader to do that." );
                        }
                        break;
                    case "kick":
                        if( server.isInTeam ( player ) ){
                            NoseTeam team = server.getTeam ( player );
                            if(team.getCreator () == player) {
                                if(Bukkit.getPlayer ( args[1] ) != null) {
                                    if(team.getPlayers ().contains ( player.getUniqueId () )) {
                                        team.leave ( Objects.requireNonNull ( Bukkit.getPlayer ( args[1] ) ) );
                                        console.send ( player , "§7You have kicked §a" + args[1] + "§7 from your team." );
                                        console.send ( Objects.requireNonNull ( Bukkit.getPlayer ( args[1] ) ) , "§a" + player.getName () + " has removed you from the team." );
                                    } else {
                                        console.send ( player , "§7You cannot do that, since §a " + args[1] + " is not in your team." );
                                    }
                                } else {
                                console.send ( player , "§7You cannot do that, since §a" + args[1] + " wasn't found." );
                                }
                            } else {
                                console.send ( player , "§7You cannot do that, since you aren't team leader" );
                            }
                        } else {
                            console.send ( player , "§7You cannot do that, since you don't have a team." );
                        }
                        break;
                    case "join":
                        if(! server.isInTeam ( player )) {
                            if ( Bukkit.getPlayer ( args[1] ) != null ) {
                                Player target = Bukkit.getPlayer ( args[1] );
                                if ( server.getTeam ( target ) != null ) {
                                    server.getTeam ( target ).joinRequest ( player );
                                } else {
                                    console.send ( player , "§7You cannot do that, since " + args[1] + " has no team." );
                                }
                            } else {
                                console.send ( player , "§7You cannot do that, since " + args[1] + " wasn't found." );
                            }
                        } else {
                            console.send ( player , "§7You cannot do that, since you are already member of a team. Type §a/team §cleave §7to leave/disband your current team." );
                        }
                        break;
                    default:
                        console.send ( player ,
                                       "§7Use /team create -> to create a team\n" +
                                               "             -/team leave -> to leave/disband your current team\n" +
                                               "             -/team list -> to see who is in your team\n" +
                                               "             -/team accept <player> -> to accept the join request from a player\n" +
                                               "             -/team kick <player> -> to remove a player from your team\n" +
                                               "             -/team join <player> -> to send a join request to a players team" );
                        break;
                }
            }
            else {
                console.send ( player , "§7Use /team create -> to create a team\n" +
                        "             -/team leave -> to leave/disband your current team\n" +
                        "             -/team list -> to see who is in your team\n" +
                        "             -/team accept <player> -> to accept the join request from a player\n" +
                        "             -/team kick <player> -> to remove a player from your team\n" +
                        "             -/team join <player> -> to send a join request to a players team" );
            }

            // at the end refresh teams for player
            if(server.isInTeam ( player )) {
                server.getTeam ( player ).setEntries ( );
            }

            //refresh tablist
            Bukkit.getOnlinePlayers ().forEach ( nosehad::setupScoreboard );
        }
        else {
            Bukkit.getLogger ( ).info ( "Action failed, this command is only available ingame." );
        }
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete ( @NotNull CommandSender sender , @NotNull Command command , @NotNull String alias , @NotNull String[] args ) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<> ();
            arguments.add ( "create" );
            arguments.add ( "leave" );
            arguments.add ( "list" );
            arguments.add ( "accept" );
            arguments.add ( "join" );
            arguments.add ( "kick" );
            return arguments;
        }
        return null;
    }
}
