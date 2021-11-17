package me.imbuzz.dev.workload.abstractation;

import lombok.SneakyThrows;

import java.util.concurrent.ConcurrentLinkedDeque;

public class WorkloadThread implements Runnable {

    private final int MAX_MS_PER_TICK = 10;
    private final ConcurrentLinkedDeque<Workload> workloadDeque = new ConcurrentLinkedDeque<>();

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
