package me.imbuzz.dev.workload;

import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.managers.GeneratorManager;
import me.imbuzz.dev.objects.Generator;
import me.imbuzz.dev.workload.abstractation.WorkloadThread;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class CheckGenTask extends BukkitRunnable {

    private final WorkloadThread workload;
    private final GeneratorManager generatorManager;

    @Override
    public void run() {
        for (Generator generator : generatorManager.getOnlineGenerators().values()) {
            if (!generator.isReadyToBeHarvested() && System.currentTimeMillis() - generator.getLastBrokenTime() >= generator.getGeneratorType().getRegenTime() * 1000L)
                workload.addLoad(new GenWorkLoad(generator));
        }
    }

}