package entities.file;

import entities.manager.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileBackedTaskManagerTest {
    private File file;
    private FileBackedTaskManager fileBackedTaskManager;

    @BeforeEach
    public void before() {
        this.file = new File("test.csv");
        this.fileBackedTaskManager = new FileBackedTaskManager(file);
    }

    @AfterEach
    public void after() {
        file.delete();
    }

    @Test
    public void checkThatEmptyFileIsCreated() {

    }
}
