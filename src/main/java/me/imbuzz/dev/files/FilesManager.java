package me.imbuzz.dev.files;

import lombok.Getter;
import me.imbuzz.dev.UniqueGenerators;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Getter
public class FilesManager {

    private File configFile;
    private FileConfiguration configData;

    private File generatorsFile;
    private FileConfiguration generatorsData;


    public FilesManager(UniqueGenerators plugin){
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()){
            plugin.saveResource("config.yml", false);
        }
        configData = YamlConfiguration.loadConfiguration(configFile);

        generatorsFile = new File(plugin.getDataFolder(), "generators.yml");
        if (!generatorsFile.exists()){
            plugin.saveResource("generators.yml", false);
        }
        generatorsData = YamlConfiguration.loadConfiguration(generatorsFile);
    }
}
