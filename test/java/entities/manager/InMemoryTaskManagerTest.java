package entities.manager;

import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import enums.Status;
import exceptions.TaskValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getInMemoryTaskManager();
    }

    @Test
    void checkManagerSavingEntities() {
        taskManager.addTask(new Task("task_title_1", "task_desc_1", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(10)));
        taskManager.addTask(new Task("task_title_2", "task_desc_2", Status.NEW,
                LocalDateTime.now().minusHours(1), Duration.ofMinutes(10)));
        taskManager.addTask(new Task("task_title_3", "task_desc_3", Status.NEW,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(10)));

        Assertions.assertEquals(3, taskManager.getAllTasks().size(), "Число задач не равно 3");
        Task task = taskManager.getAllTasks().get(1);
        Assertions.assertEquals(task, taskManager.getAllTasks().get(1), "Задачи не равны");

        taskManager.addEpic(new Epic("epic1", "epic1_desc"));
        taskManager.addEpic(new Epic("epic2", "epic2_desc"));

        Assertions.assertEquals(2, taskManager.getAllEpics().size(), "Число эпиков не равно 2");
        Epic epic = taskManager.getAllEpics().getFirst();
        Assertions.assertEquals(epic, taskManager.getAllEpics().getFirst(), "Эпики не равны");

        Subtask subtask1 = new Subtask("subt1", "desc1", Status.NEW, 4,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(10));
        Subtask subtask2 = new Subtask("subt2", "desc2", Status.NEW, 4,
                LocalDateTime.now().plusHours(3), Duration.ofMinutes(10));

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        Assertions.assertEquals(2, taskManager.getAllSubtasks().size(), "Число подзадач не равно 2");
        Subtask subtask = taskManager.getAllSubtasks().getFirst();
        Assertions.assertEquals(subtask, subtask1, "Подзадачи не равны");
    }

    @Test
    void checkManagerDontSaveEntitiesWithId() {
        Task task = new Task(1, "task_title_1", "task_desc_1", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        Assertions.assertEquals(-1, taskManager.addTask(task),
                "Задача с id в параметрах была добавлена в менеджер");

        Epic epic = new Epic(1, "title", "desc", null, null);
        Assertions.assertEquals(-1, taskManager.addEpic(epic),
                "Эпик с id в параметрах был добавлен в менеджер");

        Subtask subtask = new Subtask(1, "title", "desc", Status.NEW, 1, LocalDateTime.now(), Duration.ofMinutes(10));
        taskManager.addEpic(new Epic("", ""));
        Assertions.assertEquals(-1, taskManager.addSubtask(subtask),
                "Подзадача с id в параметрах была добавлена в менеджер");
    }

    @Test
    void checkAddingNullsInManager() {
        Assertions.assertEquals(-1, taskManager.addTask(null),
                "null задача была добавлена в менеджер");
        Assertions.assertEquals(-1, taskManager.addEpic(null),
                "null эпик был добавлен в менеджер");
        Assertions.assertEquals(-1, taskManager.addSubtask(null),
                "null подзадача была добавлена в менеджер");
    }

    @Test
    void checkThatSubtaskCantBeAddedAsEpic() {
        Epic epic = new Epic("", "");
        int epicIndex = taskManager.addEpic(epic);

        Assertions.assertEquals(-1,
                taskManager.addSubtask(new Subtask(epicIndex, "", "", Status.NEW, epicIndex, LocalDateTime.now(), Duration.ofMinutes(10))),
                "id эпика был добавлен в коллекцию подзадач");
    }

    @Test
    void checkManagerSavingEntity() {
        Task task = new Task("title_1", "description_1", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        taskManager.addTask(task);
        Task taskFromManager = taskManager.getAllTasks().getFirst();

        Assertions.assertEquals(task.getId(), taskFromManager.getId(), "id не равны");
        Assertions.assertEquals(task.getTitle(), taskFromManager.getTitle(), "заголовки не равны");
        Assertions.assertEquals(task.getDescription(), taskFromManager.getDescription(), "описания не равны");
        Assertions.assertEquals(task.getStatus(), taskFromManager.getStatus(), "статусы не равны");
    }

    @Test
    void updateTaskTest() {
        taskManager.addTask(new Task("title", "description", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10)));
        Task taskUpd = new Task(1, "title_upd", "desc_upd", Status.IN_PROGRESS, LocalDateTime.now(), Duration.ofMinutes(10));
        Assertions.assertTrue(taskManager.updateTask(taskUpd), "Не удалось обновить задачу");

        Task taskUpdManager = taskManager.getTask(1);
        Assertions.assertEquals(taskUpd.getId(), taskUpdManager.getId(), "id не равны");
        Assertions.assertEquals(taskUpd.getTitle(), taskUpdManager.getTitle(), "заголовки не равны");
        Assertions.assertEquals(taskUpd.getDescription(), taskUpdManager.getDescription(), "описания не равны");
        Assertions.assertEquals(taskUpd.getStatus(), taskUpdManager.getStatus(), "статусы не равны");
    }

    @Test
    void deleteTaskTest() {
        Task task1 = new Task("t1", "t1", Status.NEW, LocalDateTime.now().plusHours(3), Duration.ofMinutes(10));
        Task task2 = new Task("t2", "t2", Status.IN_PROGRESS, LocalDateTime.now(), Duration.ofMinutes(10));
        Task task3 = new Task("t3", "t3", Status.DONE, LocalDateTime.now().minusHours(2), Duration.ofMinutes(10));
        int index1 = taskManager.addTask(task1);
        int index2 = taskManager.addTask(task2);
        int index3 = taskManager.addTask(task3);

        taskManager.deleteTaskById(index2);
        Assertions.assertEquals(2, taskManager.getAllTasks().size(), "Размер коллекции не равен 2");
        Assertions.assertNull(taskManager.getTask(index2), "Задача не была удалена");
        Assertions.assertNotNull(taskManager.getTask(index1), "Задача была удалена");
        Assertions.assertNotNull(taskManager.getTask(index3), "Задача была удалена");
    }

    @Test
    void checkOverlappingException() {
        Task task1 = new Task("t1", "t1", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        Task task2 = new Task("t2", "t2", Status.IN_PROGRESS, LocalDateTime.now().minusMinutes(5), Duration.ofMinutes(10));
        taskManager.addTask(task1);
        Assertions.assertThrowsExactly(TaskValidationException.class, () -> taskManager.addTask(task2));
    }

    @Test
    @DisplayName("Проверить, что настройки времени эпика изменяются при добавлении подзадач")
    void checkEpicTimeSettings() {
        Epic epic = new Epic("epic1", "epic1_desc");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("subt1", "desc1", Status.NEW, 1,
                LocalDateTime.now().minusHours(1), Duration.ofMinutes(10));
        Subtask subtask2 = new Subtask("subt2", "desc2", Status.NEW, 1,
                LocalDateTime.now(), Duration.ofMinutes(10));
        Subtask subtask3 = new Subtask("subt3", "desc3", Status.NEW, 1,
                LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(10));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        Assertions.assertEquals(subtask1.getStartTime(), epic.getStartTime(),
                "Начальное время Эпика не совпадает с ожидаемым");
        Assertions.assertEquals(subtask3.getEndTime(), epic.getEndTime(),
                "Конечное время Эпика не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Проверить, что настройки времени эпика изменяются после изменений настроек времени подзадач")
    void checkEpicTimeSettingsAfterChangingSubtaskTimeSettings() {
        Epic epic = new Epic("epic1", "epic1_desc");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("subt1", "desc1", Status.NEW, 1,
                LocalDateTime.now().minusHours(1), Duration.ofMinutes(10));
        Subtask subtask2 = new Subtask("subt2", "desc2", Status.NEW, 1,
                LocalDateTime.now(), Duration.ofMinutes(10));
        Subtask subtask3 = new Subtask("subt3", "desc3", Status.NEW, 1,
                LocalDateTime.now().plusMinutes(50), Duration.ofMinutes(10));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        Subtask tempSubtask = taskManager.getSubtask(2);
        tempSubtask.setStartTime(LocalDateTime.now().plusHours(3));
        taskManager.updateSubtask(tempSubtask);

        Assertions.assertEquals(taskManager.getPrioritizedTasks().first().getStartTime(), epic.getStartTime(),
                "Начальное время Эпика не совпадает с ожидаемым");
        Assertions.assertEquals(taskManager.getPrioritizedTasks().last().getEndTime(), epic.getEndTime(),
                "Конечное время Эпика не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Проверить, что коллекция приоритетных задач выставляет элементы по приоритету времени")
    void checkPrioritizedTasksIsWorkingCorrect() {
        Epic epic = new Epic("epic1", "epic1_desc");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("subt1", "desc1", Status.NEW, 1,
                LocalDateTime.now().plusHours(5), Duration.ofMinutes(10));
        taskManager.addSubtask(subtask1);

        Task task1 = new Task("t1", "t1", Status.NEW, LocalDateTime.now().minusDays(1), Duration.ofMinutes(10));
        Task task2 = new Task("t1", "t1", Status.NEW, LocalDateTime.now().plusHours(2), Duration.ofMinutes(10));
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        List<Task> prios = new ArrayList<>(taskManager.getPrioritizedTasks());
        Assertions.assertEquals(task1, prios.getFirst());
        Assertions.assertEquals(subtask1, prios.getLast());
        Assertions.assertEquals(task2, prios.get(1));
    }

    @Test
    @DisplayName("Проверить, что коллекция приоритетных задач обновляется после обновления задачи")
    void checkPrioritizedTasksIsWorkingCorrectAfterTaskUpdate() {
        Task task1 = new Task("t1", "t1", Status.NEW, LocalDateTime.now().minusDays(1), Duration.ofMinutes(10));
        Task task2 = new Task("t1", "t1", Status.NEW, LocalDateTime.now().plusHours(2), Duration.ofMinutes(10));
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.updateTask(new Task(1, "t1 upd", "t1 upd desc", Status.IN_PROGRESS,
                LocalDateTime.now().plusDays(10), Duration.ofMinutes(5)));

        Assertions.assertEquals(task2.getId(), taskManager.prioritizedTasks.getFirst().getId());
        Assertions.assertEquals(task1.getId(), taskManager.prioritizedTasks.getLast().getId());
    }

    @Test
    @DisplayName("Проверить, что коллекция приоритетных задач обновляется после удаления задачи")
    void checkPrioritizedTasksIsWorkingCorrectAfterOneTaskDeletion() {
        Task task1 = new Task("t1", "t1", Status.NEW, LocalDateTime.now().minusDays(1), Duration.ofMinutes(10));
        Task task2 = new Task("t1", "t1", Status.NEW, LocalDateTime.now().plusHours(2), Duration.ofMinutes(10));
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.deleteTaskById(2);

        Assertions.assertEquals(1, taskManager.prioritizedTasks.size());
        Assertions.assertEquals(task1.getId(), taskManager.prioritizedTasks.getFirst().getId());
    }

    @Test
    @DisplayName("Проверить, что коллекция приоритетных задач обновляется после удаления всех задач")
    void checkPrioritizedTasksIsWorkingCorrectAfterAllTaskDeletion() {
        Task task1 = new Task("t1", "t1", Status.NEW, LocalDateTime.now().minusDays(1), Duration.ofMinutes(10));
        Task task2 = new Task("t1", "t1", Status.NEW, LocalDateTime.now().plusHours(2), Duration.ofMinutes(10));
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.deleteAllTasks();

        Assertions.assertTrue(taskManager.prioritizedTasks.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"id", "all"})
    @DisplayName("Проверить, что коллекция приоритетных задач обновляется после удаления эпика")
    void checkPrioritizedTasksIsWorkingCorrectAfterEpicDeletion(String option) {
        Epic epic = new Epic("epic1", "epic1_desc");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("subt1", "desc1", Status.NEW, 1,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(10));
        Subtask subtask2 = new Subtask("subt2", "desc2", Status.NEW, 1,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(10));
        Subtask subtask3 = new Subtask("subt3", "desc3", Status.NEW, 1,
                LocalDateTime.now().plusHours(3), Duration.ofMinutes(10));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        if (option.equalsIgnoreCase("id"))
            taskManager.deleteEpicById(1);
        if (option.equalsIgnoreCase("all"))
            taskManager.deleteAllEpics();

        Assertions.assertTrue(taskManager.prioritizedTasks.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"id", "all"})
    @DisplayName("Проверить, что коллекция приоритетных задач обновляется после удаления подзадачи")
    void checkPrioritizedTasksIsWorkingCorrectAfterSubtaskDeletion(String option) {
        Epic epic = new Epic("epic1", "epic1_desc");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("subt1", "desc1", Status.NEW, 1,
                LocalDateTime.now().plusHours(3), Duration.ofMinutes(10));
        Subtask subtask2 = new Subtask("subt2", "desc2", Status.NEW, 1,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(10));
        Subtask subtask3 = new Subtask("subt3", "desc3", Status.NEW, 1,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(10));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        if (option.equalsIgnoreCase("id")) {
            taskManager.deleteSubtaskById(2);
            Assertions.assertEquals(2, taskManager.getPrioritizedTasks().size());
            Assertions.assertEquals(subtask3, taskManager.getPrioritizedTasks().getFirst());
            Assertions.assertEquals(subtask2, taskManager.getPrioritizedTasks().getLast());
        }
        if (option.equalsIgnoreCase("all")) {
            taskManager.deleteAllSubtasks();
            Assertions.assertTrue(taskManager.prioritizedTasks.isEmpty());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"null-null", "start-null", "null-end"})
    @DisplayName("Проверить заполнения поле Эпика при наличии null полей у подзадач")
    void checkEpicUpdateWithSubtasksTimeNullFields(String option) {
        Epic epic = new Epic("epic1", "epic1_desc");
        taskManager.addEpic(epic);

        if (option.equalsIgnoreCase("null-null")) {
            Subtask subtask1 = new Subtask("subt1", "desc1", Status.NEW, 1,
                    null, null);
            taskManager.addSubtask(subtask1);

            Assertions.assertNull(epic.getStartTime());
            Assertions.assertNull(epic.getEndTime());
            Assertions.assertNull(epic.getDuration());
            Assertions.assertEquals(1, taskManager.getAllEpics().size());
            Assertions.assertEquals(1, taskManager.getAllSubtasks().size());
            Assertions.assertTrue(taskManager.prioritizedTasks.isEmpty());
        }

        if (option.equalsIgnoreCase("start-null")) {
            Subtask subtask1 = new Subtask("subt1", "desc1", Status.NEW, 1,
                    LocalDateTime.now(), null);
            taskManager.addSubtask(subtask1);

            Assertions.assertNotNull(epic.getStartTime());
            Assertions.assertNull(epic.getEndTime());
            Assertions.assertNull(epic.getDuration());
            Assertions.assertEquals(1, taskManager.getAllEpics().size());
            Assertions.assertEquals(1, taskManager.getAllSubtasks().size());
            Assertions.assertEquals(1, taskManager.prioritizedTasks.size());
        }

        if (option.equalsIgnoreCase("null-end")) {
            Subtask subtask1 = new Subtask("subt1", "desc1", Status.NEW, 1,
                    null, Duration.ofMinutes(5));
            taskManager.addSubtask(subtask1);

            Assertions.assertNull(epic.getStartTime());
            Assertions.assertNull(epic.getEndTime());
            Assertions.assertNotNull(epic.getDuration());
            Assertions.assertEquals(1, taskManager.getAllEpics().size());
            Assertions.assertEquals(1, taskManager.getAllSubtasks().size());
            Assertions.assertTrue(taskManager.prioritizedTasks.isEmpty());
        }
    }
}