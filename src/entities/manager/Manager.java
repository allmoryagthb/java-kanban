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

    private int idCounter;

    public Manager() {
        this.tasks = new TreeMap<>();
        this.epics = new TreeMap<>();
        this.subtasks = new TreeMap<>();
    }

    // ***** МЕТОДЫ ДЛЯ TASK *****

    public int addNewTask(Task task) {
        if (task != null) {
            task.setId(++idCounter);
            tasks.put(idCounter, task);
            return idCounter;
        }
        return -1;
    }

    public boolean updateTask(Task task) {
        if (task == null || task.getId() == null || !tasks.containsKey(task.getId()))
            return false;
        return tasks.put(task.getId(), task) != null;
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public List<Task> getAllTasks() {
        return tasks.values()
                .stream()
                .toList();
    }

    public boolean deleteTaskById(int id) {
        return tasks.remove(id) != null;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    // ***** МЕТОДЫ ДЛЯ EPIC *****

    public int addNewEpic(Epic epic) {
        if (epic != null) {
            epic.setId(++idCounter);
            epics.put(idCounter, epic);
            return idCounter;
        }
        return -1;
    }

    public boolean updateEpic(Epic epic) {
        if (epic == null || epic.getId() == null || !epics.containsKey(epic.getId())) {
            return false;
        }
        epics.get(epic.getId()).setTitle(epic.getTitle());
        epics.get(epic.getId()).setDescription(epic.getDescription());
        return true;
    }

    public List<Epic> getAllEpics() {
        return epics.values()
                .stream()
                .toList();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public List<Subtask> getEpicSubtasks(int id) {
        if (!epics.containsKey(id)) {
            return Collections.emptyList();
        }
        return epics.get(id).getSubtasksIds()
                .stream()
                .map(subtaskId -> subtasks.get(subtaskId))
                .toList();
    }

    public boolean deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            epics.get(id).getSubtasksIds()
                    .forEach(subtaskId -> subtasks.remove(subtaskId));
        }
        return epics.remove(id) != null;
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    // ***** МЕТОДЫ ДЛЯ SUBTASKS *****

    public int addNewSubtask(Subtask subtask) {
        if (subtasks == null || subtask.getEpicId() == null || !epics.containsKey(subtask.getEpicId())) {
            return -1;
        }
        subtask.setId(++idCounter);
        subtasks.put(idCounter, subtask);
        epics.get(subtask.getEpicId()).addSubtask(subtask.getId());
        updateEpicStatus(subtask.getEpicId());
        return idCounter;
    }

    public boolean updateSubtask(Subtask subtask) {
        if (subtask == null ||
                subtask.getId() == null ||
                !subtasks.containsKey(subtask.getId()) ||
                !Objects.equals(subtasks.get(subtask.getId()).getEpicId(), subtask.getEpicId())) {
            return false;
        }
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
        return true;
    }

    public List<Subtask> getAllSubtasks() {
        return subtasks.values()
                .stream()
                .toList();
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public boolean deleteSubtaskById(int id) {
        if (!subtasks.containsKey(id)) {
            return false;
        }
        epics.get(subtasks.get(id).getEpicId()).deleteSubtaskIdById(id);
        updateEpicStatus(subtasks.get(id).getEpicId());
        return subtasks.remove(id) != null;
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.deleteAllSubtasksIds();
            updateEpicStatus(epic.getId());
        });
    }

    private void updateEpicStatus(int id) {
        Epic epic = epics.get(id);
        if (epic.getSubtasksIds().isEmpty() ||
                epic.getSubtasksIds()
                        .stream()
                        .allMatch(subtaskId -> subtasks.get(subtaskId).getStatus().equals(NEW))) {
            epic.setStatus(NEW);
            return;
        } else if (epic.getSubtasksIds()
                .stream()
                .allMatch(subtaskId -> subtasks.get(subtaskId).getStatus().equals(DONE))) {
            epic.setStatus(DONE);
            return;
        }
        epic.setStatus(IN_PROGRESS);
    }
}
