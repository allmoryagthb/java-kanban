package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.manager.FileBackedTaskManager;
import http.adapter.DurationAdapter;
import http.adapter.LocalDateTimeAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class HttpTaskServerTest {
    protected HttpTaskServer httpTaskServer;
    protected FileBackedTaskManager manager;
    protected final String BASE_URL = "http://localhost:8080";
    protected Gson gson;

    @BeforeEach
    public void setUp() {
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        this.manager = httpTaskServer.fileBackedTaskManager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @AfterEach
    public void cleanUp() {
        httpTaskServer.stop();
    }
}
