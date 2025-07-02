package entities.tasks;

import enums.Status;

import java.util.Objects;

import static enums.Status.NEW;

public abstract class BaseTask {

    protected static int taskIdCounter;
    protected final int taskId;
    protected String title;
    protected String description;
    protected Status status;

    public BaseTask(String title, String description) {
        this.taskId = ++taskIdCounter;
        this.title = title;
        this.description = description;
        this.status = NEW;
    }

    public BaseTask(int id, String title, String description) {
        this.taskId = id;
        this.title = title;
        this.description = description;
        this.status = NEW;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BaseTask baseTask = (BaseTask) o;
        return taskId == baseTask.taskId && Objects.equals(description, baseTask.description) && status == baseTask.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, description, status);
    }
}
