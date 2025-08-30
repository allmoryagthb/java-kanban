package entities.file;

import entities.manager.FileBackedTaskManager;
import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    public void before() throws IOException {
        this.file = File.createTempFile("temp", "csv");
        this.fileBackedTaskManager = new FileBackedTaskManager(file);
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

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void checkLoadingFromFile(boolean checkFurtherWork) {
        fileBackedTaskManager.addTask(new Task("task title1", "task desc", Status.NEW));
        fileBackedTaskManager.addTask(new Task("task title2", "task desc", Status.NEW));
        fileBackedTaskManager.addEpic(new Epic("epic title", "epic desc"));
        fileBackedTaskManager.addSubtask(new Subtask("subt title", "subt desc", Status.DONE, 3));
        fileBackedTaskManager.addTask(new Task("task title3", "task desc", Status.IN_PROGRESS));
        fileBackedTaskManager.getTask(2);
        fileBackedTaskManager.getTask(1);
        fileBackedTaskManager.getTask(5);

        FileBackedTaskManager fileBackedTaskManagerLoaded = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(fileBackedTaskManager.getAllTasks(), fileBackedTaskManagerLoaded.getAllTasks(),
                "Коллекции задач не совпадают");
        Assertions.assertEquals(fileBackedTaskManager.getAllEpics(), fileBackedTaskManagerLoaded.getAllEpics(),
                "Коллекции эпиков не совпадают");
        Assertions.assertEquals(fileBackedTaskManager.getAllEpics().stream().map(Epic::getSubtasksIds).toList(),
        fileBackedTaskManagerLoaded.getAllEpics().stream().map(Epic::getSubtasksIds).toList(),
                "Списки id подзадач эпиков у оригинального и восстановленного списков не совпадают");
        Assertions.assertEquals(fileBackedTaskManager.getAllSubtasks(), fileBackedTaskManagerLoaded.getAllSubtasks(),
                "Коллекции подзадач не совпадают");
        Assertions.assertEquals(fileBackedTaskManager.getHistory(), fileBackedTaskManagerLoaded.getHistory(),
                "Коллекции истории не совпадают");

        if (checkFurtherWork) {
            fileBackedTaskManagerLoaded.addTask(new Task("task title4", "task desc", Status.NEW));
            Assertions.assertTrue(fileBackedTaskManagerLoaded.getAllTasks().containsAll(fileBackedTaskManager.getAllTasks()),
                    "В новой коллекции отсутствует задача, присутствующая в старой коллекции");
            fileBackedTaskManagerLoaded.addEpic(new Epic("epic title", "epic desc"));
            Assertions.assertTrue(fileBackedTaskManagerLoaded.getAllEpics().containsAll(fileBackedTaskManager.getAllEpics()),
                    "В новой коллекции отсутствует эпик, присутствующий в старой коллекции");
            fileBackedTaskManagerLoaded.addSubtask(new Subtask("subt title", "subt desc", Status.IN_PROGRESS, 3));
            Assertions.assertTrue(fileBackedTaskManagerLoaded.getAllSubtasks().containsAll(fileBackedTaskManager.getAllSubtasks()),
                    "В новой коллекции отсутствует подзадача, присутствующая в старой коллекции");
        }
    }
}
