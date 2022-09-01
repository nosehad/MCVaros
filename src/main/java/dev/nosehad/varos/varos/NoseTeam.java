package dev.nosehad.varos.varos;

import dev.nosehad.varos.utils.console;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.Serializable;
import java.util.*;

public class NoseTeam implements Serializable {
    private final int max_size;

    public String getName () {
        return name;
    }

    private String name;

    private final String color;
    private LinkedList<UUID> joinRequests = new LinkedList<> ();
    private ArrayList<UUID> players = new ArrayList<>();
    private final UUID creator;
    public NoseTeam ( int max_size , UUID creator) {
        this.creator = creator;
        this.color = generateColor ( new Random () );
        this.name = String.valueOf ( new Random ().nextInt () );
        this.players.add ( creator );
        this.max_size = max_size;
    }

    public boolean isOwner(Player player) {
        return (player.getUniqueId () == creator);
    }

    public Player getCreator() {
        return Bukkit.getPlayer ( creator );
    }

    public String getColor () {
        return color;
    }

    public void leave(Player player) {
        if(player.getUniqueId () != creator) {
            players.remove ( player.getUniqueId ( ) );
            Bukkit.getServer ( ).getScoreboardManager ( ).getMainScoreboard ( ).getTeams ( ).forEach ( team1 -> {
                if ( team1.getEntries ( ).contains ( player.getName ( ) ) ) {
                    team1.removeEntry ( player.getName ( ) );
                }
            } );
        }
        else {
            try {
                if ( players.size ( ) > 1 ) {
                    Varos.current.teams.forEach ( noseTeam -> {
                        if ( noseTeam == this ) {
                            Varos.current.teams.remove ( this );
                            players.forEach ( uuid -> {
                                if ( Bukkit.getPlayer ( uuid ) != null ) {
                                    Player target = Bukkit.getPlayer ( uuid );
                                    assert target != null;
                                    console.send ( target , "§7Your team has been §cdeleted§7, since the team leader left." );
                                    Bukkit.getServer ( ).getScoreboardManager ( ).getMainScoreboard ( ).getTeams ( ).forEach ( team1 -> {
                                        if ( team1.getEntries ( ).contains ( target.getName ( ) ) ) {
                                            team1.removeEntry ( target.getName ( ) );
                                        }
                                    } );
                                }
                            } );
                            players = null;
                            Scoreboard scoreboard = Bukkit.getServer ( ).getScoreboardManager ( ).getMainScoreboard ( );
                            Team team = scoreboard.getTeam ( name );
                            if ( team != null ) {
                                team.unregister ( );
                            }
                            name = null;
                            joinRequests = null;
                        }
                    } );
                }
                else {
                    Varos.current.teams.forEach ( noseTeam -> {
                        if ( noseTeam == this ) {
                            Varos.current.teams.remove ( this );
                            if ( Bukkit.getPlayer ( creator ) != null ) {
                                Player target = Bukkit.getPlayer ( creator );
                                assert target != null;
                                console.send ( target , "§7Your team has been §cdeleted§7, since the team leader left."  );
                                Bukkit.getServer ( ).getScoreboardManager ( ).getMainScoreboard ( ).getTeams ( ).forEach ( team1 -> {
                                    if ( team1.getEntries ( ).contains ( target.getName ( ) ) ) {
                                        team1.removeEntry ( target.getName ( ) );
                                    }
                                } );
                            }
                            players = null;
                            Scoreboard scoreboard = Bukkit.getServer ( ).getScoreboardManager ( ).getMainScoreboard ( );
                            Team team = scoreboard.getTeam ( name );
                            if ( team != null ) {
                                team.unregister ( );
                            }
                            name = null;
                            joinRequests = null;
                        }
                    } );
                }
            } catch (Exception ignored) {}
        }
    }

    //request system - start
    public void acceptRequest(UUID uuid) {
        if ( joinRequests.contains ( uuid ) ) {
            if ( Bukkit.getPlayer ( uuid ) != null ) {
                Player player = Bukkit.getPlayer ( uuid );
                assert player != null;
                joinRequests.remove ( uuid );
                add ( uuid );
                console.send ( Objects.requireNonNull ( Bukkit.getPlayer ( creator ) ) , "§7You accepted §a" + player.getName ( ) + "§7's join request." );
                console.send ( player , "§7You've joined §a" + Objects.requireNonNull ( Bukkit.getPlayer ( creator ) ).getName () + "§7's team." );
            }
            else {
                console.send ( Objects.requireNonNull ( Bukkit.getPlayer ( creator ) ) , "§7You couldn't accept §a" + Bukkit.getOfflinePlayer ( uuid ).getName ( ) + "§7's request, because he is left the server." );
            }
        } else {
            console.send ( Objects.requireNonNull ( Bukkit.getPlayer ( creator ) ) , "§7You couldn't accept §a" + Bukkit.getOfflinePlayer ( uuid ).getName ( ) + "'s request, since he didn't sent a join request." );
        }
    }
    public void joinRequest(Player player) {
        if(!isFull ()) {
            if ( Bukkit.getPlayer ( creator ) != null ) {
                joinRequests.add ( player.getUniqueId ( ) );
                console.send ( Objects.requireNonNull ( Bukkit.getPlayer ( creator ) ) , "§a" + player.getName ( ) + " §7has requested to join §dyour§7 team, write /team accept §a" + player.getName ( ) + "§7 to accept." );
                console.send ( player, "§7You have sent a join request to §a" + Objects.requireNonNull ( Bukkit.getPlayer ( creator ) ).getName () + "§7's team." );
            }
            return;
        }
        console.send ( player, "§7Your request was §ccanceled §7because the team is already §cfull§7." );
    }
    //request system - end

    public void setEntries() {
        players.forEach ( uuid -> {
            if(Bukkit.getPlayer ( uuid ) != null) {
               Player player = Bukkit.getPlayer ( uuid );
               Scoreboard scoreboard = Bukkit.getServer ().getScoreboardManager ().getMainScoreboard ();
               Team team = scoreboard.getTeam ( name );
               if ( team == null ) {
                   team = scoreboard.registerNewTeam ( name );
                   team.setAllowFriendlyFire ( false );
                   team.setCanSeeFriendlyInvisibles ( true );
               }

                assert player != null;
                Bukkit.getServer ().getScoreboardManager ().getMainScoreboard ().getTeams ().forEach ( team1 -> {
                    if(team1.getEntries ().contains ( player.getName () )) {
                        team1.removeEntry ( player.getName () );
                    }
                } );
                team.addEntry ( player.getName () );
            }
        } );
    }

    public boolean isFull() {
        return (players.size () >= max_size);
    }

    public void add( UUID player ) {
        if(!isFull ()) {
            players.add ( player );
        }
    }

    private String generateColor( Random r ) {
        final char [] hex = { '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char [] s = new char[7];
        int     n = r.nextInt(0x1000000);

        s[0] = '#';
        for (int i=1;i<7;i++) {
            s[i] = hex[n & 0xf];
            n >>= 4;
        }
        return new String(s);
    }

    public ArrayList<UUID> getPlayers ( ) {
        return players;
    }
}