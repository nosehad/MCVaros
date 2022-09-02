package dev.nosehad.varos.varos;

import dev.nosehad.varos.Main;
import dev.nosehad.varos.utils.reset;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class TimerThread extends Thread implements Serializable {

    protected ArrayList<Player> targets;
    protected int timer_seconds;
    protected int timer_minutes;
    protected int timer_hours;
    protected int timer_days;
    protected boolean sec_passed;
    protected boolean running;
    protected boolean shown;
    public TimerThread () {
        running = true;
        shown = true;
        start ();
        targets = new ArrayList<Player> ();
    }

    public void updatePlayers ( ArrayList<Player> targets ) {
        this.targets = targets;
    }

    public void setRunning ( boolean running ) {
        this.running = running;
        if(!shown)
            return;
        if ( !running ) {
            if ( timer_days != 0 ) {
                targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Time: §a§l" + timer_days + "d " + timer_hours + "h " + timer_minutes + "m " + timer_seconds + "s §r§8§l⬝" ) ) );
            }
            else if ( timer_hours != 0 ) {
                targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Time: §a§l" + timer_hours + "h " + timer_minutes + "m " + timer_seconds + "s §r§8§l⬝" ) ) );
            }
            else if ( timer_minutes != 0 ) {
                targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Time: §a§l" + timer_minutes + "m " + timer_seconds + "s §r§8§l⬝" ) ) );
            }
            else {
                targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Time: §a§l" + timer_seconds + "s §r§8§l⬝" ) ) );
            }
        }
        else {
            targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Timer §c§lpaused §r§8§l⬝" ) ) );
        }
    }

    public void setShown ( boolean shown ) {
        this.shown = shown;

        if(!shown) {
            targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "               " ) ) );
        }
        else {
            if(running) {
                if ( timer_days != 0 ) {
                    targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Time: §a§l" + timer_days + "d " + timer_hours + "h " + timer_minutes + "m " + timer_seconds + "s §r§8§l⬝" ) ) );
                }
                else if ( timer_hours != 0 ) {
                    targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Time: §a§l" + timer_hours + "h " + timer_minutes + "m " + timer_seconds + "s §r§8§l⬝" ) ) );
                }
                else if ( timer_minutes != 0 ) {
                    targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Time: §a§l" + timer_minutes + "m " + timer_seconds + "s §r§8§l⬝" ) ) );
                }
                else {
                    targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Time: §a§l" + timer_seconds + "s §r§8§l⬝" ) ) );
                }
            }
            else {
                targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Timer §c§lpaused §r§8§l⬝" ) ) );
            }
        }

    }

    public boolean second_passed () {
        if (sec_passed) {
            sec_passed = false;
            return true;
        }
        return false;
    }

    public void reset () {
        timer_seconds = 0;
        timer_minutes = 0;
        timer_hours = 0;
        timer_days = 0;
    }

    @Override
    public void run () {

        //run Timer
        if ( running ) {
            //trigger second passed event
            sec_passed = true;

            //set Timer
            if ( timer_seconds < 59 ) {
                timer_seconds++;
            }
            else {
                timer_seconds = 0;
                if ( timer_minutes < 59 ) {
                    timer_minutes++;
                }
                else {
                    timer_minutes = 0;
                    if ( timer_hours < 23 ) {
                        timer_hours++;
                    }
                    else {
                        timer_hours = 0;
                        timer_days++;
                    }
                }
            }
        }
        if ( shown ) {
            //show Timer
            Bukkit.getScheduler ( ).runTask ( Main.getInstance ( ) , () -> {
                //if shown -> send Timer
                if ( running ) {
                    if ( timer_days != 0 ) {
                        targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Time: §a§l" + timer_days + "d " + timer_hours + "h " + timer_minutes + "m " + timer_seconds + "s §r§8§l⬝" ) ) );
                    }
                    else if ( timer_hours != 0 ) {
                        targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Time: §a§l" + timer_hours + "h " + timer_minutes + "m " + timer_seconds + "s §r§8§l⬝" ) ) );
                    }
                    else if ( timer_minutes != 0 ) {
                        targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Time: §a§l" + timer_minutes + "m " + timer_seconds + "s §r§8§l⬝" ) ) );
                    }
                    else {
                        targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Time: §a§l" + timer_seconds + "s §r§8§l⬝" ) ) );
                    }
                }
                else {
                    targets.forEach ( player -> player.spigot ( ).sendMessage ( ChatMessageType.ACTION_BAR , new TextComponent ( "§8§l⬝§r §7Timer §c§lpaused §r§8§l⬝" ) ) );
                }
            } );
        }
        try {
            sleep ( 1000 );
            reset.pull ( new Thread ( this) );
        }
        catch (InterruptedException e) {
            e.printStackTrace ( );
        }
    }
}
