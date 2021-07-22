package me.imbuzz.dev.objects;

import lombok.Getter;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
public class Generator {

    private UUID generatorUUID;
    private GeneratorType generatorType;
    private long lastBrokenTime;
    private boolean readyToBeHarvested;
    private Location location;


    public Generator(GeneratorType type, long l, boolean r, Location loc){
        generatorType = type;
        generatorUUID = UUID.randomUUID();
        lastBrokenTime = l;
        readyToBeHarvested = r;
        location = loc;
    }

    public void setup(){
        if (!readyToBeHarvested) location.getBlock().setType(generatorType.getWaitingMaterial());
        else location.getBlock().setType(generatorType.getCompletedMaterial());
    }

    public void refreshGenerator(){
        location.getBlock().setType(generatorType.getCompletedMaterial());
        location.getWorld().playEffect(location, Effect.STEP_SOUND, location.getBlock().getType());
        readyToBeHarvested = true;
    }

    public void breakGenerator(){
        location.getWorld().dropItemNaturally(location.clone().add(0, 1, 0), new ItemStack(generatorType.getGiveMaterial()));
        location.getBlock().setType(generatorType.getWaitingMaterial());
        readyToBeHarvested = false;
        lastBrokenTime = System.currentTimeMillis();
    }

}
