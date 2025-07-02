package entities.tasks;

import java.util.*;

public class Epic extends BaseTask {
    private Map<Integer, Subtask> subtasks = new TreeMap<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(int id, String title, String description) {
        super(id, title, description);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);
    }

    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + taskId +
                ", title=" + title +
                ", description=" + description +
                ", status=" + status +
                ", subtasks=" + subtasks +
                '}';
    }
}
