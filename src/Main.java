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

        manager.tasks.addTask(new Task("Task 1", "Description for tasks 1"));
        manager.tasks.addTask(new Task("Task 2", "Description for tasks 2"));
        manager.tasks.addTask(new Task("Task 3", "Description for tasks 3"));
        manager.tasks.addTask(new Task("Task 4", "Description for tasks 4"));
        manager.tasks.addTask(new Task("Task 5", "Description for tasks 5"));

        manager.tasks.getTaskById(1);
        manager.tasks.getTaskById(2);
        manager.tasks.getTaskById(3);
        manager.tasks.getTaskById(4);
        manager.tasks.getTaskById(5);
        manager.tasks.getTaskById(6);
        manager.tasks.getTaskList().forEach(System.out::println);

        System.out.println("\n-----\n");

        manager.tasks.deleteTaskById(3);
        manager.tasks.getTaskById(3);
        manager.tasks.getTaskList().forEach(System.out::println);

        System.out.println("\n-----\n");

        manager.tasks.changeTaskDescriptionById(2, "Task 2 edited description");
        System.out.println(manager.tasks.getTaskById(2));

        System.out.println("\n-----\n");

        manager.tasks.changeTaskStatusById(4, Status.IN_PROGRESS);
        System.out.println(manager.tasks.getTaskById(4));
        manager.tasks.getTaskList().forEach(System.out::println);

        System.out.println("\n-----\n");

        manager.tasks.addTask(new Task(2, "Task 2 new instance upd1",
                "Task 2 new instance description upd1"));
        System.out.println(manager.tasks.getTaskById(2));

        manager.tasks.addTask(new Task(2, "Task 2 new instance upd2",
                "Task 2 new instance description upd2"));
        System.out.println(manager.tasks.getTaskById(2));

        System.out.println("\n-----\n");

        Epic epic1 = new Epic("Epic 1 Title", "Epic 1 description");
        manager.epics.addTask(epic1);
        System.out.println(manager.epics.getTaskById(6));

        Subtask subtask1 = new Subtask("Subtask 1 title", "Subtask 1 description", epic1);
        Subtask subtask2 = new Subtask("Subtask 1 title", "Subtask 1 description", epic1);

        manager.subtasks.addTask(subtask1);
        manager.subtasks.addTask(subtask2);
        System.out.println(manager.subtasks.getTaskList());

        System.out.println("\n-----\n");

        manager.epics.changeTaskStatusById(6, Status.IN_PROGRESS);

        System.out.println("\n-----\n");
    }
}
