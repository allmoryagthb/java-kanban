package entities.manager;

import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import enums.TaskTypes;
import exceptions.ManagerSaveException;
import util.CSVTaskFormat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public int addTask(Task task) {
        int id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public boolean updateTask(Task task) {
        boolean res = super.updateTask(task);
        save();
        return res;
    }

    @Override
    public Task getTask(int id) {
        final Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public boolean deleteTaskById(int id) {
        boolean res = super.deleteTaskById(id);
        save();
        return res;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public int addEpic(Epic epic) {
        int id = super.addEpic(epic);
        save();
        return id;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        boolean res = super.updateEpic(epic);
        save();
        return res;
    }

    @Override
    public Epic getEpic(int id) {
        final Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public boolean deleteEpicById(int id) {
        boolean res = super.deleteEpicById(id);
        save();
        return res;
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int id = super.addSubtask(subtask);
        save();
        return id;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        boolean res = super.updateSubtask(subtask);
        save();
        return res;
    }

    @Override
    public Subtask getSubtask(int id) {
        final Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public boolean deleteSubtaskById(int id) {
        boolean res = super.deleteSubtaskById(id);
        save();
        return res;
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        final List<Map<Integer, ? extends Task>> listOfAllEntities = List.of(
                fileBackedTaskManager.tasks,
                fileBackedTaskManager.epics,
                fileBackedTaskManager.subtasks);

        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split(System.lineSeparator());

            int counter = 0;
            List<Integer> historyIds = new ArrayList<>();
            for (String line : lines) {
                if (line.isEmpty()) {
                    historyIds = CSVTaskFormat.getHistoryIdsListFromString(lines[++counter]);
                    break;
                }
                if (line.equals(CSVTaskFormat.getHeader())) {
                    counter++;
                    continue;
                }
                if (line.split(",")[1].equalsIgnoreCase(TaskTypes.TASK.getValue())) {
                    final Task task = CSVTaskFormat.getTaskFromString(line);
                    fileBackedTaskManager.tasks.put(task.getId(), task);
                } else if (line.split(",")[1].equalsIgnoreCase(TaskTypes.EPIC.getValue())) {
                    final Epic epic = CSVTaskFormat.getEpicFromString(line);
                    fileBackedTaskManager.epics.put(epic.getId(), epic);
                } else {
                    final Subtask subtask = CSVTaskFormat.getSubtaskFromString(line);
                    fileBackedTaskManager.epics.get(subtask.getEpicId()).addSubtask(subtask.getId());
                    fileBackedTaskManager.subtasks.put(subtask.getId(), subtask);
                }
                counter++;

                if (fileBackedTaskManager.getIdCounter() < counter)
                    fileBackedTaskManager.setIdCounter(counter);
            }

            historyIds.forEach(elementId -> listOfAllEntities.forEach(collection -> {
                if (collection.containsKey(elementId)) {
                    fileBackedTaskManager.historyManager.addTask(collection.get(elementId));
                }
            }));

        } catch (IOException e) {
            throw new ManagerSaveException("Can't read from file: " + file.getName(), e);
        }

        return fileBackedTaskManager;
    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(CSVTaskFormat.getHeader());
            writer.newLine();

            List.of(tasks, epics, subtasks).forEach(collection ->
                    collection.forEach((key, value) -> {
                        try {
                            writer.write(CSVTaskFormat.toString(value));
                            writer.newLine();
                        } catch (IOException e) {
                            throw new ManagerSaveException("Can't save to file: " + file.getName(), e);
                        }
                    }));
            writer.newLine();
            writer.write(CSVTaskFormat.toString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file: " + file.getName(), e);
        }
    }
}
