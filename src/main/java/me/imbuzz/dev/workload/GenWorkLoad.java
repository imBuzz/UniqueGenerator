package me.imbuzz.dev.workload;

import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.objects.Generator;
import me.imbuzz.dev.workload.abstractation.Workload;

@RequiredArgsConstructor
public class GenWorkLoad implements Workload {

    private final Generator generator;

    @Override
    public void compute() {
        generator.refreshGenerator();
    }

}
