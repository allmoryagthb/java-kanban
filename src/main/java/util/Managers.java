package util;

import entities.manager.HistoryManager;
import entities.manager.InMemoryHistoryManager;
import entities.manager.InMemoryTaskManager;
import entities.manager.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
