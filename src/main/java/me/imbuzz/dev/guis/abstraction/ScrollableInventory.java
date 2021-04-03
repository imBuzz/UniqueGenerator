package me.imbuzz.dev.guis.abstraction;

import com.google.common.collect.Lists;
import me.imbuzz.dev.tools.ItemBuilder;
import me.imbuzz.dev.tools.Useful;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class ScrollableInventory{
    private int lastItemAdded = 0;
    private boolean giveItems;
    private List<ImplementMenu> pages = Lists.newArrayList();

    public ScrollableInventory(String invName, int invSize, List<ItemStack> items, boolean gives) {
        ImplementMenu menu = new ImplementMenu(invSize, invName);
        giveItems = gives;
        if (!items.isEmpty()){
            while (lastItemAdded < items.size()){
                for (int i = 0; i < menu.getInventory().getSize(); i++){
                    if (i < items.size()){
                        if (i < 45 && lastItemAdded < items.size()){
                            ItemStack itemStack = items.get(lastItemAdded);
                            menu.setItem(i, itemStack, player -> {
                                if (giveItems){
                                    player.getInventory().addItem(itemStack);
                                }
                            });
                            lastItemAdded++;
                        }
                        else {
                            ItemBuilder itemBuilder = new ItemBuilder(Material.ARROW);
                            itemBuilder.setName(ChatColor.GRAY + "Back");
                            menu.setItem(48, itemBuilder.toItemStack(), player -> getPreviousPage(getCurrentPage(player)).open(player));
                            itemBuilder.setName(ChatColor.GRAY + "Next");
                            menu.setItem(50, itemBuilder.toItemStack(), player -> getNextPage(getCurrentPage(player)).open(player));
                            itemBuilder.setMaterial(Material.BARRIER);
                            itemBuilder.setName(ChatColor.GRAY + "Close");
                            menu.setItem(49, itemBuilder.toItemStack(), player -> player.closeInventory());

                            pages.add(menu);

                            menu = new ImplementMenu(invSize, invName);
                            break;
                        }
                    }
                    else pages.add(menu);
                }
            }
        }
        else pages.add(new ImplementMenu(invSize, invName));
    }

    public Menu getPreviousPage(Menu currentPage){
        int index = pages.indexOf(currentPage);
        if (index != 0){
            return pages.get(index - 1);
        }
        return currentPage;
    }

    public Menu getCurrentPage(Player player){
        return Menu.inventoriesByUUID.get(Menu.openInventories.get(player.getUniqueId()));
    }

    public Menu getNextPage(Menu currentPage){
        int index = pages.indexOf(currentPage) + 1;
        if (index < pages.size()){
            return pages.get(index);
        }
        return currentPage;
    }

    public void openScrollable(Player player){
        if (pages.get(0) == null) {
            player.sendMessage(Useful.colorize("&cNo page for no items"));
            return;
        }

        pages.get(0).open(player);
    }

    private class ImplementMenu extends Menu {
        public ImplementMenu(int invSize, String invName) {
            super(invSize, invName);
        }
    }

}
