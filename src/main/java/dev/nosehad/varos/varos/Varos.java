package dev.nosehad.varos.varos;

import dev.nosehad.varos.Main;
import dev.nosehad.varos.User.userdata;
import dev.nosehad.varos.scoreboard.VarosScoreboard;
import dev.nosehad.varos.utils.reset;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Varos extends AVaros implements Runnable, Serializable {
    public static Varos current;

    public static void loadRecent () {
        try{
            FileInputStream fis = new FileInputStream( new File ( "./plugins/Varos/varos.dat"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            current = (Varos) ois.readObject();
            fis.close();
            ois.close();
        } catch(Exception ex){
            ex.printStackTrace();
        }
        Thread thread = new Thread(current);
        thread.start ();
        current.timerThread.start ();

        //load userdatas
        Bukkit.getServer ().getOnlinePlayers ().forEach ( nosehad::setupData );
    }

    public Varos ( int lives  , int spawnradius , boolean allow_bed_respawn , int shrinking_after_minutes , boolean old_combat , String admin_passwd, int protection_time, int worldborder_radius, int min_worldborder_radius, int max_players_per_team  ) {
        super ( lives , spawnradius , allow_bed_respawn , shrinking_after_minutes , old_combat , admin_passwd , protection_time , worldborder_radius, min_worldborder_radius, max_players_per_team  );
        current = this;
        Thread thread = new Thread ( this );
        Bukkit.getServer ( ).getOnlinePlayers ( ).forEach ( nosehad::setupData );
        thread.start ( );
    }

    //scoreboard methods
    @Override
    public int getWorldBorder () {
        return worldBorder;
    }

    @Override
    public String getLifeString( UUID uuid ) {
        if(playerLifes.get ( uuid ) >= 1) {
            return "§c" + "♥".repeat ( Math.max ( 0 , playerLifes.get ( uuid ) ) );
        } else {
            return "§8no lifes left§7";
        }
    }
    @Override
    public String getProtectionTimeString() {
        String temp;
        if(timerThread.timer_minutes <= protection_time) {
            temp = ("§f" + (timerThread.timer_minutes - protection_time) + "§7:§f" + String.valueOf (60-timerThread.timer_seconds).replace ( "60", "00" )).replaceAll ( "-" , "" );
        }
        else {
            temp = "§cSchutzzeit abgelaufen!";
        }
        return temp;
    }

    @Override
    public void toggleInfo(Player player) {
        if(getInfoToggled ().contains ( player )) {
            player.setScoreboard( Bukkit.getScoreboardManager().getNewScoreboard() );
            userdata.getData ( player.getUniqueId () ).setAdditionalString ( "none" );
        } else {
            userdata.getData ( player.getUniqueId () ).setAdditionalString ( "info" );
            new VarosScoreboard ( player );
        }
    }
    @Override
    public boolean infoToggled(Player player) {
        return getInfoToggled ().contains ( player );
    }

    @Override
    public void toggleTimer(Player player) {
        if(timerToggled.contains ( player.getUniqueId () )) {
            timerToggled.remove ( player.getUniqueId () );
            player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "   " ) );
        } else {
            timerToggled.add ( player.getUniqueId () );
        }
    }
    @Override
    public boolean timerToggled(Player player) {
        return timerToggled.contains ( player.getUniqueId () );
    }

    @Override
    public void createTeam(Player player) {
        teams.add ( new NoseTeam ( max_players_per_team, player.getUniqueId () ) );
    }
    @Override
    public boolean isInTeam( Player player ) {
        AtomicBoolean result = new AtomicBoolean ( false );
        teams.forEach ( ( noseTeam ) -> {
            if(noseTeam.getPlayers ().contains ( player.getUniqueId () )) {
                result.set ( true );
            }
        } );
        return result.get ();
    }
    @Override
    public NoseTeam getTeam(Player player) {
        AtomicReference<NoseTeam> result = new AtomicReference<>();
        teams.forEach ( ( noseTeam ) -> {
            if(noseTeam.getPlayers ().contains ( player.getUniqueId () )) {
                result.set ( noseTeam );
            }
        } );
        return result.get ();
    }

    //async runtime
    @Override
    public void run () {
        Varos var = this;
        Bukkit.getScheduler ().runTask ( Main.getInstance ( ) , new Runnable ( ) {
            @Override
            public void run () {
                timer_tasks ();
                wb_tasks ( );
            }
        } );
        //reset Thread
        try {
            Thread.sleep ( 1000/20 );
            reset.pull ( new Thread ( this) );
        }
        catch (InterruptedException e) {
            e.printStackTrace ( );
        }
    }
}
