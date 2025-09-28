package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.manager.FileBackedTaskManager;
import entities.tasks.Epic;
import http.adapter.DurationAdapter;
import http.adapter.LocalDateTimeAdapter;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServerEpicTest {
    private HttpTaskServer httpTaskServer;
    private FileBackedTaskManager manager;
    private final String BASE_URL = "http://localhost:8080";
    private Gson gson;

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

    @Test
    @DisplayName("Когда добавляется новый эпик, тогда возвращается ответ")
    public void whenAddingEpic_thenResponseReturn() throws IOException, InterruptedException {
        Epic epic = new Epic("epic_title_1", "epic_desc_1");
        String json = gson.toJson(epic);
        epic.setId(1);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Создан новый эпик с id 1", response.body(), "Некорректное тело ответа");
        Assertions.assertEquals(1, manager.getAllEpics().size());
        Assertions.assertEquals(epic, manager.getEpic(1));
    }

    @Test
    @DisplayName("Когда запрашивается эпик с существующим id, тогда возвращается ответ")
    public void whenGetEpicWithExistedId_thenResponseReturn() throws IOException, InterruptedException {
        Epic epic = new Epic("epic_title_1", "epic_desc_1");
        String json = gson.toJson(epic);
        epic.setId(1);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestGet = HttpRequest.newBuilder(URI.create(BASE_URL + "/epics/1"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(requestGet, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals(epic, gson.fromJson(response.body(), Epic.class),
                "Некорректное тело ответа");
    }

    @Test
    @DisplayName("Когда удаляется эпик с существующим id, тогда возвращается ответ")
    public void whenDeleteEpicWithExistedId_thenResponseReturn() throws IOException, InterruptedException {
        Epic epic = new Epic("epic_title_1", "epic_desc_1");
        String json = gson.toJson(epic);
        epic.setId(1);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestDelete = HttpRequest.newBuilder(URI.create(BASE_URL + "/epics/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Эпик с id 1 успешно удален", response.body(), "Некорректное тело ответа");
    }

    @Test
    @DisplayName("Когда запрашивается эпик с несуществующим id, тогда возвращается ответ")
    public void whenGetEpicWithNonExistedId_thenResponseReturn() throws IOException, InterruptedException {
        HttpRequest requestGet = HttpRequest.newBuilder(URI.create(BASE_URL + "/epics/123"))
                .GET()
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> response = httpClient.send(requestGet, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Эпик с id 123 не найден", response.body(), "Некорректное тело ответа");
    }

    @Test
    @DisplayName("Когда удаляется эпик с несуществующим id, тогда возвращается ответ")
    public void whenDeleteEpicWithNonExistedId_thenResponseReturn() throws IOException, InterruptedException {
        HttpRequest requestGet = HttpRequest.newBuilder(URI.create(BASE_URL + "/epics/123"))
                .DELETE()
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> response = httpClient.send(requestGet, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Эпик с id 123 не найден", response.body(), "Некорректное тело ответа");
    }
}
