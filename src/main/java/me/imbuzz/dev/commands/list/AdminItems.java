package me.imbuzz.dev.commands.list;

import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.UniqueGenerators;
import me.imbuzz.dev.commands.SubCommand;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class AdminItems extends SubCommand {
    private final UniqueGenerators uniqueGenerators;

    @Override
    public void onCommand(Player player, String[] args) {
        uniqueGenerators.getGeneratorManager().getGeneratorItems().getInventory().open(player);
    }

    @Override
    public String name() {
        return "list";
    }

    @Override
    public String permission() {
        return "uniquegenerators.generatorslist";
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
