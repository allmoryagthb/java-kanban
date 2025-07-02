package entities.manager;

import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import enums.Status;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Manager {
    private Map<Integer, Task> taskMap;
    private Map<Integer, Epic> epicMap;
    private Map<Integer, Subtask> subtaskMap;

    public Manager() {
        this.taskMap = new TreeMap<>();
        this.epicMap = new TreeMap<>();
        this.subtaskMap = new TreeMap<>();
    }

    public List<Task> getTaskList() {
        return taskMap.values().stream().toList();
    }

    public List<Epic> getEpicList() {
        return epicMap.values().stream().toList();
    }

    public List<Subtask> getSubtaskList() {
        return subtaskMap.values().stream().toList();
    }

    public void addTask(Task task) {
        taskMap.put(task.getTaskId(), task);
    }

    public void addEpic(Epic epic) {
        epicMap.put(epic.getTaskId(), epic);
    }

    public Task getTaskById(int id) {
        checkTaskIsExistsById(id);
        return taskMap.get(id);
    }

    public Epic getEpicById(int id) {
        checkEpicIsExistsById(id);
        return epicMap.get(id);
    }

    public void changeTaskDescriptionById(int id, String description) {
        checkTaskIsExistsById(id);
        taskMap.get(id).setDescription(description);
    }

    public void changeEpicDescriptionById(int id, String description) {
        checkEpicIsExistsById(id);
        epicMap.get(id).setDescription(description);
    }

    public void changeTaskStatusById(int id, Status newStatus) {
        checkTaskIsExistsById(id);
        taskMap.get(id).setStatus(newStatus);
    }

    public void changeEpicStatusById(int id, Status newStatus) {
        checkEpicIsExistsById(id);
        epicMap.get(id).setStatus(newStatus);
    }

    public void deleteAllTasks() {
        this.taskMap.clear();
    }

    public void deleteAllEpics() {
        this.epicMap.clear();
    }

    public void deleteTaskById(int id) {
        checkTaskIsExistsById(id);
        taskMap.remove(id);
    }

    public void deleteEpicById(int id) {
        checkEpicIsExistsById(id);
        epicMap.remove(id);
    }

    private void checkTaskIsExistsById(int id) {
        try {
            if (taskMap.get(id) == null)
                throw new Exception();
        } catch (Exception e) {
            System.out.printf(">>>ОШИБКА: Задача с id = '%d' не найдена!%n", id);
        }
    }

    private void checkEpicIsExistsById(int id) {
        try {
            if (epicMap.get(id) == null)
                throw new Exception();
        } catch (Exception e) {
            System.out.printf(">>>ОШИБКА: Эпик с id = '%d' не найдена!%n", id);
        }
    }
}
