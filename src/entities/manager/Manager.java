package entities.manager;

import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;

import java.util.*;

import static enums.Status.*;

public class Manager {
    private Map<Integer, Task> tasks;
    private Map<Integer, Epic> epics;
    private Map<Integer, Subtask> subtasks;

    private static int taskIdCounter;
    private static int epicIdCounter;
    private static int subtaskIdCounter;

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
            do {
                task.setId(++taskIdCounter);
            } while (tasks.containsKey(task.getId()));
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

    public Task getTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.printf(">>>ОШИБКА: Задачи с id = '%d' не существует!%n", id);
            return Task.NOT_FOUND_TASK;
        }
        return tasks.get(id);
    }

    public List<Task> getAllTasks() {
        return tasks.values().stream().toList();
    }

    public Task deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.printf(">>>ОШИБКА: Задачи с id = '%d' не существует!%n", id);
            return Task.NOT_FOUND_TASK;
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
        }
        if (epic.getId() == null) {
            do {
                epic.setId(++epicIdCounter);
            } while (epics.containsKey(epic.getId()));
        }
        epics.put(epic.getId(), epic);
    }

    public void updateEpic(Epic epic) {
        if (epic.getId() == null) {
            System.out.println(">>>ОШИБКА: Передано невалидное значение id эпика!");
            return;
        }
        if (!epics.containsKey(epic.getId())) {
            System.out.printf(">>>ОШИБКА: Эпика с id = '%d' не существует!%n", epic.getId());
            return;
        }
        epics.get(epic.getId()).setTitle(epic.getTitle());
        epics.get(epic.getId()).setDescription(epic.getDescription());
        updateEpicStatus(epic.getId());
    }

    public List<Epic> getAllEpics() {
        return epics.values().stream().toList();
    }

    public Epic getEpicById(int id) {
        if(!epics.containsKey(id)) {
            System.out.printf(">>>ОШИБКА: Эпика с id = '%d' не существует!%n", id);
            return Epic.NOT_FOUND_EPIC;
        }
        return epics.get(id);
    }

    public List<Subtask> getEpicSubtasks(int id) {
        if (!epics.containsKey(id)) {
            System.out.printf(">>>ОШИБКА: Эпика с id = '%d' не существует!%n", id);
            return Collections.emptyList();
        }
        return epics.get(id).getSubtasksIds().stream().map(e -> subtasks.get(e)).toList();
    }

    public Epic deleteEpicById(int id) {
        if (!epics.containsKey(id)) {
            System.out.printf(">>>ОШИБКА: Эпика с id = '%d' не существует!%n", id);
            return Epic.NOT_FOUND_EPIC;
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
            do {
                subtask.setId(++subtaskIdCounter);
            } while (subtasks.containsKey(subtask.getId()));
        }
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtask(subtask.getId());
        updateEpicStatus(subtask.getEpicId());
    }

    public void updateSubtask(Subtask subtask) {
        if (subtask.getId() == null) {
            System.out.println(">>>ОШИБКА: Невалидный id подзадачи!");
            return;
        }
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.printf(">>>ОШИБКА: Подзадача с id = '%d' не существует!%n", subtask.getId());
            return;
        }
        if (!Objects.equals(subtasks.get(subtask.getId()).getEpicId(), subtask.getEpicId())) {
            System.out.println(">>>ОШИБКА: Подзадача ссылается на другой эпик!");
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    public List<Subtask> getAllSubtasks() {
        return subtasks.values().stream().toList();
    }

    public Subtask getSubtaskById(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.printf(">>>ОШИБКА: Подзадача с id = '%d' не существует!%n", id);
            return Subtask.NOT_FOUND_SUBTASK;
        }
        return subtasks.get(id);
    }

    public Subtask deleteSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.printf(">>>ОШИБКА: Подзадача с id = '%d' не существует!%n", id);
            return Subtask.NOT_FOUND_SUBTASK;
        }
        epics.get(subtasks.get(id).getEpicId()).getSubtasksIds().remove(id);
        updateEpicStatus(subtasks.get(id).getEpicId());
        return subtasks.remove(id);
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        epics.values().forEach(e -> {
            e.getSubtasksIds().clear();
            e.setStatus(NEW);
        });

    }

    private void updateEpicStatus(int id) {
        Epic epic = epics.get(id);
        if (epic.getSubtasksIds().isEmpty() ||
                epic.getSubtasksIds().stream().allMatch(e -> subtasks.get(e).getStatus().equals(NEW))) {
            epic.setStatus(NEW);
            return;
        } else if (epic.getSubtasksIds().stream().allMatch(e -> subtasks.get(e).getStatus().equals(DONE))) {
            epic.setStatus(DONE);
            return;
        }
        epic.setStatus(IN_PROGRESS);
    }
}
