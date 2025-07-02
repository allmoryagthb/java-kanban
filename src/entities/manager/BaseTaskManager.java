package entities.manager;

import entities.tasks.BaseTask;
import enums.Status;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static enums.Status.*;

public abstract class BaseTaskManager<T extends BaseTask> {
    protected Map<Integer, T> genericMap;

    public BaseTaskManager() {
        this.genericMap = new TreeMap<>();
    }

    public List<T> getTaskList() {
        return genericMap.values().stream().toList();
    }

    public void addTask(T task) {
        if (genericMap.get(task.getTaskId()) != null && genericMap.get(task.getTaskId()).getStatus() == NEW) {
            task.setStatus(IN_PROGRESS);
            genericMap.put(task.getTaskId(), task);
        } else if (genericMap.get(task.getTaskId()) != null &&
                (genericMap.get(task.getTaskId()).getStatus() == IN_PROGRESS ||
                        genericMap.get(task.getTaskId()).getStatus() == DONE)) {
            task.setStatus(DONE);
            genericMap.put(task.getTaskId(), task);
        } else {
            task.setStatus(NEW);
            genericMap.put(task.getTaskId(), task);
        }
    }

    public T getTaskById(int id) {
        if (checkTaskIsExistsById(id))
            return genericMap.get(id);
        return null;
    }

    public void changeTaskDescriptionById(int id, String description) {
        if (checkTaskIsExistsById(id))
            genericMap.get(id).setDescription(description);
    }

    public void changeTaskStatusById(int id, Status newStatus) {
        if (checkTaskIsExistsById(id))
            genericMap.get(id).setStatus(newStatus);
    }

    public void deleteAllTasks() {
        this.genericMap.clear();
    }

    public void deleteTaskById(int id) {
        if (checkTaskIsExistsById(id))
            genericMap.remove(id);
    }

    protected boolean checkTaskIsExistsById(int id) {
        if (genericMap.containsKey(id)) {
            return true;
        }
        System.out.printf(">>>ОШИБКА: Задача с id = '%d' не найдена!%n", id);
        return false;
    }

    @Override
    public String toString() {
        return genericMap.toString();
    }
}
