package dev.nosehad.varos.packet;

import dev.nosehad.varos.Main;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public class ReflectionPayloadPacket {

    private Main plugin;

    public ReflectionPayloadPacket(Main plugin) {
        this.plugin = plugin;
        Messenger messenger = Bukkit.getMessenger();
        try {
            Method method = messenger.getClass().getDeclaredMethod("addToOutgoing", Plugin.class, String.class);
            method.setAccessible(true);
            method.invoke(messenger, plugin, F3Name.BRAND_CHANNEL);
        } catch (Exception ex) {
            Bukkit.getLogger ().info ( Main.prefix + "§cfailed to register brand channel: " + ex );
        }
    }

    public void send(Player player, String brand) {
        sendRaw(player, brand);
    }

    public void sendRaw(Player player, String brand) {
        Validate.notNull(player, "Player is null!");
        Validate.notNull(brand, "Server brand is null!");
        
        checkPlayerChannels(player);

        player.sendPluginMessage( plugin, F3Name.BRAND_CHANNEL , new PacketSerializer(brand).toArray());
    }

    public Object getHandle() {
        throw new UnsupportedOperationException("Not implemented in ReflectionPayloadPacket!");
    }

    //Less efficient than direct usе of NMS
    private void checkPlayerChannels(Player player) {
        try {
            Field playerChannels = player.getClass().getDeclaredField("channels");
            playerChannels.setAccessible(true);
            Set<String> channels = (Set<String>) playerChannels.get(player);
            channels.add(F3Name.BRAND_CHANNEL);
        } catch (Exception ex) {
            Bukkit.getLogger ().info ( Main.prefix + "§cfailed to add channel to player: " + ex );
        }
    }

}