package entities.manager;

import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import exceptions.TaskValidationException;
import util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static enums.Status.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Epic> epics;
    protected final Map<Integer, Subtask> subtasks;
    protected final TreeSet<Task> prioritizedTasks;
    protected final HistoryManager historyManager;

    private int idCounter;

    public InMemoryTaskManager() {
        this.tasks = new TreeMap<>();
        this.epics = new TreeMap<>();
        this.subtasks = new TreeMap<>();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        this.historyManager = Managers.getDefaultHistory();
    }

    // ***** МЕТОДЫ ДЛЯ TASK *****

    @Override
    public int addTask(Task task) {
        if (task == null || task.getId() != null) {
            return -1;
        }
        task.setId(++idCounter);
        tasks.put(idCounter, task);
        add(task);
        return idCounter;
    }

    @Override
    public boolean updateTask(Task task) {
        if (task == null || task.getId() == null || !tasks.containsKey(task.getId()))
            return false;
        if (task.getStartTime() == null)
            task.setStartTime(tasks.get(task.getId()).getStartTime());
        if (task.getDuration() == null)
            task.setDuration(tasks.get(task.getId()).getDuration());

        prioritizedTasks.remove(tasks.get(task.getId()));
        if (task.getStartTime() != null)
            add(task);
        return tasks.put(task.getId(), task) != null;
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task == null)
            return null;
        historyManager.addTask(task);
        return new Task(task);
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
        prioritizedTasks.remove(tasks.get(id));
        return tasks.remove(id) != null;
    }

    @Override
    public void deleteAllTasks() {
        tasks.keySet().forEach(historyManager::remove);
        prioritizedTasks.removeAll(tasks.values());
        tasks.clear();
    }

    // ***** МЕТОДЫ ДЛЯ EPIC *****

    @Override
    public int addEpic(Epic epic) {
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
        if (epic == null)
            return null;
        historyManager.addTask(epic);
        return new Epic(epic);
    }

    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        if (!epics.containsKey(id)) {
            return Collections.emptyList();
        }
        return epics.get(id).getSubtasksIds()
                .stream()
                .map(subtasks::get)
                .toList();
    }

    @Override
    public boolean deleteEpicById(int id) {
        if (epics.containsKey(id) && !epics.get(id).getSubtasksIds().isEmpty()) {
            epics.get(id).getSubtasksIds()
                    .forEach(subtaskId -> {
                        prioritizedTasks.remove(subtasks.get(subtaskId));
                        subtasks.remove(subtaskId);
                        historyManager.remove(subtaskId);
                    });
        }
        historyManager.remove(id);
        return epics.remove(id) != null;
    }

    @Override
    public void deleteAllEpics() {
        epics.keySet().forEach(historyManager::remove);
        subtasks.keySet().forEach(historyManager::remove);
        prioritizedTasks.removeAll(subtasks.values());
        epics.clear();
        subtasks.clear();
    }

    // ***** МЕТОДЫ ДЛЯ SUBTASKS *****

    @Override
    public int addSubtask(Subtask subtask) {
        if (subtask == null ||
                subtask.getId() != null ||
                subtask.getEpicId() == null ||
                !epics.containsKey(subtask.getEpicId())) {
            return -1;
        }
        subtask.setId(++idCounter);
        subtasks.put(idCounter, subtask);
        epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId());
        updateEpicStatus(subtask.getEpicId());
        updateEpicTimeStatuses(subtask.getEpicId());
        if (subtask.getStartTime() != null)
            add(subtask);
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
        if (subtask.getStartTime() == null)
            subtask.setStartTime(subtasks.get(subtask.getId()).getStartTime());
        if (subtask.getDuration() == null)
            subtask.setDuration(subtasks.get(subtask.getId()).getDuration());

        prioritizedTasks.remove(subtasks.get(subtask.getId()));
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
        updateEpicTimeStatuses(subtask.getEpicId());
        add(subtask);
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
        if (subtask == null)
            return null;
        historyManager.addTask(subtask);
        return new Subtask(subtasks.get(id));
    }

    @Override
    public boolean deleteSubtaskById(int id) {
        if (!subtasks.containsKey(id)) {
            return false;
        }
        epics.get(subtasks.get(id).getEpicId()).deleteSubtaskIdById(id);
        historyManager.remove(id);
        updateEpicStatus(subtasks.get(id).getEpicId());
        updateEpicTimeStatuses(subtasks.get(id).getEpicId());
        if (checkPrioritizedTasksContainsId(subtasks.get(id).getId()))
            prioritizedTasks.remove(subtasks.get(id));
        return subtasks.remove(id) != null;
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.keySet().forEach(historyManager::remove);
        prioritizedTasks.removeAll(subtasks.values());
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.deleteAllSubtasksIds();
            updateEpicStatus(epic.getId());
            updateEpicTimeStatuses(epic.getId());
        });
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public int getIdCounter() {
        return this.idCounter;
    }

    protected void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
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

    private void updateEpicTimeStatuses(int id) {
        Epic epic = epics.get(id);

        if (!epic.getSubtasksIds().isEmpty()) {
            AtomicReference<LocalDateTime> start = new AtomicReference<>(LocalDateTime.MAX);
            AtomicReference<Duration> duration = new AtomicReference<>(Duration.ofMillis(0));
            AtomicReference<LocalDateTime> end = new AtomicReference<>(LocalDateTime.MIN);

            epic.getSubtasksIds().forEach(subtaskId -> {
                Subtask subtask = subtasks.get(subtaskId);

                if (subtask.getStartTime() != null && subtask.getStartTime().isBefore(start.get()))
                    start.set(subtask.getStartTime());

                if (subtask.getEndTime() != null && subtask.getEndTime().isAfter(end.get()))
                    end.set(subtask.getEndTime());

                if (subtask.getDuration() != null)
                    duration.getAndUpdate(e -> e.plus(subtask.getDuration()));
            });

            if (start.get() != LocalDateTime.MAX)
                epic.setStartTime(start.get());
            if (end.get() != LocalDateTime.MIN)
                epic.setEndTime(end.get());
            if (!duration.get().equals(Duration.ofMillis(0)))
                epic.setDuration(duration.get());
        }
    }

    private static boolean isOverlapped(Task taskForAddition, Task task) {
        Task task1stInTimeline = task;
        Task task2ndInTimeline = taskForAddition;

        if (taskForAddition.getStartTime().isBefore(task.getStartTime())) {
            task1stInTimeline = taskForAddition;
            task2ndInTimeline = task;
        }

        LocalDateTime start1 = task1stInTimeline.getStartTime();
        LocalDateTime end1 = task1stInTimeline.getEndTime();
        LocalDateTime start2 = task2ndInTimeline.getStartTime();
        LocalDateTime end2 = task2ndInTimeline.getEndTime();

        return start1.isBefore(start2) && end1.isAfter(start2) ||
                start1.isEqual(start2) && end1.isEqual(end2) ||
                start1.isBefore(end2) && (end1.isAfter(end2) || end1.isEqual(end2) || end1.isAfter(end2));
    }

    private void add(Task taskToAdd) {
        String pattern = "HH:mm";
        try {
            prioritizedTasks.stream()
                    .filter(taskInSet -> isOverlapped(taskToAdd, taskInSet))
                    .findFirst()
                    .ifPresentOrElse(
                            overlappedTask -> {
                                String message = "Новая задача с id '%d' title = '%s'\n".formatted(taskToAdd.getId(), taskToAdd.getTitle()) +
                                        "startTime : '%s'\n"
                                                .formatted(taskToAdd.getStartTime().format(DateTimeFormatter.ofPattern(pattern))) +
                                        "endTime : '%s'\n"
                                                .formatted(taskToAdd.getEndTime().format(DateTimeFormatter.ofPattern(pattern))) +
                                        "пересекается с существующей задачей с id '%d' title = '%s'\n"
                                                .formatted(overlappedTask.getId(), overlappedTask.getTitle()) +
                                        "startTime : '%s'\n"
                                                .formatted(overlappedTask.getStartTime().format(DateTimeFormatter.ofPattern(pattern))) +
                                        "endTime : '%s'\n"
                                                .formatted(overlappedTask.getEndTime().format(DateTimeFormatter.ofPattern(pattern)));

                                throw new TaskValidationException(message);
                            },
                            () -> prioritizedTasks.add(taskToAdd));
        } catch (TaskValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean checkPrioritizedTasksContainsId(int id) {
        return prioritizedTasks.stream().anyMatch(e -> e.getId().equals(id));
    }
}
