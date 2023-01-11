package dev.nosehad.varos;

import dev.nosehad.varos.commands.*;
import dev.nosehad.varos.listener.ChatListener;
import dev.nosehad.varos.listener.joinListener;
import dev.nosehad.varos.utils.YMLConfiguration;
import dev.nosehad.varos.varos.LVaros;
import dev.nosehad.varos.varos.ProtectionListener;
import dev.nosehad.varos.varos.SpawnBoostListener;
import dev.nosehad.varos.varos.Varos;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class Main extends JavaPlugin {
    static Main instance;
    public final static String prefix = "§d§lVAROS§r §8• §7";

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable () {
        this.getCommand ( "team" ).setExecutor(new teamCMD ());
        this.getCommand ( "teams" ).setExecutor(new teamCMD ());
        this.getCommand ( "gc" ).setExecutor ( new gcCMD () );
        this.getCommand ( "info" ).setExecutor ( new infoCMD () );
        this.getCommand ( "Timer" ).setExecutor ( new timerCMD () );
        this.getCommand ( "verify" ).setExecutor ( new verifyCMD () );
        this.getCommand ( "live" ).setExecutor(new lifeCMD () );
        this.getCommand ( "lives" ).setExecutor(new lifeCMD () );
        this.getCommand ( "leben" ).setExecutor(new lifeCMD () );
        this.getCommand ( "varos" ).setExecutor(new varosCMD () );
        this.getCommand ( "spawn" ).setExecutor(new spawnCMD () );
        this.getCommand ( "l" ).setExecutor(new spawnCMD () );
        this.getCommand ( "lobby" ).setExecutor(new spawnCMD () );
        instance = this;

        setupFiles ();

        startVaros ();


        Bukkit.getPluginManager ( ).registerEvents ( new joinListener ( ) , this );
        Bukkit.getPluginManager ( ).registerEvents ( new LVaros ( ) , this );
        Bukkit.getPluginManager ( ).registerEvents ( new ProtectionListener ( ) , this );
        Bukkit.getPluginManager ( ).registerEvents ( new SpawnBoostListener () , this );
        Bukkit.getPluginManager ( ).registerEvents ( new ChatListener () , this );
        Bukkit.getLogger ( ).info ( "§aVaros 0.03 by _itznosehad_ wurde erfolgreich geladen!" );
    }

    @Override
    public void onDisable () {
        if (Varos.current != null) {
            Varos.current.save ();
        }
    }

    static void setupFiles () {
        if(!new File ( "./plugins/Varos" ).mkdir ())
            return;
        if ( Files.isReadable ( Paths.get ( "./plugins/Varos/config.yml" ) ) )
            return;
        try (FileWriter file = new FileWriter ( "./plugins/Varos/config.yml" ))
        {
            file.write ( """
                                        ## set on true to enable 1.8 combat ##
                                        old_combat: true
                                       
                                        ## /verify <admin_passwort> to be able to enter admin commands ##
                                        admin_password: example
                                         
                                        ## protection time at game start in minutes ##
                                        prot_time: 45
                                         
                                        ## radius for spawn flight ##
                                        spawnradius: 50
                                         
                                        ## amount of allowed deaths ##
                                        lives: 3
                                         
                                        ## is it allowed to respawn in beds if you have lifes left ##
                                        bed_respawn: false
                                         
                                        ## after this amount of minutes, the worldborder starts shrinking ##
                                        start_shrinking: 60
                                        
                                        ## worldborder radius at start of the game ##
                                        wordborder_radius: 5000
                                        
                                        ## smallest worldborder radius ##
                                        min_worldborder_radius: 100
                                        
                                        ## player limit per team ##
                                        max_team_players: 2
                                         
                                        ## set the spawn by using /varos setspawn ##
                                        
                                        ~Varos Plugin by _itznosehad_
                                        """ );
            file.flush ( );
        }
        catch (IOException e)
        {
            e.printStackTrace ( );
        }
    }
    public void startVaros()
    {
        YMLConfiguration configuration = new YMLConfiguration ( "./plugins/Varos/config.yml" );
        try
        {
            Bukkit.getLogger().info (
                    "Starting Varos Plugin with configuration: \n" +
                            "lives: " + configuration.getString ( "lives" )  + "\n" +
                            "spawn radius: " + configuration.getString ( "spawnradius" )  + "\n" +
                            "default worldborder radius: " + configuration.getString ( "wordborder_radius" )  + "\n" +
                            "bed respawn: " + configuration.getString ( "bed_respawn" ) + "\n" +
                            "worldborder starts shrinking after: " + configuration.getString ( "start_shrinking" ) + " minutes" + "\n" +
                            "1.8 combat: " + configuration.getString ( "old_combat" ) + "\n" +
                            "root password: " + configuration.getString ( "admin_password" ) + "\n" +
                            "max players per team: " + configuration.getString ( "max_team_players" ) + "\n" +
                            "lowest worldborder radius: " + configuration.getString ( "min_worldborder_radius" ) + "\n" +
                            "protection time: " + configuration.getString ( "prot_time" ) + " minutes"  );
            if( Files .isReadable ( Paths.get ( "./plugins/Varos/varos.dat"  ))) {
                Varos.loadRecent ();
                return;
            }
            new Varos ( Integer.parseInt ( configuration.getString ( "lives" ) ) ,
                        Integer.parseInt ( configuration.getString ( "spawnradius" ) ) ,
                        Boolean.parseBoolean ( configuration.getString ( "bed_respawn" ) ) ,
                        Integer.parseInt ( configuration.getString ( "start_shrinking" ) ) ,
                        Boolean.parseBoolean ( configuration.getString ( "old_combat" ) ) ,
                        configuration.getString ( "admin_password" ) ,
                        Integer.parseInt ( configuration.getString ( "prot_time" ) ) ,
                        Integer.parseInt ( configuration.getString ( "wordborder_radius" ) ),
                        Integer.parseInt ( configuration.getString ( "min_worldborder_radius" ) ),
                        Integer.parseInt ( configuration.getString ( "max_team_players" ) ));
        }
        catch (IOException | ExecutionException e) {
            e.printStackTrace ();
            Bukkit.getLogger ().info ( "§cWasn't able to read config. Is it corrupted?" );
        }
    }

    public static Main getInstance () {
        return instance;
    }
}
