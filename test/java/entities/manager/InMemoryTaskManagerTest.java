package entities.manager;

import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Managers;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void checkManagerSavingEntities() {
        taskManager.addTask(new Task("task_title_1", "task_desc_1", Status.NEW));
        taskManager.addTask(new Task("task_title_2", "task_desc_2", Status.NEW));
        taskManager.addTask(new Task("task_title_3", "task_desc_3", Status.NEW));

        Assertions.assertEquals(3, taskManager.getAllTasks().size(), "Число задач не равно 3");
        Task task = taskManager.getAllTasks().get(1);
        Assertions.assertEquals(task, taskManager.getAllTasks().get(1), "Задачи не равны");

        taskManager.addEpic(new Epic("epic1", "epic1_desc"));
        taskManager.addEpic(new Epic("epic2", "epic2_desc"));

        Assertions.assertEquals(2, taskManager.getAllEpics().size(), "Число эпиков не равно 2");
        Epic epic = taskManager.getAllEpics().getFirst();
        Assertions.assertEquals(epic, taskManager.getAllEpics().getFirst(), "Эпики не равны");

        Subtask subtask1 = new Subtask("subt1", "desc1", Status.NEW, 4);
        Subtask subtask2 = new Subtask("subt2", "desc2", Status.NEW, 4);

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        Assertions.assertEquals(2, taskManager.getAllSubtasks().size(), "Число подзадач не равно 2");
        Subtask subtask = taskManager.getAllSubtasks().getFirst();
        Assertions.assertEquals(subtask, subtask1, "Подзадачи не равны");
    }

    @Test
    void checkManagerDontSaveEntitiesWithId() {
        Task task = new Task(1, "task_title_1", "task_desc_1", Status.NEW);
        Assertions.assertEquals(-1, taskManager.addTask(task),
                "Задача с id в параметрах была добавлена в менеджер");

        Epic epic = new Epic(1, "title", "desc");
        Assertions.assertEquals(-1, taskManager.addEpic(epic),
                "Эпик с id в параметрах был добавлен в менеджер");

        Subtask subtask = new Subtask(1, "title", "desc", Status.NEW, 1);
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
                taskManager.addSubtask(new Subtask(epicIndex, "", "", Status.NEW, epicIndex)),
                "id эпика был добавлен в коллекцию подзадач");
    }

    @Test
    void checkManagerSavingEntity() {
        Task task = new Task("title_1", "description_1", Status.NEW);
        taskManager.addTask(task);
        Task taskFromManager = taskManager.getAllTasks().getFirst();

        Assertions.assertEquals(task.getId(), taskFromManager.getId(), "id не равны");
        Assertions.assertEquals(task.getTitle(), taskFromManager.getTitle(), "заголовки не равны");
        Assertions.assertEquals(task.getDescription(), taskFromManager.getDescription(), "описания не равны");
        Assertions.assertEquals(task.getStatus(), taskFromManager.getStatus(), "статусы не равны");
    }

    @Test
    void updateTaskTest() {
        taskManager.addTask(new Task("title", "description", Status.NEW));
        Task taskUpd = new Task(1, "title_upd", "desc_upd", Status.IN_PROGRESS);
        Assertions.assertTrue(taskManager.updateTask(taskUpd), "Не удалось обновить задачу");

        Task taskUpdManager = taskManager.getTask(1);
        Assertions.assertEquals(taskUpd.getId(), taskUpdManager.getId(), "id не равны");
        Assertions.assertEquals(taskUpd.getTitle(), taskUpdManager.getTitle(), "заголовки не равны");
        Assertions.assertEquals(taskUpd.getDescription(), taskUpdManager.getDescription(), "описания не равны");
        Assertions.assertEquals(taskUpd.getStatus(), taskUpdManager.getStatus(), "статусы не равны");
    }

    @Test
    void deleteTaskTest() {
        Task task1 = new Task("t1", "t1", Status.NEW);
        Task task2 = new Task("t2", "t2", Status.IN_PROGRESS);
        Task task3 = new Task("t3", "t3", Status.DONE);
        int index1 = taskManager.addTask(task1);
        int index2 =  taskManager.addTask(task2);
        int index3 =  taskManager.addTask(task3);

        taskManager.deleteTaskById(index2);
        Assertions.assertEquals(2, taskManager.getAllTasks().size(), "Размер коллекции не равен 2");
        Assertions.assertNull(taskManager.getTask(index2), "Задача не была удалена");
        Assertions.assertNotNull(taskManager.getTask(index1), "Задача была удалена");
        Assertions.assertNotNull(taskManager.getTask(index3), "Задача была удалена");
    }
}