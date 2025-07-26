package entities.manager;

import entities.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new LinkedList<>();
    private static final int MAX_SIZE = 10;

    @Override
    public void addTask(Task task) {
        history.add(new Task(task));
        if (history.size() > MAX_SIZE)
            history.removeFirst();
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }
}
