package entities.manager;

import entities.tasks.Task;

import java.util.List;

public interface HistoryManager {

    boolean addTask(Task task);

    List<Task> getHistory();
}
