package me.imbuzz.dev.commands.list;

import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.UniqueGenerators;
import me.imbuzz.dev.commands.SubCommand;
import me.imbuzz.dev.guis.GeneratorOnline;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class AdminList extends SubCommand {
    private final UniqueGenerators uniqueGenerators;

    @Override
    public void onCommand(Player player, String[] args) {
        GeneratorOnline.getInventory(uniqueGenerators).open(player);
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
