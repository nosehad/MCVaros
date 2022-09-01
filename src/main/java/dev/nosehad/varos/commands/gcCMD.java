package dev.nosehad.varos.commands;

import com.sun.tools.jconsole.JConsoleContext;
import dev.nosehad.varos.utils.GarbageCleaner;
import dev.nosehad.varos.utils.console;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class gcCMD implements CommandExecutor, TabExecutor {
    GarbageCleaner cleaner;

    @Override
    public boolean onCommand ( @NotNull CommandSender sender , @NotNull Command command , @NotNull String label , @NotNull String[] args ) {
        if(sender instanceof Player p ) {
            if ( permissibleBase.get ().verified ( p ) ) {
                switch (args.length) {
                    case 0:
                        if ( cleaner != null ) {
                            cleaner.delete ();
                            cleaner = null;
                            console.send ( p , "§cGarbage Cleaner deactivated." );
                        }
                        else {
                            console.send ( p , "§aGarbage Cleaner activated." );
                            cleaner = new GarbageCleaner ( );
                        }
                        break;
                    case 1:
                        console.send ( p , "§cError: Please enter the speed as floating point value." );
                        break;
                    case 2:
                        if ( args[0].equals ( "set_interval" ) ) {
                            cleaner.setSpeed ( Float.parseFloat ( args[1] ) );
                            console.send ( p, "§7Speed was set to " + args[1] + "!" );
                        }
                        else {
                            console.send ( p , "§c/gc optional: set_interval <speed as floating point>" );
                        }
                        break;
                    default:
                        console.send ( p , "§c/gc optional: set_interval <speed as floating point>" );
                        break;
                }

            } else {
                console.send ( p, "§7You do not have permission to execute this command. Type §a/verify <password> §7to change that." );
            }
        } else {
            Bukkit.getLogger ().info ( "Action failed, this command can only be executed by a player." );
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete ( @NotNull CommandSender sender , @NotNull Command command , @NotNull String alias , @NotNull String[] args ) {
        if ( sender instanceof Player p ) {
            if ( permissibleBase.get ().verified ( p ) ) {
                if ( args.length == 1 ) {
                    List<String> arguments = new ArrayList<> ( );
                    arguments.add ( "set_interval" );
                    return arguments;
                }
            }
        }
        return null;
    }
}

