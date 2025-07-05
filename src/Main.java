import entities.manager.Manager;
import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;

import static enums.Status.*;

public class Main {

    public static void main(String[] args) {
        checkManager();
    }

    private static void checkManager() {
        Manager manager = new Manager();

        System.out.println("\n========================\n");
        manager.addNewTask(new Task("task1 title", "task1 description", NEW));
        manager.addNewTask(new Task(2, "task2 id title", "task2 description", IN_PROGRESS));
        manager.addNewTask(new Task("task3 title", "task3 description", NEW));
        manager.addNewTask(new Task(4, "task4 id title", "task4 description", NEW));
        manager.addNewTask(new Task(5, "task5 id title", "task5 description", NEW));
        manager.addNewTask(new Task("task6 title", "task6 description", NEW));

        manager.updateTask(new Task("", "", NEW));
        manager.updateTask(new Task(4, "task4 upd", "task4 upd description", DONE));

        System.out.println(manager.getTaskById(123));
        System.out.println(manager.getTaskById(2));

        System.out.println(manager.getAllTasks());

        System.out.println(manager.deleteTaskById(3));

        manager.deleteAllTasks();

        manager.addNewTask(new Task("task7", "", IN_PROGRESS));

        System.out.println("\n========================\n");
        manager.addNewEpic(new Epic("epic1", "epic1 description"));
        manager.addNewEpic(new Epic(2, "epic2", "epic2 description"));
        manager.addNewEpic(new Epic(3, "epic3", "epic3 description"));
        manager.addNewEpic(new Epic("epic4", "epic4 description"));

        manager.updateEpic(new Epic("", ""));
        manager.updateEpic(new Epic(2, "epic2 upd", "epic2 description upd"));

        System.out.println(manager.getEpicSubtasks(123));
        System.out.println(manager.getEpicSubtasks(1));

        System.out.println(manager.deleteEpicById(123));
        System.out.println(manager.deleteEpicById(3));

        manager.deleteAllEpics();

        System.out.println("\n========================\n");
        manager.addNewEpic(new Epic("epic1", "epic1 description"));
        manager.addNewEpic(new Epic("epic2", "epic2 description"));

        manager.addNewSubtask(new Subtask("sub1", "sub1", NEW, 5));
        manager.addNewSubtask(new Subtask("sub2", "sub2", IN_PROGRESS, 5));
        manager.addNewSubtask(new Subtask("sub3", "sub3", DONE, 5));
        manager.addNewSubtask(new Subtask(3, "sub3", "sub3", DONE, 5));
        manager.addNewSubtask(new Subtask(4, "sub4", "sub4", NEW, 5));
        manager.addNewSubtask(new Subtask(4, "sub4", "sub4", IN_PROGRESS, 5));
        manager.addNewSubtask(new Subtask(5, "sub5", "sub5", IN_PROGRESS, 5));
        manager.addNewSubtask(new Subtask("sub6", "sub6", DONE, 5));

        manager.addNewSubtask(new Subtask("sub7", "sub7", DONE, 6));
        manager.addNewSubtask(new Subtask("sub8", "sub8", DONE, 6));
        manager.addNewSubtask(new Subtask("sub9", "sub9", IN_PROGRESS, 6));

        manager.updateSubtask(new Subtask("", "", DONE, 6));
        manager.updateSubtask(new Subtask(2, "sub2 upd", "sub2 upd", DONE, 6));
        manager.updateSubtask(new Subtask(2, "sub2 upd", "sub2 upd", DONE, 5));
        manager.updateSubtask(new Subtask(123, "sub2 upd", "sub2 upd", DONE, 5));

        System.out.println(manager.getAllSubtasks());
        System.out.println(manager.getSubtaskById(123));
        System.out.println(manager.getSubtaskById(7));
        System.out.println(manager.deleteSubtask(9));

        System.out.println(manager.getEpicById(123));
        System.out.println(manager.getEpicById(5));

        System.out.println(manager.getEpicSubtasks(5));
        manager.deleteAllSubtasks();
    }
}
