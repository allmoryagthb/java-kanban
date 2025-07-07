package entities.tasks;

import enums.Status;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Epic extends Task {
    private Set<Integer> subtasksIds = new HashSet<>();

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
    }

    public Epic(int id, String title, String description) {
        super(id, title, description, Status.NEW);
    }

    public void addSubtask(int id) {
        subtasksIds.add(id);
    }

    public Set<Integer> getSubtasksIds() {
        return new HashSet<>(subtasksIds);
    }

    public boolean deleteSubtaskIdById(int id) {
        return subtasksIds.remove(id);
    }

    public void deleteAllSubtasksIds() {
        subtasksIds.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksIds, epic.subtasksIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksIds);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtasksIds=" + subtasksIds +
                '}';
    }
}
