package me.imbuzz.dev.commands.list;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.UniqueGenerators;
import me.imbuzz.dev.commands.SubCommand;
import me.imbuzz.dev.guis.lists.GeneratorsOnline;
import me.imbuzz.dev.tools.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

@RequiredArgsConstructor
public class AdminList extends SubCommand {
    private final UniqueGenerators uniqueGenerators;

    @Override
    public void onCommand(Player player, String[] args) {
        ArrayList<ItemStack> currentItems = Lists.newArrayList();
        uniqueGenerators.getGeneratorManager().getOnlineGenerators().values().forEach(generator ->{
            ItemBuilder itemBuilder = new ItemBuilder(generator.getGeneratorType().createItem());
            itemBuilder.addLoreLines(
                    "",
                    "&7Location: X: " + generator.getLocation().getX() + " Y: " + generator.getLocation().getY() + " Z: " + generator.getLocation().getY(),
                    "&7World: " + generator.getLocation().getWorld().getName()
            );
            currentItems.add(itemBuilder.toItemStack());
        });

        new GeneratorsOnline("Generators Placed", currentItems).openScrollable(player);
    }

    @Override
    public String name() {
        return "online";
    }

    @Override
    public String permission() {
        return "uniquegenerators.generatorsonline";
    }

    @Override
    public String info() {
        return null;
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }


}
