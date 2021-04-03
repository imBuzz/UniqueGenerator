package me.imbuzz.dev.guis.abstraction;

import com.google.common.collect.Maps;
import lombok.Getter;
import me.imbuzz.dev.tools.Useful;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

@Getter
public abstract class Menu {

    public static Map<UUID, Menu> inventoriesByUUID = Maps.newHashMap();
    public static Map<UUID, UUID> openInventories = Maps.newHashMap();

    private UUID uuid;
    private Inventory inventory;
    private Map<Integer, MenuAction> actions;

    public Menu(int invSize, String invName){
        uuid = UUID.randomUUID();
        inventory = Bukkit.createInventory(null, invSize, Useful.colorize(invName));
        actions = Maps.newHashMap();
        inventoriesByUUID.put(getUuid(), this);
    }

    public void setItem(int slot, ItemStack item, MenuAction action){
        inventory.setItem(slot, item);
        actions.put(slot, action);
    }

    public void setItem(int slot, ItemStack stack){
        setItem(slot, stack, null);
    }

    public void open(Player player){
        player.openInventory(inventory);
        openInventories.put(player.getUniqueId(), getUuid());
    }


}
