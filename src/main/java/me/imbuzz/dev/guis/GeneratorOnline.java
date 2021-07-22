package me.imbuzz.dev.guis;

import com.google.common.collect.Lists;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.UniqueGenerators;
import me.imbuzz.dev.tools.ItemBuilder;
import me.imbuzz.dev.tools.Useful;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

@RequiredArgsConstructor
public class GeneratorOnline implements InventoryProvider {

    static SmartInventory INVENTORY;
    private final UniqueGenerators uniqueGenerators;

    public static SmartInventory getInventory(UniqueGenerators uniqueGenerators) {
        INVENTORY = SmartInventory.builder()
                .provider(new GeneratorOnline(uniqueGenerators))
                .size(6, 9)
                .manager(uniqueGenerators.getInventoryManager())
                .title(ChatColor.DARK_GRAY + "Generators Online")
                .build();
        return INVENTORY;
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 15).toItemStack()));
        Pagination pagination = contents.pagination();
        ClickableItem[] items = new ClickableItem[uniqueGenerators.getGeneratorManager().getOnlineGenerators().size()];

        ArrayList<ItemStack> currentItems = Lists.newArrayList();
        uniqueGenerators.getGeneratorManager().getOnlineGenerators().values().forEach(generator -> {
            ItemBuilder itemBuilder = new ItemBuilder(generator.getGeneratorType().createItem());
            itemBuilder.addLoreLines(
                    "",
                    "&7Location: X: " + generator.getLocation().getX() + " Y: " + generator.getLocation().getY() + " Z: " + generator.getLocation().getY(),
                    "&7World: " + generator.getLocation().getWorld().getName()
            );
            currentItems.add(itemBuilder.toItemStack());
        });


        for (int i = 0; i < items.length; i++) {
            ItemStack itemStack = currentItems.get(i);
            items[i] = ClickableItem.of(itemStack, event -> Useful.dropItem(player, itemStack.clone()));
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(27);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));

        if (currentItems.size() > 27) {
            contents.set(5, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).setName(ChatColor.GRAY + "Indietro").toItemStack(), e -> {
                INVENTORY.open(player, pagination.previous().getPage());
                player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);

            }));
            contents.set(5, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).setName(ChatColor.GRAY + "Avanti").toItemStack(), e -> {
                INVENTORY.open(player, pagination.next().getPage());
                player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);

            }));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }


}
