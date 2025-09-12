package entities.file;

import entities.manager.FileBackedTaskManager;
import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import util.CSVTaskFormat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.logging.Logger;

public class FileBackedTaskManagerTest {
    private File file;
    private FileBackedTaskManager fileBackedTaskManager;
    private static final Logger logger = Logger.getLogger(String.valueOf(FileBackedTaskManager.class));

    @BeforeEach
    public void before() throws IOException {
        //this.file = File.createTempFile("temp", "csv");
        file = new File("temp.csv");
        this.fileBackedTaskManager = new FileBackedTaskManager(file);
    }

    @Test
    public void checkThatFileCreatedByAddingTask() throws IOException {
        Task task = new Task("task title1", "task desc", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
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
        Task task = new Task("task title1", "task desc", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
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
        fileBackedTaskManager.addTask(new Task("task title1", "task desc", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(5)));
        fileBackedTaskManager.addTask(new Task("task title2", "task desc", Status.NEW,
                LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5)));
        fileBackedTaskManager.addEpic(new Epic("epic title", "epic desc"));
        fileBackedTaskManager.addSubtask(new Subtask("subt title", "subt desc", Status.DONE, 3,
                LocalDateTime.now().minusHours(1), Duration.ofMinutes(5)));
        fileBackedTaskManager.addTask(new Task("task title3", "task desc", Status.IN_PROGRESS,
                LocalDateTime.now().minusHours(2), Duration.ofMinutes(5)));
        fileBackedTaskManager.getTask(2);
        fileBackedTaskManager.getTask(1);
        fileBackedTaskManager.getTask(5);
        Set<Task> prioritySetOrigin = fileBackedTaskManager.getPrioritizedTasks();

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
        Assertions.assertEquals(prioritySetOrigin, fileBackedTaskManagerLoaded.getPrioritizedTasks());

        if (checkFurtherWork) {
            fileBackedTaskManagerLoaded.addTask(new Task("task title4", "task desc", Status.NEW,
                    LocalDateTime.now().plusHours(2), Duration.ofMinutes(5)));
            Assertions.assertTrue(fileBackedTaskManagerLoaded.getAllTasks().containsAll(fileBackedTaskManager.getAllTasks()),
                    "В новой коллекции отсутствует задача, присутствующая в старой коллекции");
            fileBackedTaskManagerLoaded.addEpic(new Epic("epic title", "epic desc"));
            Assertions.assertTrue(fileBackedTaskManagerLoaded.getAllEpics().containsAll(fileBackedTaskManager.getAllEpics()),
                    "В новой коллекции отсутствует эпик, присутствующий в старой коллекции");
            fileBackedTaskManagerLoaded.addSubtask(new Subtask("subt title", "subt desc",
                    Status.IN_PROGRESS, 3, LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(10)));
            Assertions.assertTrue(fileBackedTaskManagerLoaded.getAllSubtasks().containsAll(fileBackedTaskManager.getAllSubtasks()),
                    "В новой коллекции отсутствует подзадача, присутствующая в старой коллекции");
        }
    }

    @Test
    @DisplayName("Проверить, что настройки времени сущностей сохраняются после загрузки из файла")
    void checkLoadingFromFileTimeSettings() {
        Task task = new Task("task title1", "task desc", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(15));
        LocalDateTime taskStart = task.getStartTime();
        Duration taskDuration = task.getDuration();
        LocalDateTime taskEnd = task.getEndTime();

        fileBackedTaskManager.addTask(task);

        Epic epic = new Epic("epic title", "epic desc");
        fileBackedTaskManager.addEpic(epic);
        Subtask subtask = new Subtask("subt title", "subt desc", Status.DONE, 2,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(50));
        fileBackedTaskManager.addSubtask(subtask);

        LocalDateTime epicStart = epic.getStartTime();
        Duration epicDuration = epic.getDuration();
        LocalDateTime epicEnd = epic.getEndTime();

        LocalDateTime subtaskStart = subtask.getStartTime();
        Duration subtaskDuration = subtask.getDuration();
        LocalDateTime subtaskEnd = subtask.getEndTime();

        FileBackedTaskManager fileBackedTaskManagerLoaded = FileBackedTaskManager.loadFromFile(file);
        Task testTask = fileBackedTaskManagerLoaded.getAllTasks().getFirst();
        Epic testEpic = fileBackedTaskManagerLoaded.getAllEpics().getFirst();
        Subtask testSubtask = fileBackedTaskManagerLoaded.getAllSubtasks().getFirst();

        Assertions.assertEquals(taskStart.withNano(0), testTask.getStartTime().withNano(0));
        Assertions.assertEquals(taskDuration, testTask.getDuration());
        Assertions.assertEquals(taskEnd.withNano(0), testTask.getEndTime().withNano(0));


        Assertions.assertEquals(epicStart.withNano(0), testEpic.getStartTime().withNano(0));
        Assertions.assertEquals(epicDuration, testEpic.getDuration());
        Assertions.assertEquals(epicEnd.withNano(0), testEpic.getEndTime().withNano(0));


        Assertions.assertEquals(subtaskStart.withNano(0), testSubtask.getStartTime().withNano(0));
        Assertions.assertEquals(subtaskDuration, testSubtask.getDuration());
        Assertions.assertEquals(subtaskEnd.withNano(0), testSubtask.getEndTime().withNano(0));
    }
}
