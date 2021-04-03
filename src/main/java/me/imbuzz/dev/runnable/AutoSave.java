package me.imbuzz.dev.runnable;

import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.files.FilesManager;
import me.imbuzz.dev.managers.GeneratorManager;

@RequiredArgsConstructor
public class AutoSave implements Runnable {

    private final GeneratorManager generatorManager;
    private final FilesManager filesManager;

    @Override
    public void run() {
        generatorManager.saveOnlineGenerators(filesManager);
    }



}
