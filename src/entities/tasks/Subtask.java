package entities.tasks;

public class Subtask extends BaseTask {
    private final Epic epic;

    public Subtask(String title, String description, Epic epic) {
        super(title, description);
        this.epic = epic;
        epic.addSubtask(this);
    }

    public Subtask(int id, String title, String description, Epic epic) {
        super(id, title, description);
        this.epic = epic;
        epic.addSubtask(this);
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + taskId +
                ", title=" + title +
                ", description=" + description +
                ", status=" + status +
                ", epic_id=" + epic.getTaskId() +
                '}';
    }
}
