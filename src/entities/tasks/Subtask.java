package entities.tasks;

import static enums.Status.*;

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
        tryToChangeEpicStatus();
    }

    public Epic getEpic() {
        return epic;
    }

    /**
     * Метод для попытки изменения статуса эпика, если все статусы сабтасок соответствуют условиям для изменения
     */
    private void tryToChangeEpicStatus() {
        if (this.status == NEW) {
            epic.setStatus(NEW);
        } else if (this.status == IN_PROGRESS && epic.getSubtasks().stream().noneMatch(e -> e.getStatus() == NEW)) {
            epic.setStatus(IN_PROGRESS);
        } else if (this.status == DONE && epic.getSubtasks().stream().noneMatch(e -> e.getStatus() == IN_PROGRESS)) {
            epic.setStatus(DONE);
        }
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

    private void print() {
        System.out.printf("Создана новая подзадача '%s' с id = '%d'%n", description, taskId);
    }
}
