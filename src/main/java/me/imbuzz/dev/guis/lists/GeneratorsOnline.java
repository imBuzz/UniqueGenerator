package me.imbuzz.dev.guis.lists;

import me.imbuzz.dev.guis.abstraction.ScrollableInventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GeneratorsOnline extends ScrollableInventory {

    public GeneratorsOnline(String invName, List<ItemStack> items) {
        super(invName, 54, items, false);
    }

}
