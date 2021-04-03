package me.imbuzz.dev.managers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.SneakyThrows;
import me.imbuzz.dev.UniqueGenerators;
import me.imbuzz.dev.files.FilesManager;
import me.imbuzz.dev.guis.lists.GeneratorItems;
import me.imbuzz.dev.objects.Generator;
import me.imbuzz.dev.objects.GeneratorType;
import me.imbuzz.dev.tools.ItemBuilder;
import me.imbuzz.dev.tools.Useful;
import me.imbuzz.dev.workload.CheckGenTask;
import me.imbuzz.dev.workload.WorkloadThread;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class GeneratorManager {
    private final UniqueGenerators uniqueGenerators;
    private GeneratorItems generatorItems;

    private final Map<String, GeneratorType> loadedGenerators = Maps.newHashMap();
    private final HashMap<Location, Generator> onlineGenerators = Maps.newHashMap();

    private final WorkloadThread genRefreshWorkload;
    private final BukkitRunnable genCheckTask;

    public GeneratorManager(UniqueGenerators plugin) {
        uniqueGenerators = plugin;

        genRefreshWorkload = new WorkloadThread();
        Bukkit.getScheduler().runTaskTimer(uniqueGenerators, genRefreshWorkload, 100L, 20L);

        genCheckTask = new CheckGenTask(genRefreshWorkload, this);
        genCheckTask.runTaskTimerAsynchronously(uniqueGenerators, 100L, 20L);

        loadFromConfig(uniqueGenerators.getFilesManager(), this);
    }

    public boolean isAGenerator(Location blockLocation) {
        return onlineGenerators.get(blockLocation) != null;
    }

    public Generator getGenerator(Location location) {
        return onlineGenerators.get(location);
    }

    public void addGenerator(Generator generator){
        onlineGenerators.put(generator.getLocation(), generator);
    }

    public void removeGenerator(Location location){
        location.getWorld().playEffect(location, Effect.STEP_SOUND, location.getBlock().getType());
        location.getBlock().setType(Material.AIR);
        location.getWorld().dropItemNaturally(location.clone().add(0, 1, 0), onlineGenerators.get(location).getGeneratorType().createItem());

        onlineGenerators.remove(location);
    }

    public boolean isItemInHandAGenerator(ItemStack itemStack) {
        ItemBuilder itemBuilder = new ItemBuilder(itemStack);
        boolean isGen = itemBuilder.getNBTBoolean("isGen") && loadedGenerators.keySet().contains(itemBuilder.getNBTString("genTag"));
        return isGen;
    }

    public GeneratorType getGeneratorFromItem(ItemStack itemStack){
        ItemBuilder itemBuilder = new ItemBuilder(itemStack);
        return loadedGenerators.get((itemBuilder.getNBTString("genTag")));
    }

    public void loadFromConfig(FilesManager filesManager, GeneratorManager generatorManager){
        Bukkit.getScheduler().runTaskAsynchronously(uniqueGenerators, () -> {
            ConfigurationSection sections = filesManager.getConfigData().getConfigurationSection("generators");
            sections.getKeys(false).forEach(generatorTag -> {
                try{
                    ConfigurationSection section = sections.getConfigurationSection(generatorTag);
                    int regenTime = section.getInt("regenTime");
                    Material completedMaterial = Material.getMaterial(section.getString("completedMaterial").toUpperCase());
                    Material waitingMaterial = Material.getMaterial(section.getString("waitingMaterial").toUpperCase());
                    Material giveMaterial = Material.getMaterial(section.getString("giveMaterial").toUpperCase());
                    String name = section.getString("item.name");
                    ArrayList<String> lore = (ArrayList<String>) section.getStringList("item.lore");

                    loadedGenerators.put(generatorTag, new GeneratorType(generatorTag, regenTime, completedMaterial,
                            waitingMaterial, giveMaterial, name, lore));

                    Bukkit.getLogger().info("[UniqueGenerators]: Registered generator in section: " + generatorTag);

                    ArrayList<ItemStack> currentItems = Lists.newArrayList();
                    getLoadedGenerators().values().forEach(value -> currentItems.add(value.createItem()));
                    generatorItems = new GeneratorItems("Generators List", currentItems);
                }
                catch (Exception e1){
                    Bukkit.getLogger().severe("[UniqueGenerators Error]: " + e1.getLocalizedMessage() + ", check your config.yml!");
                }
            });
        });
        Bukkit.getScheduler().runTaskLater(uniqueGenerators, () -> loadSavedGenerators(uniqueGenerators.getFilesManager(), generatorManager), 20);
    }

    @SneakyThrows
    public void saveOnlineGenerators(FilesManager filesManager){
        filesManager.getGeneratorsData().set("generatorslist", null);
        onlineGenerators.values().forEach(generator -> {
            filesManager.getGeneratorsData().set("generatorslist." + generator.getGeneratorUUID() + ".type", generator.getGeneratorType().getTag());
            filesManager.getGeneratorsData().set("generatorslist." + generator.getGeneratorUUID() + ".lastBrokenTime", generator.getLastBrokenTime());
            filesManager.getGeneratorsData().set("generatorslist." + generator.getGeneratorUUID() + ".location", Useful.serializeLocation(generator.getLocation()));
        });
        filesManager.getGeneratorsData().save(filesManager.getGeneratorsFile());
    }

    public void loadSavedGenerators(FilesManager filesManager, GeneratorManager generatorManager){
        Set<Generator> cache = Sets.newHashSet();
        Bukkit.getScheduler().runTaskAsynchronously(uniqueGenerators, () -> {
            int numberOfGens = 0;
            ConfigurationSection sections = filesManager.getGeneratorsData().getConfigurationSection("generatorslist");
            if (sections != null){
                for (String genUUID : sections.getKeys(false)) {
                    ConfigurationSection genSection = sections.getConfigurationSection(genUUID);

                    GeneratorType generatorType = loadedGenerators.get(genSection.getString("type"));
                    if (generatorType == null){
                        Bukkit.getLogger().warning("UniqueGenerators: Not found a generator type for: " + genSection.getString("type"));
                        continue;
                    }
                    long lastBrokenTime = genSection.getLong("lastBrokenTime");
                    Location location = Useful.deserializeLocation(genSection.getString("location"));

                    cache.add(new Generator(generatorType, lastBrokenTime, false, location));
                    numberOfGens++;
                }
                Bukkit.getScheduler().runTask(uniqueGenerators, () -> cache.forEach(generator -> {
                    generator.setup();
                    generatorManager.addGenerator(generator);
                }));

                Bukkit.getLogger().info("[UniqueGenerators]: Loaded " + numberOfGens + " generators");
            }
        });
    }






}
