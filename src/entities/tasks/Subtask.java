package entities.tasks;

import enums.Status;

public class Subtask extends Task {
    public static final Subtask EMPTY_TASK = new Subtask("NOT FOUND", "NOT FOUND", null, null);
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
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", status=" + status +
                ", epic_id=" + epicId +
                '}';
    }
}
