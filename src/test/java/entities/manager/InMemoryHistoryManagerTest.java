package entities.manager;

import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Managers;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void checkHistoryManagerSavingEntity() {
        Task task1 = new Task(1, "Aaa", "Aaa", Status.NEW);
        Epic epic1 = new Epic(2, "Bbb", "Bbb");
        Subtask subtask1 = new Subtask(3, "Ccc", "Ccc", Status.NEW, 1);

        historyManager.addTask(task1);
        Assertions.assertEquals(1, historyManager.getHistory().size());
        Assertions.assertEquals(task1.getId(), historyManager.getHistory().getFirst().getId());

        historyManager.addTask(epic1);
        Assertions.assertEquals(2, historyManager.getHistory().size());
        Assertions.assertEquals(epic1.getId(), historyManager.getHistory().get(1).getId());

        historyManager.addTask(subtask1);
        Assertions.assertEquals(3, historyManager.getHistory().size());
        Assertions.assertEquals(subtask1.getId(), historyManager.getHistory().get(2).getId());
    }

    @Test
    void checkHistoryManagerSave10EntitiesMax() {
        for (int i = 1; i <= 100; i++) {
            Task task = new Task(i, "aaa", "aaa", Status.NEW);
            historyManager.addTask(task);
            if (i < 10) {
                Assertions.assertEquals(i, historyManager.getHistory().size());
                Assertions.assertEquals(1, historyManager.getHistory().getFirst().getId());
            } else {
                Assertions.assertEquals(10, historyManager.getHistory().size());
                Assertions.assertEquals(i - 9, historyManager.getHistory().getFirst().getId());
            }
            Assertions.assertEquals(i, historyManager.getHistory().getLast().getId());
        }
    }

    @Test
    void checkHistoryManagerEntitiesConditions() {
        Task task = new Task(1, "title1", "description1", Status.NEW);
        historyManager.addTask(task);

        Task taskFromHistory = historyManager.getHistory().getFirst();
        Assertions.assertEquals(1, historyManager.getHistory().size(),
                "Количество элементов в истории не равно 1");
        Assertions.assertEquals(task.getId(), taskFromHistory.getId(), "id не совпадают");
        Assertions.assertEquals(task.getTitle(), taskFromHistory.getTitle(), "Заголовки не совпадают");
        Assertions.assertEquals(task.getDescription(), taskFromHistory.getDescription(),
                "Описания не совпадают");
        Assertions.assertEquals(task.getStatus(), taskFromHistory.getStatus(), "Статусы не совпадают");

        task.setTitle("title2");
        task.setDescription("description2");
        task.setStatus(Status.IN_PROGRESS);
        historyManager.addTask(task);

        taskFromHistory = historyManager.getHistory().getFirst();
        Assertions.assertEquals(2, historyManager.getHistory().size(),
                "Количество элементов в истории не равно 2");
        Assertions.assertEquals(task.getId(), taskFromHistory.getId(), "id не совпадают");
        Assertions.assertNotEquals(task.getTitle(), taskFromHistory.getTitle(), "Заголовки совпадают");
        Assertions.assertNotEquals(task.getDescription(), taskFromHistory.getDescription(),
                "Описания совпадают");
        Assertions.assertNotEquals(task.getStatus(), taskFromHistory.getStatus(), "Статусы совпадают");

        taskFromHistory = historyManager.getHistory().getLast();
        Assertions.assertEquals(task.getId(), taskFromHistory.getId(), "id не совпадают");
        Assertions.assertEquals(task.getTitle(), taskFromHistory.getTitle(), "Заголовки не совпадают");
        Assertions.assertEquals(task.getDescription(), taskFromHistory.getDescription(),
                "Описания не совпадают");
        Assertions.assertEquals(task.getStatus(), taskFromHistory.getStatus(), "Статусы не совпадают");
    }
}