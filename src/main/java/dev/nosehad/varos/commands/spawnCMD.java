package dev.nosehad.varos.commands;

import dev.nosehad.varos.utils.console;
import dev.nosehad.varos.varos.Varos;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class spawnCMD implements CommandExecutor {
    @Override
    public boolean onCommand ( @NotNull CommandSender sender , @NotNull Command cmd , @NotNull String label , String[] args ) {
        if ( sender instanceof Player player ) {
            if(!Varos.current.hasStarted ( )) {
                Varos.current.getSpawn ( ).tp ( player );
                console.send ( player , "§7You have been teleported to spawn" );
            } else {
                console.send ( player , "§7The Event has already started." );
            }
        } else {
            Bukkit.getLogger ().info ( "Action failed, this command can only be executed by a player." );
        }
        return false;
    }
}
