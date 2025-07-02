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
        Manager taskManager = new Manager();

        taskManager.addTask(new Task("Task 1", "Description for tasks 1"));
        taskManager.addTask(new Task("Task 2", "Description for tasks 2"));
        taskManager.addTask(new Task("Task 3", "Description for tasks 3"));
        taskManager.addTask(new Task("Task 4", "Description for tasks 4"));
        taskManager.addTask(new Task("Task 5", "Description for tasks 5"));

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.getTaskById(4);
        taskManager.getTaskById(5);
        taskManager.getTaskById(6);
        taskManager.getTaskList().forEach(System.out::println);

        System.out.println("/n-----");
        taskManager.deleteTaskById(3);
        taskManager.getTaskById(3);
        taskManager.getTaskList().forEach(System.out::println);

        System.out.println("/n-----");
        taskManager.changeTaskDescriptionById(2, "Task 2 edited description");
        System.out.println(taskManager.getTaskById(2));

        System.out.println("/n-----");
        taskManager.changeTaskStatusById(4, Status.IN_PROGRESS);
        System.out.println(taskManager.getTaskById(4));
        taskManager.getTaskList().forEach(System.out::println);

        System.out.println("/n-----");
        taskManager.addTask(new Task(2, "Task 2 new instance", "Task 2 new instance description"));
        taskManager.getTaskList().forEach(System.out::println);

        System.out.println("/n-----");
        taskManager.addEpic(new Epic("Epic 1", "Epic 1 description"));
        taskManager.getEpicList().forEach(System.out::println);
        Epic epic = new Epic(6, "Epic 1 edited", "Epic 1 edited description");
        taskManager.addEpic(epic);
        taskManager.getEpicList().forEach(System.out::println);

        System.out.println("/n-----");
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", epic);
    }
}
