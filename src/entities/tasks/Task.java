package entities.tasks;

public class Task extends BaseTask {

    public Task(String title, String description) {
        super(title, description);
    }

    public Task(int id, String title, String description) {
        super(id, title, description);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskNum=" + taskId +
                ", title=" + title +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
