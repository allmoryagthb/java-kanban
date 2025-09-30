package http;

import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import enums.Status;
import http.token.TaskTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskServerHistoryTest extends HttpTaskServerTest {

    @Test
    @DisplayName("Когда запрашивается история, тогда возвращается ответ")
    public void whenGetHistory_thenResponseReturn() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        Task task = new Task("task title1", "task desc", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(15));
        Epic epic = new Epic("epic title", "epic description");
        Subtask subtask = new Subtask("subt title", "subt desc", Status.NEW, 2,
                LocalDateTime.now().minusHours(1), Duration.ofMinutes(5));
        String jsonTask = gson.toJson(task);
        String jsonEpic = gson.toJson(epic);
        String jsonSubtask = gson.toJson(subtask);

        HttpRequest requestTask = HttpRequest.newBuilder(URI.create(BASE_URL + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();
        httpClient.send(requestTask, HttpResponse.BodyHandlers.ofString());
        HttpRequest requestEpic = HttpRequest.newBuilder(URI.create(BASE_URL + "/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .build();
        httpClient.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        HttpRequest requestSubtask = HttpRequest.newBuilder(URI.create(BASE_URL + "/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask))
                .build();
        httpClient.send(requestSubtask, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestTaskGet = HttpRequest.newBuilder(URI.create(BASE_URL + "/tasks/1"))
                .GET()
                .build();
        httpClient.send(requestTaskGet, HttpResponse.BodyHandlers.ofString());
        HttpRequest requestSubtaskGet = HttpRequest.newBuilder(URI.create(BASE_URL + "/subtasks/3"))
                .GET()
                .build();
        httpClient.send(requestSubtaskGet, HttpResponse.BodyHandlers.ofString());
        HttpRequest requestEpicGet = HttpRequest.newBuilder(URI.create(BASE_URL + "/epics/2"))
                .GET()
                .build();
        httpClient.send(requestEpicGet, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestHistoryGet = HttpRequest.newBuilder(URI.create(BASE_URL + "/history"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(requestHistoryGet, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        List<Task> history = gson.fromJson(response.body(), new TaskTypeToken().getType());

        Assertions.assertEquals(1, history.get(0).getId());
        Assertions.assertEquals(3, history.get(1).getId());
        Assertions.assertEquals(2, history.get(2).getId());
    }
}
