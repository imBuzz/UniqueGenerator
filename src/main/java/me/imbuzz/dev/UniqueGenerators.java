package me.imbuzz.dev;

import lombok.Getter;
import me.imbuzz.dev.commands.CommandManager;
import me.imbuzz.dev.files.FilesManager;
import me.imbuzz.dev.guis.abstraction.InventoryListener;
import me.imbuzz.dev.listeners.BlockEvents;
import me.imbuzz.dev.managers.GeneratorManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class UniqueGenerators extends JavaPlugin {
    private FilesManager filesManager;
    private GeneratorManager generatorManager;
    private CommandManager commandManager;


    @Override
    public void onEnable() {
        commandManager = new CommandManager(this);
        commandManager.setup();

        filesManager = new FilesManager(this);
        generatorManager = new GeneratorManager(this);

        new InventoryListener(this);
        new BlockEvents(this, generatorManager);


        Bukkit.getLogger().info("UniqueGenerators has been enabled, created by ImBuzz");
    }

    @Override
    public void onDisable() {
        generatorManager.saveOnlineGenerators(filesManager);

        Bukkit.getLogger().info("UniqueGenerators disabled, created by ImBuzz");
    }
}
