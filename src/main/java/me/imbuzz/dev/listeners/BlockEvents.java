package me.imbuzz.dev.listeners;

import me.imbuzz.dev.UniqueGenerators;
import me.imbuzz.dev.managers.GeneratorManager;
import me.imbuzz.dev.objects.Generator;
import me.imbuzz.dev.objects.GeneratorType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;


public class BlockEvents implements Listener {

    private final GeneratorManager generatorManager;

    public BlockEvents(UniqueGenerators uniqueGenerators, GeneratorManager gm){
        generatorManager = gm;

        Bukkit.getPluginManager().registerEvents(this, uniqueGenerators);
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event){
        ItemStack ipoteticItem = event.getPlayer().getItemInHand();

        if (ipoteticItem != null){
            if (generatorManager.isItemInHandAGenerator(ipoteticItem)){
                GeneratorType generatorType = generatorManager.getGeneratorFromItem(ipoteticItem);
                event.getBlockPlaced().getWorld().playEffect(event.getBlockPlaced().getLocation(), Effect.STEP_SOUND, generatorType.getWaitingMaterial());
                Generator generator = new Generator(generatorType, System.currentTimeMillis(), false, event.getBlock().getLocation());
                generator.setup();
                generatorManager.addGenerator(generator);
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event){
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();

        if (generatorManager.isAGenerator(blockLocation)) {
            event.setCancelled(true);
            Generator generator = generatorManager.getGenerator(blockLocation);
            if (event.getPlayer().isSneaking()) {
                generatorManager.removeGenerator(blockLocation);
            } else if (generator.isReadyToBeHarvested()) {
                generator.breakGenerator();

            } else {
                event.getPlayer().sendMessage(ChatColor.YELLOW + "In order break the generator you have to be shifted");
            }
        }
    }



}
