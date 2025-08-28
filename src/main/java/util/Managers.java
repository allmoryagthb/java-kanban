package util;

import entities.manager.FileBackedTaskManager;
import entities.manager.HistoryManager;
import entities.manager.InMemoryHistoryManager;

import java.io.File;

public class Managers {

    public static FileBackedTaskManager getDefault() {
        File file = new File("vault.csv");
        if (file.exists())
            file.delete();
        return new FileBackedTaskManager(new File("vault.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
