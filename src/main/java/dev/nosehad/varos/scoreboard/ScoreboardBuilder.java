package dev.nosehad.varos.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;


public abstract class ScoreboardBuilder {
    protected final Scoreboard scoreboard;
    protected final Objective objective;
    protected final Player player;

    public ScoreboardBuilder(Player player, String displayName) {
        this.player = player;
        if (player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        this.scoreboard = player.getScoreboard();
        if (this.scoreboard.getObjective("display") != null) {
            Objects.requireNonNull ( this.scoreboard.getObjective ( "display" ) ).unregister();
        }

        this.objective = this.scoreboard.registerNewObjective ( "display", "dummy", Component.text ( displayName ));
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.createScoreboard();
    }

    public abstract void createScoreboard();

    public void setScore(String content, int score) {
        this.objective.getScore(content).setScore(score);
    }

}