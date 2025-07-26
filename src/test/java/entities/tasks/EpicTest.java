package entities.tasks;

import enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {

    @Test
    void checkEpicsAreEqualsIfIdsAreEquals() {
        Epic epic1 = new Epic(1, "Aaa", "Aaa");
        Epic epic2 = new Epic(1, "Bbb", "Bbb");

        Assertions.assertEquals(epic1, epic2, "Epics are no equals");
    }

    @Test
    void checkEpicsNotEqualsIfIdsNotEquals() {
        Epic epic1 = new Epic(123, "Aaa", "Aaa");
        Epic epic2 = new Epic(321, "Aaa", "Aaa");

        Assertions.assertNotEquals(epic1, epic2, "Epics are equals");
    }
}