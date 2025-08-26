package entities.manager;

import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import util.Managers;

import java.util.*;

import static enums.Status.*;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks;
    private Map<Integer, Epic> epics;
    private Map<Integer, Subtask> subtasks;
    private HistoryManager historyManager;

    private int idCounter;

    public InMemoryTaskManager() {
        this.tasks = new TreeMap<>();
        this.epics = new TreeMap<>();
        this.subtasks = new TreeMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    // ***** МЕТОДЫ ДЛЯ TASK *****

    @Override
    public int addNewTask(Task task) {
        if (task == null || task.getId() != null) {
            return -1;
        }
        task.setId(++idCounter);
        tasks.put(idCounter, task);

        return idCounter;
    }

    @Override
    public boolean updateTask(Task task) {
        if (task == null || task.getId() == null || !tasks.containsKey(task.getId()))
            return false;
        return tasks.put(task.getId(), task) != null;
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null)
            historyManager.addTask(task);
        return tasks.get(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return tasks.values()
                .stream()
                .toList();
    }

    @Override
    public boolean deleteTaskById(int id) {
        historyManager.remove(id);
        return tasks.remove(id) != null;
    }

    @Override
    public void deleteAllTasks() {
        tasks.keySet().forEach(taskId -> historyManager.remove(taskId));
        tasks.clear();
    }

    // ***** МЕТОДЫ ДЛЯ EPIC *****

    @Override
    public int addNewEpic(Epic epic) {
        if (epic == null || epic.getId() != null) {
            return -1;
        }
        epic.setId(++idCounter);
        epics.put(idCounter, epic);
        return idCounter;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (epic == null || epic.getId() == null || !epics.containsKey(epic.getId())) {
            return false;
        }
        epics.get(epic.getId()).setTitle(epic.getTitle());
        epics.get(epic.getId()).setDescription(epic.getDescription());
        return true;
    }

    @Override
    public List<Epic> getAllEpics() {
        return epics.values()
                .stream()
                .toList();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null)
            historyManager.addTask(epic);
        return epics.get(id);
    }

    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        if (!epics.containsKey(id)) {
            return Collections.emptyList();
        }
        return epics.get(id).getSubtasksIds()
                .stream()
                .map(subtaskId -> subtasks.get(subtaskId))
                .toList();
    }

    @Override
    public boolean deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            epics.get(id).getSubtasksIds()
                    .forEach(subtaskId -> {
                        subtasks.remove(subtaskId);
                        historyManager.remove(subtaskId);
                    });
        }
        historyManager.remove(id);;
        return epics.remove(id) != null;
    }

    @Override
    public void deleteAllEpics() {
        epics.keySet().forEach(epicId -> historyManager.remove(epicId));
        subtasks.keySet().forEach(subtaskId -> historyManager.remove(subtaskId));
        epics.clear();
        subtasks.clear();
    }

    // ***** МЕТОДЫ ДЛЯ SUBTASKS *****

    @Override
    public int addNewSubtask(Subtask subtask) {
        if (subtask == null || subtask.getId() != null ||
                subtask.getEpicId() == null || !epics.containsKey(subtask.getEpicId())) {
            return -1;
        }
        subtask.setId(++idCounter);
        subtasks.put(idCounter, subtask);
        epics.get(subtask.getEpicId()).addSubtask(subtask.getId());
        updateEpicStatus(subtask.getEpicId());
        return idCounter;
    }

    @Override
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

    @Override
    public List<Subtask> getAllSubtasks() {
        return subtasks.values()
                .stream()
                .toList();
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null)
            historyManager.addTask(subtask);
        return subtasks.get(id);
    }

    @Override
    public boolean deleteSubtaskById(int id) {
        if (!subtasks.containsKey(id)) {
            return false;
        }
        epics.get(subtasks.get(id).getEpicId()).deleteSubtaskIdById(id);
        historyManager.remove(id);
        updateEpicStatus(subtasks.get(id).getEpicId());
        return subtasks.remove(id) != null;
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.keySet().forEach(subtasksId -> historyManager.remove(subtasksId));
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.deleteAllSubtasksIds();
            updateEpicStatus(epic.getId());
        });
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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
