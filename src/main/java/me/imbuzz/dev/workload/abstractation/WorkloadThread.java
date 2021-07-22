package me.imbuzz.dev.workload.abstractation;

import com.google.common.collect.Queues;
import lombok.SneakyThrows;

import java.util.ArrayDeque;

public class WorkloadThread implements Runnable {

    private final int MAX_MS_PER_TICK = 10;
    private final ArrayDeque<Workload> workloadDeque = Queues.newArrayDeque();

    public void addLoad(Workload workload){
        workloadDeque.add(workload);
    }

    public void removeWorkLoad(Workload workload) {
        workloadDeque.remove(workload);
    }

    @Override
    @SneakyThrows
    public void run() {
        long stopTime = System.currentTimeMillis() + MAX_MS_PER_TICK;
        while (!workloadDeque.isEmpty() && System.currentTimeMillis() <= stopTime){
            workloadDeque.poll().compute();
        }
    }

}
