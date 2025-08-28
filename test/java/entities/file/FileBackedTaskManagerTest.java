package entities.file;

import entities.manager.FileBackedTaskManager;
import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import enums.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.CSVTaskFormat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

public class FileBackedTaskManagerTest {
    private File file;
    private FileBackedTaskManager fileBackedTaskManager;
    private static final Logger logger = Logger.getLogger(String.valueOf(FileBackedTaskManager.class));

    @BeforeEach
    public void before() {
        this.file = new File("test.csv");
        this.fileBackedTaskManager = new FileBackedTaskManager(file);
    }

    @AfterEach
    public void after() {
        if (file.exists())
            logger.info("Удаление файла %s после теста: %b".formatted(file.getName(), file.delete()));
    }

    @Test
    public void checkThatFileCreatedByAddingTask() throws IOException {
        Task task = new Task("task title1", "task desc", Status.NEW);
        fileBackedTaskManager.addTask(task);
        final String csv = Files.readString(file.toPath());
        String[] lines = csv.split(System.lineSeparator());
        Assertions.assertEquals(CSVTaskFormat.getHeader(), lines[0],
                "Первая строка не соответствует хэдэру");
        Assertions.assertEquals(CSVTaskFormat.toString(task), lines[1],
                "Вторая строка не соответствует ожидаемому формату");
        Assertions.assertEquals(2, lines.length,
                "Количество строк не соответствует ожидаемому числу");
        Assertions.assertTrue(file.exists());
    }

    @Test
    public void checkThatHistoryAddedToFile() throws IOException {
        Task task = new Task("task title1", "task desc", Status.NEW);
        fileBackedTaskManager.addTask(task);
        fileBackedTaskManager.getTask(1);
        final String csv = Files.readString(file.toPath());
        String[] lines = csv.split(System.lineSeparator());
        Assertions.assertEquals("", lines[2], "Третья строка должна быть пустой");
        Assertions.assertEquals(task.getId(), Integer.valueOf(lines[3]), "Четвертая строка не соответствует ожидаемой");
        Assertions.assertEquals(4, lines.length,
                "Количество строк не соответствует ожидаемому числу");
        Assertions.assertTrue(file.exists());
    }

    @Test
    public void checkLoadingFromFile() {
        fileBackedTaskManager.addTask(new Task("task title1", "task desc", Status.NEW));
        fileBackedTaskManager.addTask(new Task("task title2", "task desc", Status.NEW));
        fileBackedTaskManager.addEpic(new Epic("epic title", "epic desc"));
        fileBackedTaskManager.addSubtask(new Subtask("subt title", "subt desc", Status.NEW, 3));
        fileBackedTaskManager.addTask(new Task("task title3", "task desc", Status.NEW));
        fileBackedTaskManager.getTask(2);
        fileBackedTaskManager.getTask(1);
        fileBackedTaskManager.getTask(5);

        FileBackedTaskManager fileBackedTaskManagerLoaded = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(fileBackedTaskManager.getAllTasks(), fileBackedTaskManagerLoaded.getAllTasks(),
                "Коллекции задач не совпадают");
        Assertions.assertEquals(fileBackedTaskManager.getAllEpics(), fileBackedTaskManagerLoaded.getAllEpics(),
                "Коллекции эпиков не совпадают");
        Assertions.assertEquals(fileBackedTaskManager.getAllSubtasks(), fileBackedTaskManagerLoaded.getAllSubtasks(),
                "Коллекции подзадач не совпадают");
        Assertions.assertEquals(fileBackedTaskManager.getHistory(), fileBackedTaskManagerLoaded.getHistory(),
                "Коллекции истории не совпадают");
    }
}
