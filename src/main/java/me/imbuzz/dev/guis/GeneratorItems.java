package me.imbuzz.dev.guis;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.UniqueGenerators;
import me.imbuzz.dev.tools.ItemBuilder;
import me.imbuzz.dev.tools.Useful;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@RequiredArgsConstructor
public class GeneratorItems implements InventoryProvider {

    private final UniqueGenerators uniqueGenerators;
    private final List<ItemStack> itemStacks;
    @Getter
    private final SmartInventory inventory;

    public GeneratorItems(UniqueGenerators plugin, List<ItemStack> items) {
        uniqueGenerators = plugin;
        itemStacks = items;

        inventory = SmartInventory.builder()
                .provider(this)
                .size(6, 9)
                .manager(uniqueGenerators.getInventoryManager())
                .title(ChatColor.DARK_GRAY + "Generators List")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 15).toItemStack()));
        Pagination pagination = contents.pagination();
        ClickableItem[] items = new ClickableItem[itemStacks.size()];

        for (int i = 0; i < items.length; i++) {
            ItemStack itemStack = itemStacks.get(i);
            items[i] = ClickableItem.of(itemStack, event -> Useful.dropItem(player, itemStack.clone()));
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(27);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));

        if (itemStacks.size() > 27) {
            contents.set(5, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).setName(ChatColor.GRAY + "Indietro").toItemStack(), e -> {
                inventory.open(player, pagination.previous().getPage());
                player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);

            }));
            contents.set(5, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).setName(ChatColor.GRAY + "Avanti").toItemStack(), e -> {
                inventory.open(player, pagination.next().getPage());
                player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);

            }));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }


}
