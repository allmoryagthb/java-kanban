import entities.manager.InMemoryTaskManager;
import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;

import static enums.Status.*;

public class Main {

    public static void main(String[] args) {
        checkManager();
    }

    private static void checkManager() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        System.out.println("\n========================\n");
        inMemoryTaskManager.addNewTask(new Task("task1 title", "task1 description", NEW));
        inMemoryTaskManager.addNewTask(new Task(2, "task2 id title", "task2 description", IN_PROGRESS));
        inMemoryTaskManager.addNewTask(new Task("task3 title", "task3 description", NEW));
        inMemoryTaskManager.addNewTask(new Task(4, "task4 id title", "task4 description", NEW));
        inMemoryTaskManager.addNewTask(new Task(5, "task5 id title", "task5 description", NEW));
        inMemoryTaskManager.addNewTask(new Task("task6 title", "task6 description", NEW));
        inMemoryTaskManager.addNewTask(new Task(1, "some task id title", "task description", NEW));
        System.out.println(inMemoryTaskManager.getTaskById(123));
        System.out.println(inMemoryTaskManager.updateTask(new Task("", "", NEW)));
        inMemoryTaskManager.updateTask(new Task(4, "task4 upd", "task4 upd description", DONE));
        inMemoryTaskManager.updateTask(new Task("task123 upd", "task123 upd description", DONE));

        System.out.println(inMemoryTaskManager.getTaskById(2));

        System.out.println(inMemoryTaskManager.getAllTasks());

        System.out.println(inMemoryTaskManager.deleteTaskById(3));
        System.out.println(inMemoryTaskManager.deleteTaskById(123));

        inMemoryTaskManager.deleteAllTasks();

        inMemoryTaskManager.addNewTask(new Task("task7", "", IN_PROGRESS));

        System.out.println("\n========================\n");
        inMemoryTaskManager.addNewEpic(new Epic("epic1", "epic1 description"));
        inMemoryTaskManager.addNewEpic(new Epic(2, "epic2", "epic2 description"));
        inMemoryTaskManager.addNewEpic(new Epic(3, "epic3", "epic3 description"));
        inMemoryTaskManager.addNewEpic(new Epic("epic4", "epic4 description"));

        inMemoryTaskManager.updateEpic(new Epic("", ""));
        inMemoryTaskManager.updateEpic(new Epic(2, "epic2 upd", "epic2 description upd"));

        System.out.println(inMemoryTaskManager.getEpicSubtasks(123));
        System.out.println(inMemoryTaskManager.getEpicSubtasks(1));

        System.out.println(inMemoryTaskManager.deleteEpicById(123));
        System.out.println(inMemoryTaskManager.deleteEpicById(3));

        inMemoryTaskManager.deleteAllEpics();

        System.out.println("\n========================\n");
        inMemoryTaskManager.addNewEpic(new Epic("epic1", "epic1 description"));
        inMemoryTaskManager.addNewEpic(new Epic("epic2", "epic2 description"));

        inMemoryTaskManager.addNewSubtask(new Subtask("sub1", "sub1", NEW, 13));
        inMemoryTaskManager.addNewSubtask(new Subtask("sub2", "sub2", IN_PROGRESS, 13));
        inMemoryTaskManager.addNewSubtask(new Subtask("sub3", "sub3", DONE, 13));
        inMemoryTaskManager.addNewSubtask(new Subtask(3, "sub3", "sub3", DONE, 14));


        inMemoryTaskManager.updateSubtask(new Subtask("", "", DONE, 6));
        inMemoryTaskManager.updateSubtask(new Subtask(2, "sub2 upd", "sub2 upd", DONE, 6));
        inMemoryTaskManager.updateSubtask(new Subtask(2, "sub2 upd", "sub2 upd", DONE, 5));
        inMemoryTaskManager.updateSubtask(new Subtask(123, "sub2 upd", "sub2 upd", DONE, 5));

        System.out.println(inMemoryTaskManager.getAllSubtasks());
        System.out.println(inMemoryTaskManager.getSubtaskById(123));
        System.out.println(inMemoryTaskManager.getSubtaskById(7));
        System.out.println(inMemoryTaskManager.deleteSubtaskById(9));

        System.out.println(inMemoryTaskManager.getEpicById(123));
        System.out.println(inMemoryTaskManager.getEpicById(5));

        System.out.println(inMemoryTaskManager.getEpicSubtasks(5));
        inMemoryTaskManager.deleteAllSubtasks();
    }
}
