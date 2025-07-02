package entities.manager;

public class Manager {
    public final TaskManager tasks = new TaskManager();
    public final SubtaskManager subtasks = new SubtaskManager();
    public final EpicManager epics = new EpicManager();
}
