package entities.tasks;

import enums.Status;

import java.util.Objects;

public class Subtask extends Task {
    public static final Subtask NOT_FOUND_SUBTASK = new Subtask("NOT FOUND", "NOT FOUND", null, null);
    private final Integer epicId;

    public Subtask(String title, String description, Status status, Integer epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public Subtask(int id, String title, String description, Status status, Integer epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", epic_id=" + epicId +
                '}';
    }
}
