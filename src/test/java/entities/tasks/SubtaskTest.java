package entities.tasks;

import enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {

    @Test
    void checkSubtasksAreEqualsIfIdsAreEquals() {
        Subtask subtask1 = new Subtask(123, "Aaa", "Aaa", Status.NEW, 1);
        Subtask subtask2 = new Subtask(123, "Bbb", "Bbb", Status.IN_PROGRESS, 2);

        Assertions.assertEquals(subtask1, subtask2, "Subtasks not equals");
    }

    @Test
    void checkSubtasksNotEqualsIfIdsNotEquals() {
        Subtask subtask1 = new Subtask(123, "Aaa", "Aaa", Status.NEW, 1);
        Subtask subtask2 = new Subtask(321, "Aaa", "Aaa", Status.NEW, 1);

        Assertions.assertNotEquals(subtask1, subtask2, "Subtasks not equals");
    }

}