package util;

import entities.manager.FileBackedTaskManager;
import entities.manager.HistoryManager;
import entities.manager.InMemoryHistoryManager;
import entities.manager.InMemoryTaskManager;

import java.io.File;

public class Managers {

    private Managers(){}

    public static FileBackedTaskManager getDefault() {
        File file = new File("vault.csv");
        if (file.exists())
            file.delete();
        return new FileBackedTaskManager(new File("vault.csv"));
    }

    public static InMemoryTaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
