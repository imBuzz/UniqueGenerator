package me.imbuzz.dev.commands;

import org.bukkit.entity.Player;

public abstract class SubCommand {

    public abstract void onCommand(Player player, String[] args);

    public abstract String name();

    public abstract String permission();

    public abstract String info();

    public abstract String[] aliases();
}
