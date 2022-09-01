package dev.nosehad.varos.commands;

import dev.nosehad.varos.utils.console;
import dev.nosehad.varos.varos.Varos;
import net.md_5.bungee.api.ChatColor;
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
import java.util.Objects;

public class timerCMD implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand ( @NotNull CommandSender sender , @NotNull Command cmd , @NotNull String label , String[] args ) {
        if ( sender instanceof Player player ) {
            if ( args.length == 1 ) {
                if( Objects.equals ( args[0] , "toggle" ) ) {
                    Varos server = Varos.current;
                    if(server.timerToggled ( player )) {
                        server.toggleTimer ( player );
                        console.send ( player, "§7Du hast den Timer §cdeaktiviert§7." );
                    } else {
                        server.toggleTimer ( player );
                        console.send ( player, "§7Du hast den Timer §aaktiviert§7." );
                    }
                }
            }
            else {
                console.send ( player, "§7Benutze /Timer toggle um dir den Timer anzeigen zu lassen. " );
            }
        } else {
            Bukkit.getLogger ().info ( "Fehlgeschlagen, dieser Command kann nur als Spieler ausgeführt werden." );
        }
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete ( @NotNull CommandSender sender , @NotNull Command command , @NotNull String alias , @NotNull String[] args ) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<> ();
            arguments.add ( "toggle" );
            return arguments;
        }
        return null;
    }
}
