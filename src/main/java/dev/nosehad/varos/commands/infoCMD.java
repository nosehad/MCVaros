package dev.nosehad.varos.commands;

import dev.nosehad.varos.utils.console;
import dev.nosehad.varos.varos.Varos;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class infoCMD implements CommandExecutor {

    @Override
    public boolean onCommand ( @NotNull CommandSender sender , @NotNull Command command , @NotNull String label , @NotNull String[] args ) {
        if(sender instanceof Player p ) {
            Varos var = Varos.current;
            if( var.infoToggled ( p ) ) {
                var.toggleInfo ( p );
                console.send ( p, "§7Scoreboard §cdeactivated§7." );
            } else {
                var.toggleInfo ( p );
                console.send ( p, "§7Scoreboard §aactivated§7." );
            }
        } else {
            Bukkit.getLogger ().info ( "Action failed, this command can only be executed by a player." );
        }
        return false;
    }
}

