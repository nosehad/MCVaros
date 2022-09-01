package dev.nosehad.varos.commands;

import dev.nosehad.varos.utils.GarbageCleaner;
import dev.nosehad.varos.utils.console;
import dev.nosehad.varos.varos.Varos;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class varosCMD implements CommandExecutor, TabExecutor {
    String usage = "§7Usage: /§avaros §asetspawn§7(sets the spawn), §7[§aworldborder §7-> §asetspeed§7(sets the shrinking speed of the worldborder), §aset§7(changes the radius of the worldborder)], §astart§7(starts the event and teleports everyone to spawn), §areset§7(resets the event), [§atimer §7-> §apause§7, §aresume§7, §areset§7]";
    ArrayList<Player> allowedPlayers = new ArrayList<Player>();

    @Override
    public boolean onCommand ( @NotNull CommandSender sender , @NotNull Command command , @NotNull String label , @NotNull String[] args ) {
        if(sender instanceof Player p ) {
            if ( permissibleBase.get ().verified ( p ) ) {
                switch (args.length) {
                    case 0:
                        console.send ( p, usage );
                        break;
                    case 1:
                        switch (args[0]) {
                            case "start":
                                if ( Varos.current != null ) {
                                    Varos.current.start ( );
                                    console.send ( p , "§7Varos is being started." );
                                }
                                else {
                                    console.send ( p , "§7Action failed, the server wasn't started." );
                                }
                                break;
                            case "reset":
                                if ( Varos.current != null ) {
                                    console.send ( p , "§7The event is being reset, this can take a while." );
                                    Varos.current.delete ( );
                                }
                                else {
                                    console.send ( p , "§7Action failed, the server wasn't started." );
                                }
                                break;
                            case "setspawn":
                                if ( Varos.current != null ) {
                                    Location loc = p.getLocation ( );
                                    console.send ( p , "§7The spawn was set at " + loc.getX ( ) + " " + loc.getY ( ) + " " + loc.getZ ( ) + "." );
                                    Varos.current.setSpawn ( loc );
                                }
                                else {
                                    console.send ( p , "§7Action failed, the server wasn't started." );
                                }
                                break;
                            default:
                                console.send ( p , usage );
                                break;
                        }
                        break;
                    case 2:
                        if(args[0].equals ("timer")) {
                            if ( Varos.current != null ) {
                                Varos server = Varos.current;
                                switch (args[1]) {
                                    case "pause" -> {
                                        server.pause ( );
                                        console.send ( p , "§7Timer was §apaused§7." );
                                    }
                                    case "resume" -> {
                                        server.resume ( );
                                        console.send ( p , "§7Timer was §aresumed§7." );
                                    }
                                    case "reset" -> {
                                        server.reset ( );
                                        console.send ( p , "§7Timer was §areset§7." );
                                    }
                                }
                            } else {
                                console.send ( p, "§7Action failed, the server wasn't started." );
                            }
                        } else {
                            console.send ( p, usage );
                        }
                        break;
                    case 3:
                        if(args[0].equals ("worldborder")) {
                            if(args[1].equals ( "set" )) {
                                if ( Varos.current != null ) {
                                    Varos.current.setWorldBorder ( Integer.parseInt ( args[2] ) );
                                    console.send ( p, "§7The worldborder was set to §a" + args[2] + "§7.");
                                } else {
                                    console.send ( p, "§7Action failed, the server wasn't started." );
                                }
                            }
                            else if(args[1].equals ( "setspeed" )) {
                                if ( Varos.current != null ) {
                                    console.send ( p, "§7The worldborder's shrinking rate was set to §a" + args[2] + "§7.");
                                    Varos.current.setShrinkingRate ( Integer.parseInt ( args[2] ) );
                                } else {
                                    console.send ( p, "§7Action failed, the server wasn't started." );
                                }
                            }
                        } else {
                            console.send ( p, usage );
                        }
                        break;
                    default:
                        console.send ( p , usage );
                        break;
                }

            } else {
                console.send ( p, "§7You do not have permission to execute this command. Type §a/verify <password> §7to change that." );
            }
        } else {
            Bukkit.getLogger ().info ("Action failed, this command can only be executed by a player." );
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete ( @NotNull CommandSender sender , @NotNull Command command , @NotNull String alias , @NotNull String[] args ) {
        if ( sender instanceof Player p ) {
            if ( permissibleBase.get ().verified ( p ) ) {
                if ( args.length == 1 ) {
                    List<String> arguments = new ArrayList<> ( );
                    arguments.add ( "worldborder" );
                    arguments.add ( "start" );
                    arguments.add ( "reset" );
                    arguments.add ( "timer" );
                    arguments.add ( "setspawn" );
                    return arguments;
                } else if ( args.length == 2 ) {
                    if( args[0].equals ( "worldborder" ) ) {
                        List<String> arguments = new ArrayList<> ( );
                        arguments.add ( "setspeed" );
                        arguments.add ( "set" );
                        return arguments;
                    }
                    else if( args[0].equals ( "timer" ) ) {
                        List<String> arguments = new ArrayList<> ( );
                        arguments.add ( "pause" );
                        arguments.add ( "resume" );
                        arguments.add ( "reset" );
                        return arguments;
                    }
                }
            }
        }
        return null;
    }
}

