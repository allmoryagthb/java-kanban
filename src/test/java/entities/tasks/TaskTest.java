package entities.tasks;

import enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void checkTestsEqualsIfIdsAreEquals() {
        Task task1 = new Task(1, "Aaa", "Aaa", Status.NEW);
        Task task2 = new Task(1, "Bbb", "Bbb", Status.IN_PROGRESS);

        Assertions.assertEquals(task1, task2, "Tasks not equals");
    }

    @Test
    void checkTestsNotEqualsIfIdsNotEquals() {
        Task task1 = new Task(123, "Aaa", "Aaa", Status.NEW);
        Task task2 = new Task(321, "Aaa", "Aaa", Status.NEW);

        Assertions.assertNotEquals(task1, task2, "Tasks equals");
    }

}