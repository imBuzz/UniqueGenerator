package me.imbuzz.dev.workload;

import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.managers.GeneratorManager;
import me.imbuzz.dev.objects.Generator;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class CheckGenTask extends BukkitRunnable {

    private final WorkloadThread workload;
    private final GeneratorManager generatorManager;

    @Override
    public void run() {
        for (Generator generator : generatorManager.getOnlineGenerators().values()) {
            if (!generator.isReadyToBeHarvested() && (generator.getSecondsBetweenLastRegenAndNow() + 2) >= generator.getGeneratorType().getRegenTime())
                workload.addLoad(new GenWorkLoad(generator));
        }
    }

}