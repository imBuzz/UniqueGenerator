package me.imbuzz.dev.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.imbuzz.dev.tools.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class GeneratorType {

    private String tag;
    private int regenTime;
    private Material completedMaterial;
    private Material waitingMaterial;
    private Material giveMaterial;
    private String itemName;
    private ArrayList<String> itemLore;

    public ItemStack createItem(){
        ItemBuilder itemBuilder = new ItemBuilder(completedMaterial);
        itemBuilder.setNBTString("genTag", tag);
        itemBuilder.setNBTBoolean("isGen", true);

        itemBuilder.setName(itemName);
        itemBuilder.addAllLore(itemLore);


        return itemBuilder.toItemStack();
    }

}
