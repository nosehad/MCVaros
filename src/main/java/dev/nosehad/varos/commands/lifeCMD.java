package dev.nosehad.varos.commands;

import dev.nosehad.varos.utils.console;
import dev.nosehad.varos.varos.Varos;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class lifeCMD implements CommandExecutor {
    @Override
    public boolean onCommand ( @NotNull CommandSender sender , @NotNull Command command , @NotNull String label , @NotNull String[] args ) {
        if ( sender instanceof Player player ) {
            Varos server = Varos.current;
            console.send ( player, "§7You have " + server.getLifeString ( player.getUniqueId () ) + " §7lifes left.");
        } else {
            Bukkit.getLogger ().info ( "Action failed, this command can only be executed by a player." );
        }
        return false;
    }
}
