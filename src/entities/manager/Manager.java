package entities.manager;

import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import enums.Status;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static enums.Status.*;

public class Manager {
    private Map<Integer, Task> tasks;
    private Map<Integer, Epic> epics;
    private Map<Integer, Subtask> subtasks;

    private static Integer taskIdCounter;
    private static Integer epicIdCounter;
    private static Integer subtaskIdCounter;

    public Manager() {
        this.tasks = new TreeMap<>();
        this.epics = new TreeMap<>();
        this.subtasks = new TreeMap<>();
    }

    // ***** МЕТОДЫ ДЛЯ TASK *****

    public void addNewTask(Task task) {
        if (task.getId() != null && tasks.containsKey(task.getId())) {
            System.out.printf(">>>ОШИБКА: Задача с id = '%d' уже существует!%n", task.getId());
            return;
        }
        if (task.getId() == null) {
            task.setId(++taskIdCounter);
        }
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        if (task.getId() == null) {
            System.out.println(">>>ОШИБКА: Передан невалидный id задачи!");
            return;
        }
        if (!tasks.containsKey(task.getId())) {
            System.out.printf(">>>ОШИБКА: Задачи с id = '%d' не существует!%n", task.getId());
            return;
        }
        tasks.put(task.getId(), task);
    }

    public void updateTaskTitle(int id, String title) {
        if (!tasks.containsKey(id)) {
            System.out.printf(">>>ОШИБКА: Задачи с id = '%d' не существует!%n", id);
            return;
        }
        tasks.get(id).setTitle(title);
    }

    public void updateTaskDescription(int id, String description) {
        if (!tasks.containsKey(id)) {
            System.out.printf(">>>ОШИБКА: Задачи с id = '%d' не существует!%n", id);
            return;
        }
        tasks.get(id).setDescription(description);
    }

    public void updateTaskStatus(int id, Status status) {
        if (!tasks.containsKey(id)) {
            System.out.printf(">>>ОШИБКА: Задачи с id = '%d' не существует!%n", id);
            return;
        }
        tasks.get(id).setStatus(status);
    }

    public Task getTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.printf(">>>ОШИБКА: Задачи с id = '%d' не существует!%n", id);
            return Task.EMPTY_TASK;
        }
        return tasks.get(id);
    }

    public Task deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.printf(">>>ОШИБКА: Задачи с id = '%d' не существует!%n", id);
            return Task.EMPTY_TASK;
        }
        return tasks.remove(id);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    // ***** МЕТОДЫ ДЛЯ EPIC *****

    public void addNewEpic(Epic epic) {
        if (epic.getId() != null && epics.containsKey(epic.getId())) {
            System.out.printf(">>>ОШИБКА: Эпик с id = '%s' уже существует!", epic.getId());
            return;
        } else {
            epic.setId(++epicIdCounter);
        }
        epics.put(epic.getId(), epic);
    }

    public void updateEpicTitle(int id, String title) {
        if (epics.get(id) == null) {
            System.out.printf(">>>ОШИБКА: Эпика с id = '%d' не существует!%n", id);
            return;
        }
        epics.get(id).setTitle(title);
    }

    public void updateEpicDescription(int id, String description) {
        if (epics.get(id) == null) {
            System.out.printf(">>>ОШИБКА: Эпика с id = '%d' не существует!%n", id);
            return;
        }
        epics.get(id).setDescription(description);
    }

    public List<Subtask> getEpicSubtasks(int id) {
        if (epics.get(id) == null) {
            System.out.printf(">>>ОШИБКА: Эпика с id = '%d' не существует!%n", id);
            return Collections.emptyList();
        }
        return epics.get(id).getSubtasksIds().stream().map(e -> subtasks.get(e)).toList();
    }

    public Epic deleteEpicById(int id) {
        if (epics.get(id) == null) {
            System.out.printf(">>>ОШИБКА: Эпика с id = '%d' не существует!%n", id);
            return Epic.EMPTY_EPIC;
        }
        epics.keySet().forEach(e1 -> {
            subtasks.values().forEach(e2 -> {
                if (e2.getEpicId().equals(e1)) {
                    subtasks.remove(e2.getId());
                }
            });
        });
        return epics.remove(id);
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    // ***** МЕТОДЫ ДЛЯ SUBTASKS *****

    public void addNewSubtask(Subtask subtask) {
        if (subtask.getId() != null && subtasks.containsKey(subtask.getId())) {
            System.out.printf(">>>ОШИБКА: Подзадача с id = '%d' уже существует!%n", subtask.getId());
            return;
        }
        if (subtask.getEpicId() == null) {
            System.out.println(">>>ОШИБКА: Невалидный номер эпика!");
            return;
        }
        if (!epics.containsKey(subtask.getEpicId())) {
            System.out.printf(">>>ОШИБКА: Эпика с id = '%d' не существует!%n", subtask.getEpicId());
            return;
        }
        if (subtask.getId() == null) {
            subtask.setId(++subtaskIdCounter);
        }
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtask(subtask.getId());
        checkEpicStatus(subtask.getEpicId());
    }

    public void updateSubtask(Subtask subtask) {
        if (subtask.getId() == null) {
            System.out.println(">>>ОШИБКА: Невалидный id подзадачи!");
            return;
        }
        if (subtasks.get(subtask.getId()) == null) {

        }
    }

    private void checkEpicStatus(int id) {
        Epic epic = epics.get(id);
        if (epic.getSubtasksIds().isEmpty() ||
                epic.getSubtasksIds().stream().allMatch(e -> subtasks.get(e).getStatus().equals(NEW)) ) {
            epic.setStatus(NEW);
            return;
        } else if (epic.getSubtasksIds().stream().allMatch(e -> subtasks.get(e).getStatus().equals(DONE))) {
            epic.setStatus(DONE);
            return;
        }
        epic.setStatus(IN_PROGRESS);
    }
}
