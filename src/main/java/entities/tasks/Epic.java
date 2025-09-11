package entities.tasks;

import enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Epic extends Task {
    private Set<Integer> subtasksIds = new HashSet<>();
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description, Status.NEW, LocalDateTime.now(), Duration.ofNanos(0));
        this.endTime = LocalDateTime.now();
    }

    public Epic(int id, String title, String description) {
        super(id, title, description, Status.NEW, LocalDateTime.now(), Duration.ofNanos(0));
        this.endTime = LocalDateTime.now();
    }

    public void addSubtask(int id) {
        subtasksIds.add(id);
    }

    public Set<Integer> getSubtasksIds() {
        return Set.copyOf(subtasksIds);
    }

    public boolean deleteSubtaskIdById(int id) {
        return subtasksIds.remove(id);
    }

    public void deleteAllSubtasksIds() {
        subtasksIds.clear();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
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
        return "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtasksIds=" + subtasksIds +
                '}';
    }
}
