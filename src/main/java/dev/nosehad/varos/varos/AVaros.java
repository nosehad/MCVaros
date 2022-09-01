package dev.nosehad.varos.varos;

import dev.nosehad.varos.Main;
import dev.nosehad.varos.User.userdata;
import dev.nosehad.varos.scoreboard.VarosScoreboard;
import dev.nosehad.varos.utils.Position;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AVaros implements Serializable {

    protected final TimerThread timerThread;
    protected long shrinkingRate;
    protected boolean started;
    protected Position spawn;
    protected int worldBorder;
    protected int min_worldborder_radius;
    protected int max_players_per_team;
    protected final int spawnradius;
    protected boolean shrinking;
    private boolean currentlyShrinking;
    protected final int lives;
    protected boolean allow_bed_respawn;
    protected boolean old_combat;
    protected boolean protection;
    public String admin_passwd;
    protected int protection_time;
    protected int shrinking_after_minutes;
    public HashMap<UUID, Integer> playerLifes = new HashMap<> ();
    protected ArrayList<NoseTeam> teams = new ArrayList<> ();
    protected ArrayList<UUID> timerToggled = new ArrayList<> ();
    protected LinkedList<UUID> registeredPlayers = new LinkedList<> ();

    public AVaros ( int lives, int spawnradius, boolean allow_bed_respawn, int shrinking_after_minutes, boolean old_combat, String admin_passwd, int protection_time, int worldborder_radius, int min_worldborder_radius, int max_players_per_team ) {
        //construct variables
        this.lives = lives;
        this.protection = true;
        this.old_combat = old_combat;
        this.spawnradius = spawnradius;
        this.allow_bed_respawn = allow_bed_respawn;
        this.admin_passwd = admin_passwd;
        this.shrinking_after_minutes = shrinking_after_minutes;
        this.protection_time = protection_time;
        this.worldBorder = worldborder_radius;
        this.shrinkingRate = 1;
        this.max_players_per_team = max_players_per_team;
        this.min_worldborder_radius = min_worldborder_radius;
        this.shrinking = false;
        this.currentlyShrinking = false;
        this.timerThread = new TimerThread ();

        timerThread.setRunning ( false );
        started = false;
        Bukkit.getWorlds ( ).forEach ( world -> world.getWorldBorder ( ).setSize ( worldBorder ) );
    }

    protected void wb_tasks() {
        if(shrinking) {
            if ( !currentlyShrinking ) {
                if(worldBorder >= min_worldborder_radius) {
                    currentlyShrinking = true;
                    Thread t = new Thread ( ) {
                        @Override
                        public void run () {
                            Bukkit.getScheduler ( ).runTask ( Main.getInstance ( ) , new Runnable ( ) {
                                @Override
                                public void run () {
                                    worldBorder = worldBorder - 1;
                                    Bukkit.getWorlds ( ).forEach ( world -> world.getWorldBorder ( ).setSize ( worldBorder , 1 / shrinkingRate ) );
                                }
                            } );
                            try {
                                sleep ( 1000 / shrinkingRate );
                                currentlyShrinking = false;
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace ( );
                            }
                        }
                    };
                    t.start ( );
                }
            }
        }
    }
    protected void timer_tasks () {

        //send Timer to players
        ArrayList<Player> send = new ArrayList<> ();
        timerToggled.forEach ( uuid -> {
            if(Bukkit.getPlayer ( uuid ) != null) {
                send.add ( Bukkit.getPlayer ( uuid ) );
            }
        } );
        timerThread.updatePlayers ( send );

        //initialize tasks
        shrinking = !(timerThread.timer_minutes <= shrinking_after_minutes);
        if ( !(timerThread.timer_minutes <= protection_time) ) {
            protection = false;
            getOnlineDatas ().forEach ( userdata -> userdata.setProtection ( false ) );
        } if(timerThread.second_passed ()) {
            scorboardTasks ();
        }
    }
    private void scorboardTasks() {
        getInfoToggled ().forEach ( VarosScoreboard::new );
    }

    // Timer methods
    public void resume () {
        protection = false;
        timerThread.setRunning ( true );
    }
    public void pause () {
        protection = true;
        timerThread.setRunning ( false );
    }
    public void reset () {
        timerThread.reset ();
    }

    public boolean isOld_combat() {
        return this.old_combat;
    }

    // worldborder
    public void setShrinkingRate (int val) {
        this.shrinkingRate = val;
    }
    public void setWorldBorder (int targetXZ) {
        worldBorder = targetXZ;
        Bukkit.getWorlds ( ).forEach ( world -> world.getWorldBorder ( ).setSize ( worldBorder ) );
    }

    //player specific
    protected LinkedList<userdata> getOnlineDatas() {
        LinkedList<userdata> temp = new LinkedList<>();
        Bukkit.getOnlinePlayers ().forEach ( player -> temp.add ( userdata.getData ( player.getUniqueId () ) ));
        return temp;
    }
    protected LinkedList<Player> getInfoToggled() {
        LinkedList<Player> temp = new LinkedList<>();
        getOnlineDatas ().forEach ( userdata -> {
            if( Objects.equals ( userdata.getAdditionalString ( ) , "info" ) ) {
                temp.add ( userdata.getUser () );
            }
        } );
        return temp;
    }
    protected void register(Player player) {
        UUID uuid = player.getUniqueId ();
        registeredPlayers.add ( uuid );
        playerLifes.put ( uuid, lives );
    }
    protected int getLifes( UUID uuid ) {
        return playerLifes.get ( uuid );
    }
    protected void decreaseLife(UUID uuid) {
        int t = playerLifes.get ( uuid );
        playerLifes.remove ( uuid );
        playerLifes.put ( uuid, t-1 );
    }

    //team stuff
    protected abstract void createTeam(Player player);
    protected abstract boolean isInTeam( Player player );
    protected abstract NoseTeam getTeam(Player player);

    //scoreboard methods
    protected abstract int getWorldBorder ();
    protected abstract String getLifeString( UUID uuid );
    protected abstract String getProtectionTimeString();

    //command methods
    protected abstract void toggleInfo(Player player);
    public abstract boolean infoToggled(Player player);

    protected abstract void toggleTimer(Player player);
    public abstract boolean timerToggled(Player player);

    // Worldspawn methods
    public void setSpawn(Location loc){
        Bukkit.getWorlds ().forEach ( world -> {
            world.getWorldBorder ().setCenter ( loc.getX (), loc.getZ () );
        } );
        spawn = Position.valueOf ( loc );
    }
    public Position getSpawn() {
        if(spawn != null) {
            return spawn;
        } else {
            AtomicReference<World> w = new AtomicReference<>();
            Bukkit.getServer ().getWorlds ().forEach ( world -> {
                if(world.getEnvironment () != World.Environment.NETHER) {
                    if(world.getEnvironment () != World.Environment.THE_END) {
                        w.set ( world );
                    }
                }
            } );
            return Position.valueOf ( w.get ().getSpawnLocation () );
        }
    }

    //server specific methods
    public void save () {
        getInfoToggled ().forEach ( this::toggleInfo );

        //set targets of timer to null, to prevent errors
        timerThread.targets = null;

        try {
            FileOutputStream fos = new FileOutputStream( new File ( "./plugins/Varos/varos.dat"));
            ObjectOutputStream oos = new ObjectOutputStream( fos);
            // Write objects to file
            oos.writeObject(this);
            oos.close();
            fos.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void delete () {
        Varos.current = null;
        if( Files.isReadable ( Paths.get ( "./plugins/Varos/varos.dat"  ))) {
            File file = new File ( "./plugins/Varos/varos.dat" );
            file.delete ( );
        }
        Bukkit.getServer ().reload ();
    }
    public void start() {
        started = true;
        protection = true;
        timerThread.running = true;
    }
    
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasStarted () {
        return started;
    }


}
