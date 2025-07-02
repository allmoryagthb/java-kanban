package entities.tasks;

public class Subtask extends Task {
    private final Epic epic;

    public Subtask(String title, String description, Epic epic) {
        super(title, description);
        this.epic = epic;
        epic.addSubtask(this);
        print();
    }

    public Subtask(int id, String title, String description, Epic epic) {
        super(id, title, description);
        this.epic = epic;
        epic.addSubtask(this);
        print();
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + taskId +
                ", description=" + description +
                ", epic=" + epic +
                '}';
    }

    private void print() {
        System.out.printf("Создана новая подзадача '%s' с id = '%d'%n", description, taskId);
    }
}
