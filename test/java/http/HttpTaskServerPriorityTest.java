package http;

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

public class HttpTaskServerPriorityTest extends HttpTaskServerTest {

    @Test
    @DisplayName("Когда запрашивается приоритетный список, тогда возвращается ответ")
    public void whenGetPriority_thenResponseReturn() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        Task task1 = new Task("task title1", "task desc", Status.NEW,
                LocalDateTime.now().plusDays(1), Duration.ofMinutes(15));
        Task task2 = new Task("task title2", "task desc", Status.NEW,
                LocalDateTime.now().minusDays(1), Duration.ofMinutes(15));
        Task task3 = new Task("task title3", "task desc", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(15));
        String jsonTask1 = gson.toJson(task1);
        String jsonTask2 = gson.toJson(task2);
        String jsonTask3 = gson.toJson(task3);

        HttpRequest requestTask1 = HttpRequest.newBuilder(URI.create(BASE_URL + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask1))
                .build();
        httpClient.send(requestTask1, HttpResponse.BodyHandlers.ofString());
        HttpRequest requestTask2 = HttpRequest.newBuilder(URI.create(BASE_URL + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask2))
                .build();
        httpClient.send(requestTask2, HttpResponse.BodyHandlers.ofString());
        HttpRequest requestTask3 = HttpRequest.newBuilder(URI.create(BASE_URL + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask3))
                .build();
        httpClient.send(requestTask3, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestPrio = HttpRequest.newBuilder(URI.create(BASE_URL + "/prioritized"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(requestPrio, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        List<Task> prioList = gson.fromJson(response.body(), new TaskTypeToken().getType());

        Assertions.assertEquals(2, prioList.get(0).getId());
        Assertions.assertEquals(3, prioList.get(1).getId());
        Assertions.assertEquals(1, prioList.get(2).getId());
    }
}
