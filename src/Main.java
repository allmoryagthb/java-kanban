import entities.manager.Manager;
import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import enums.Status;

public class Main {

    public static void main(String[] args) {
        checkManager();
    }

    private static void checkManager() {
        Manager manager = new Manager();

        Task task1 = new Task("task1 title", "task1 description");
        Task task2 = new Task("task2 title", "task2 description");
        Task task3 = new Task("task3 title", "task3 description");

        Epic epic1 = new Epic("epic1 title", "epic1 description");
        Epic epic2 = new Epic("epic2 title", "epic2 description");

        Subtask subtask1 = new Subtask("subtask1 title", "subtitle1 description", epic1);
        Subtask subtask2 = new Subtask("subtask2 title", "subtitle2 description", epic2);
        Subtask subtask3 = new Subtask("subtask3 title", "subtitle3 description", epic2);

        manager.subtasks.addTask(subtask1);
        manager.subtasks.addTask(subtask2);
        manager.subtasks.addTask(subtask3);

        System.out.println("\n=====\n");
        manager.tasks.addTask(task1);
        manager.tasks.addTask(task2);
        manager.tasks.addTask(task2);
        manager.tasks.addTask(task3);
        manager.tasks.addTask(task3);
        manager.tasks.addTask(task3);
        manager.tasks.addTask(new Task(3, "task 3 new upd", "task3 description"));
        System.out.println(manager.tasks.getTaskList());

        System.out.println("\n=====\n");
        manager.tasks.addTask(task3);
        System.out.println(manager.tasks.getTaskList());

        System.out.println("\n=====\n");
        manager.epics.addTask(epic1);
        manager.epics.addTask(epic1);
        manager.epics.addTask(epic2);
        System.out.println(manager.epics.getTaskList());

        System.out.println("\n=====\n");
        new Subtask(6, "subtask6 title upd 1", "desc", epic1);
        System.out.println(manager.epics.getTaskList());

        System.out.println("\n=====\n");
        manager.subtasks.addTask(new Subtask(8, "subtask8 title upd 1", "desc", epic2));
        System.out.println(manager.epics.getTaskById(5));
        System.out.println(manager.epics.getSubtasks(5));
        manager.subtasks.addTask(new Subtask(8, "subtask8 title upd 1", "desc", epic2));
        System.out.println(manager.epics.getTaskById(5));

        manager.subtasks.addTask(new Subtask(7, "subtask7 title upd 1", "desc", epic2));
        System.out.println(manager.epics.getTaskById(5));
        manager.subtasks.addTask(new Subtask(7, "subtask7 title upd 1", "desc", epic2));
        System.out.println(manager.epics.getTaskById(5));

        System.out.println("\n=====\n");
        manager.subtasks.deleteAllTasks();
        System.out.println(manager.epics.getTaskList());

        System.out.println("\n========\n");
        subtask1 = new Subtask("subtask1 title", "subtitle1 description", epic1);
        subtask2 = new Subtask("subtask2 title", "subtitle2 description", epic2);
        subtask3 = new Subtask("subtask3 title", "subtitle3 description", epic2);
        manager.subtasks.addTask(subtask1);
        manager.subtasks.addTask(subtask2);
        manager.subtasks.addTask(subtask2);
        manager.subtasks.addTask(subtask3);
        System.out.println(manager.epics.getTaskList());

        System.out.println("\n========\n");
        manager.epics.deleteAllTasks();
        System.out.println(manager.epics.getTaskList());
        System.out.println(manager.subtasks.getTaskList());

        System.out.println("\n========\n");
        Epic epic3 = new Epic("epic3 title", "desc");
        manager.epics.addTask(epic3);
        Subtask subtask4 = new Subtask("sub4", "desc4", epic3);
        Subtask subtask5 = new Subtask("sub5", "desc5", epic3);
        manager.subtasks.addTask(subtask4);
        manager.subtasks.addTask(subtask5);
        System.out.println(manager.subtasks);
        System.out.println(manager.epics);

        System.out.println("\n========\n");
        manager.subtasks.changeTaskDescriptionById(13, "desc4 upd");
        System.out.println(manager.subtasks);
        System.out.println(manager.epics);

        System.out.println("\n========\n");
        manager.subtasks.changeTaskStatusById(13, Status.IN_PROGRESS);
        System.out.println(manager.subtasks);
        System.out.println(manager.epics);

        System.out.println("\n========\n");
        manager.subtasks.changeTaskStatusById(14, Status.DONE);
        System.out.println(manager.subtasks);
        System.out.println(manager.epics);

        System.out.println("\n========\n");
        manager.subtasks.changeTaskStatusById(14, Status.NEW);
        manager.subtasks.changeTaskStatusById(13, Status.DONE);
        System.out.println(manager.subtasks);
        System.out.println(manager.epics);

        System.out.println("\n========\n");
        manager.subtasks.deleteTaskById(14);
        System.out.println(manager.subtasks);
        System.out.println(manager.epics);

        System.out.println("\n========\n");
        manager.subtasks.deleteTaskById(14);
        System.out.println(manager.subtasks);
        System.out.println(manager.epics);

        System.out.println("\n========\n");
        manager.subtasks.deleteTaskById(13);
        System.out.println(manager.subtasks);
        System.out.println(manager.epics);
    }
}
