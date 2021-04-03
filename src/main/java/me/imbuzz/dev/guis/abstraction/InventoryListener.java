package me.imbuzz.dev.guis.abstraction;

import me.imbuzz.dev.UniqueGenerators;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class InventoryListener implements Listener {

    public InventoryListener(UniqueGenerators uniqueGenerators){
        Bukkit.getPluginManager().registerEvents(this, uniqueGenerators);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if (event.getWhoClicked().getType() != EntityType.PLAYER) return;

        Player player = (Player) event.getWhoClicked();
        UUID playerUUID = player.getUniqueId();

        UUID inventoryUUID = Menu.openInventories.get(playerUUID);
        if (inventoryUUID != null){
            event.setCancelled(true);
            Menu gui = Menu.inventoriesByUUID.get(inventoryUUID);
            MenuAction action = gui.getActions().get(event.getSlot());

            if (action != null){
                action.click(player);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Menu.openInventories.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Menu.openInventories.remove(event.getPlayer().getUniqueId());
    }


}
