package entities.manager;

import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;

import java.util.List;

public interface TaskManager {
    int addTask(Task task);

    boolean updateTask(Task task);

    Task getTask(int id);

    List<Task> getAllTasks();

    boolean deleteTaskById(int id);

    void deleteAllTasks();

    int addEpic(Epic epic);

    boolean updateEpic(Epic epic);

    List<Epic> getAllEpics();

    Epic getEpic(int id);

    List<Subtask> getEpicSubtasks(int id);

    boolean deleteEpicById(int id);

    void deleteAllEpics();

    int addSubtask(Subtask subtask);

    boolean updateSubtask(Subtask subtask);

    List<Subtask> getAllSubtasks();

    Subtask getSubtask(int id);

    boolean deleteSubtaskById(int id);

    void deleteAllSubtasks();

    List<Task> getHistory();
}
