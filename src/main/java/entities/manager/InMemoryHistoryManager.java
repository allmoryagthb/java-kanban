package entities.manager;

import entities.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new LinkedList<>();
    private static final int MAX_SIZE = 10;

    @Override
    public boolean addTask(Task task) {
        if (task != null && history.size() < MAX_SIZE) {
            return this.history.add(task);
        } else if (task != null && history.size() == MAX_SIZE) {
            this.history.addFirst(task);
            this.history.remove(MAX_SIZE);
            return true;
        }
        return false;
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }
}
