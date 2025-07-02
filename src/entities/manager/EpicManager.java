package entities.manager;

import entities.tasks.Epic;
import entities.tasks.Subtask;
import enums.Status;

import java.util.List;

import static enums.Status.*;

public class EpicManager extends BaseTaskManager<Epic> {

    public List<Subtask> getSubtask(int id) {
        checkTaskIsExistsById(id);
        return genericMap.get(id).getSubtasks();
    }

    @Override
    public void changeTaskStatusById(int id, Status newStatus) {
        checkTaskIsExistsById(id);
        checkStatus(id, newStatus);
        genericMap.get(id).setStatus(newStatus);
    }

    private void checkStatus(int id, Status newStatus) {
        Status currentTaskStatus = NEW;
        try {
            if (newStatus == IN_PROGRESS &&
                    genericMap.get(id).getSubtasks().stream().anyMatch(e -> e.getStatus() == NEW)) {
                throw new Exception();
            } else if (newStatus == DONE &&
                    genericMap.get(id).getSubtasks().stream().anyMatch(e -> e.getStatus() == IN_PROGRESS)) {
                currentTaskStatus = IN_PROGRESS;
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.printf(">>>ОШИБКА: Невозможно изменить статус эпика на '%s', пока статус подзадач в статусе '%s'%n",
                    newStatus, currentTaskStatus);
        }
    }
}
