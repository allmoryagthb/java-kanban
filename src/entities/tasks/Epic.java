package entities.tasks;

import enums.Status;

import java.util.HashSet;
import java.util.Set;

public class Epic extends Task {
    public static final Epic EMPTY_EPIC = new Epic("NOT FOUND", "NOT FOUND");
    private Set<Integer> subtasksIds = new HashSet<>();

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
    }

    public Epic(int id, String title, String description) {
        super(id, title, description, Status.NEW);
    }

    public void addSubtask(Integer id) {
        subtasksIds.add(id);
    }

    public Set<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", status=" + status +
                ", subtasksIds=" + subtasksIds +
                '}';
    }
}
