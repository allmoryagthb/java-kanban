package entities.manager;

import entities.tasks.BaseTask;
import enums.Status;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class BaseTaskManager<T extends BaseTask> {
    protected Map<Integer, T> genericMap;

    public BaseTaskManager() {
        this.genericMap = new TreeMap<>();
    }

    public List<T> getTaskList() {
        return genericMap.values().stream().toList();
    }

    public void addTask(T task) {
        if (genericMap.get(task.getTaskId()) != null && genericMap.get(task.getTaskId()).getStatus() == Status.NEW) {
            task.setStatus(Status.IN_PROGRESS);
            genericMap.put(task.getTaskId(), task);
        } else if (genericMap.get(task.getTaskId()) != null && genericMap.get(task.getTaskId()).getStatus() == Status.IN_PROGRESS) {
            task.setStatus(Status.DONE);
            genericMap.put(task.getTaskId(), task);
        } else {
            genericMap.put(task.getTaskId(), task);
            task.setStatus(task.getStatus());
        }
    }

    public T getTaskById(int id) {
        checkTaskIsExistsById(id);
        return genericMap.get(id);
    }

    public void changeTaskDescriptionById(int id, String description) {
        checkTaskIsExistsById(id);
        genericMap.get(id).setDescription(description);
    }

    public void changeTaskStatusById(int id, Status newStatus) {
        checkTaskIsExistsById(id);
        genericMap.get(id).setStatus(newStatus);
    }

    public void deleteAllTasks() {
        this.genericMap.clear();
    }

    public void deleteTaskById(int id) {
        checkTaskIsExistsById(id);
        genericMap.remove(id);
    }

    protected void checkTaskIsExistsById(int id) {
        try {
            if (genericMap.get(id) == null)
                throw new Exception();
        } catch (Exception e) {
            System.out.printf(">>>ОШИБКА: Задача с id = '%d' не найдена!%n", id);
        }
    }
}
