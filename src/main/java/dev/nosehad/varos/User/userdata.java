package dev.nosehad.varos.User;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class userdata implements Iuserdata {
    public static userdata getData(UUID target) {
        return users.get ( target );
    }
    public static HashMap<UUID, userdata> users = new HashMap<> ();
    private final UUID user;
    private boolean protection;
    private boolean cooldown;
    private String additionalString;
    private int additionalInt;

    public userdata ( UUID uuid ) {
        this.user = uuid;
        protection = true;
        additionalString ="none";
        users.put ( uuid, this );
    }

    @Override
    public void setAdditionalString ( String additionalString ) {
        this.additionalString = additionalString;
    }

    @Override
    public String getAdditionalString () {
        return additionalString;
    }


    public void resetAdditional() {
        additionalString = null;
        additionalInt = 0;
    }

    @Override
    public void setCooldown (boolean value) {
        cooldown = value;
    }

    @Override
    public boolean hasCooldown() {
        return cooldown;
    }


    @Override
    public void setProtection ( boolean Protection) {
        protection = Protection;
    }

    @Override
    public boolean hasProtection() {
        return protection;
    }

    @Override
    public int getAdditionalInt () {
        return additionalInt;
    }

    @Override
    public void setAdditionalInt ( int additionalInt ) {
        this.additionalInt = additionalInt;
    }

    public Player getUser () {
        return Bukkit.getPlayer ( user );
    }
}
