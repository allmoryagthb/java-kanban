package entities.tasks;

import enums.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends BaseTask {
    private List<Subtask> subtasks = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
        print();
    }

    public Epic(int id, String title, String description) {
        super(id, title, description);
        print();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + taskId +
                ", description=" + description +
                ", subtasks=" + subtasks +
                '}';
    }

    private void print() {
        System.out.printf("Создан новый эпик '%s' с id = '%d'%n", description, taskId);
    }
}
