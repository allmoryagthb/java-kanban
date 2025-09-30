package http;

import entities.tasks.Epic;
import entities.tasks.Subtask;
import enums.Status;
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

public class HttpTaskServerSubtaskTest extends HttpTaskServerTest {

    @Test
    @DisplayName("Когда добавляется новая подзадача, тогда возвращается ответ")
    public void whenAddingSubtask_thenResponseReturn() throws IOException, InterruptedException {
        Epic epic = new Epic("epic_title_1", "epic_desc_1");
        String json = gson.toJson(epic);
        epic.setId(1);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtask = new Subtask("subt title", "subt desc", Status.NEW, 1,
                LocalDateTime.now().minusHours(1), Duration.ofMinutes(5));
        json = gson.toJson(subtask);
        HttpRequest requestSubtask = HttpRequest.newBuilder(URI.create(BASE_URL + "/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(requestSubtask, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Создана новая подзадача с id 2", response.body(),
                "Некорректное тело ответа");
    }

    @Test
    @DisplayName("Когда обновляется подзадача, тогда возвращается ответ")
    public void whenUpdatingSubtask_thenResponseReturn() throws IOException, InterruptedException {
        Epic epic = new Epic("epic_title_1", "epic_desc_1");
        String json = gson.toJson(epic);
        epic.setId(1);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtask = new Subtask("subt title", "subt desc", Status.NEW, 1,
                LocalDateTime.now().minusHours(10), Duration.ofMinutes(5));
        json = gson.toJson(subtask);
        HttpRequest requestNewSubtask = HttpRequest.newBuilder(URI.create(BASE_URL + "/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        httpClient.send(requestNewSubtask, HttpResponse.BodyHandlers.ofString());

        Subtask subtaskUpd = manager.getSubtask(2);
        subtaskUpd.setTitle("title upd");
        subtaskUpd.setTitle("desc upd");
        json = gson.toJson(subtaskUpd);
        HttpRequest requestUpdSubtask = HttpRequest.newBuilder(URI.create(BASE_URL + "/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(requestUpdSubtask, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Обновлена подзадача с id 2", response.body(),
                "Некорректное тело ответа");
    }

    @Test
    @DisplayName("Когда удаляется подзадача, тогда возвращается ответ")
    public void whenDeletingSubtask_thenResponseReturn() throws IOException, InterruptedException {
        Epic epic = new Epic("epic_title_1", "epic_desc_1");
        String json = gson.toJson(epic);
        epic.setId(1);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtask = new Subtask("subt title", "subt desc", Status.NEW, 1,
                LocalDateTime.now().minusHours(10), Duration.ofMinutes(5));
        json = gson.toJson(subtask);
        HttpRequest requestNewSubtask = HttpRequest.newBuilder(URI.create(BASE_URL + "/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        httpClient.send(requestNewSubtask, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestDelete = HttpRequest.newBuilder(URI.create(BASE_URL + "/subtasks/2"))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Подзадача с id 2 успешно удалена", response.body(),
                "Некорректное тело ответа");
    }

    @Test
    @DisplayName("Когда добавляется новая подзадача с некорректными данными, тогда возвращается ответ")
    public void whenAddingSubtaskWithIncorrectData_thenResponseReturn() throws IOException, InterruptedException {
        Epic epic = new Epic("epic_title_1", "epic_desc_1");
        String json = gson.toJson(epic);
        epic.setId(1);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtask = new Subtask("subt title", "subt desc", Status.NEW, 123,
                LocalDateTime.now().minusHours(1), Duration.ofMinutes(5));
        json = gson.toJson(subtask);
        HttpRequest requestSubtask = HttpRequest.newBuilder(URI.create(BASE_URL + "/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(requestSubtask, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(406, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Ошибка при создании подзадачи", response.body(),
                "Некорректное тело ответа");
    }

    @Test
    @DisplayName("Когда удаляется несуществующая подзадача, тогда возвращается ответ")
    public void whenDeletingNonExistedSubtask_thenErrorReturn() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest requestDelete = HttpRequest.newBuilder(URI.create(BASE_URL + "/subtasks/123"))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Подзадача с id 123 не найдена", response.body(),
                "Некорректное тело ответа");
    }
}
