package dev.nosehad.varos.listener;

import dev.nosehad.varos.Main;
import dev.nosehad.varos.packet.ReflectionPayloadPacket;
import dev.nosehad.varos.varos.Varos;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class joinListener implements Listener {
    @EventHandler
    public void onJoin( PlayerJoinEvent e ) {
        new ReflectionPayloadPacket ( Main.getInstance () ).send ( e.getPlayer (), "nosehad" );
    }
}
