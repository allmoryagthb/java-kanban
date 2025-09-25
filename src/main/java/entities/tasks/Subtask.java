package entities.tasks;

import enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private final Integer epicId;

    public Subtask(String title, String description, Status status, Integer epicId, LocalDateTime startTime, Duration duration) {
        super(title, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(int id, String title, String description, Status status, Integer epicId, LocalDateTime startTime, Duration duration) {
        super(id, title, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(Subtask subtask) {
        this(subtask.getId(), subtask.getTitle(), subtask.getDescription(), subtask.getStatus(), subtask.getEpicId(),
                subtask.getStartTime(), subtask.getDuration());
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d"
                .formatted(getId(), getClass().getSimpleName(), title, status, description, epicId));
    }
}
