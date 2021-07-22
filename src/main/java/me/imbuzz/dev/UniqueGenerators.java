package me.imbuzz.dev;

import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import me.imbuzz.dev.commands.CommandManager;
import me.imbuzz.dev.files.FilesManager;
import me.imbuzz.dev.listeners.BlockEvents;
import me.imbuzz.dev.managers.GeneratorManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class UniqueGenerators extends JavaPlugin {

    private FilesManager filesManager;
    private GeneratorManager generatorManager;
    private CommandManager commandManager;
    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        filesManager = new FilesManager(this);
        generatorManager = new GeneratorManager(this);

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        commandManager = new CommandManager(this);
        commandManager.setup();

        new BlockEvents(this, generatorManager);


        Bukkit.getLogger().info("UniqueGenerators has been enabled, created by ImBuzz");
    }

    @Override
    public void onDisable() {
        generatorManager.saveOnlineGenerators(filesManager);

        Bukkit.getLogger().info("UniqueGenerators disabled, created by ImBuzz");
    }
}
