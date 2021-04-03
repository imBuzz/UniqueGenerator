package me.imbuzz.dev.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.UniqueGenerators;
import me.imbuzz.dev.commands.list.AdminItems;
import me.imbuzz.dev.commands.list.AdminList;
import me.imbuzz.dev.tools.Useful;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

@Getter @RequiredArgsConstructor
public class CommandManager implements CommandExecutor {
    private final UniqueGenerators uniqueGenerators;
    private ArrayList<SubCommand> commands = new ArrayList<>();

    public void setup() {
        commands.add(new AdminItems(uniqueGenerators));
        commands.add(new AdminList(uniqueGenerators));

        uniqueGenerators.getCommand("generators").setExecutor(this);
    }


    private SubCommand get(String name) {
        Iterator<SubCommand> subcommands = this.commands.iterator();

        while (subcommands.hasNext()) {
            SubCommand sc = subcommands.next();

            if (sc.name().equalsIgnoreCase(name)) {
                return sc;
            }

            String[] aliases;
            int length = (aliases = sc.aliases()).length;

            for (int var5 = 0; var5 < length; ++var5) {
                String alias = aliases[var5];
                if (name.equalsIgnoreCase(alias)) {
                    return sc;
                }

            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("generators")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args.length == 0) {
                    player.sendMessage(Useful.colorize("&aUniqueGenerator by ImBuzz"));
                    return true;
                }


                Optional<SubCommand> target = Optional.ofNullable(this.get(args[0]));
                if (!target.isPresent()) {
                    player.sendMessage(ChatColor.RED + "(!) Subcommand not valid!");
                    return true;
                }

                try {
                    if (target.get().permission() != null){
                        if (player.hasPermission(target.get().permission()))  target.get().onCommand(player, args);
                        else player.sendMessage(ChatColor.RED + "(!) You don't have sufficient permissions to do that!");
                    }
                    else target.get().onCommand(player, args);
                }
                catch (Exception e) {
                    player.sendMessage(ChatColor.RED + "(!) Subcommand not valid!");
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
}
