package dev.nosehad.varos.commands;

import dev.nosehad.varos.utils.console;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class verifyCMD implements CommandExecutor{

    @Override
    public boolean onCommand ( @NotNull CommandSender sender , @NotNull Command command , @NotNull String label , @NotNull String[] args ) {
        if(sender instanceof Player p ) {
            if ( permissibleBase.get ().verified ( p ) ) {
                console.send ( p, "§7You are already verified!" );
            } else {
                if (args.length != 1) {
                    console.send ( p , "§7Usage: /verify <password>" );
                } else {
                    if(permissibleBase.get ().verify ( p, args[0] ) ) {
                        console.send ( p, "§aYou§7've successfully verified yourself!" );
                    } else {
                        console.send ( p, "§7Action §cfailed, wrong password." );
                    }
                }
            }
        } else {
            Bukkit.getLogger ().info ( "Action failed, this command can only be executed by a player." );
        }
        return false;
    }
}

