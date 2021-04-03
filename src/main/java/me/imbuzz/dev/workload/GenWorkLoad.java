package me.imbuzz.dev.workload;

import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.objects.Generator;

@RequiredArgsConstructor
public class GenWorkLoad implements Workload {

    private final Generator generator;

    @Override
    public void compute() {
        generator.refreshGenerator();
    }

}
