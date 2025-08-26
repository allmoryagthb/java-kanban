package entities.manager;

import entities.tasks.Task;

import java.util.List;

public interface HistoryManager {

    void addTask(Task task);

    void remove(int id);

    List<Task> getHistory();
}
