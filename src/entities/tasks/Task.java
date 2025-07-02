package entities.tasks;

public class Task extends BaseTask {

    public Task(String title, String description) {
        super(title, description);
        print();
    }

    public Task(int id, String title, String description) {
        super(id, title, description);
        print();
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskNum=" + taskId +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    private void print() {
        System.out.printf("Создана новая задача '%s' с id = '%d'%n", description, taskId);
    }
}
