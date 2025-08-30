import entities.manager.FileBackedTaskManager;
import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import enums.Status;
import util.Managers;

public class Main {

    public static void main(String[] args) {
        checkManager();
    }

    private static void checkManager() {
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefault();
        fileBackedTaskManager.addTask(new Task("task title1", "task desc", Status.NEW));
        fileBackedTaskManager.addTask(new Task("task title2", "task desc", Status.NEW));
        fileBackedTaskManager.addEpic(new Epic("epic title", "epic desc"));
        fileBackedTaskManager.addSubtask(new Subtask("subt title", "subt desc", Status.NEW, 3));
        fileBackedTaskManager.addTask(new Task("task title3", "task desc", Status.NEW));
        fileBackedTaskManager.getTask(2);
        fileBackedTaskManager.getTask(1);
        fileBackedTaskManager.getTask(5);

      //  FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(new File("vault.csv"));
    }
}
