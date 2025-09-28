package http;

import entities.tasks.Task;
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

public class HttpTaskServerTaskTest extends HttpTaskServerTest {

    @Test
    @DisplayName("Когда добавляется новая задача, тогда возвращается ответ")
    public void whenAddingTask_thenResponseReturn() throws IOException, InterruptedException {
        Task task = new Task("task_title_1", "task_desc_1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        String json = gson.toJson(task);
        task.setId(1);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Создана новая задача с id 1", response.body(), "Некорректное тело ответа");
        Assertions.assertEquals(1, manager.getAllTasks().size());
        Assertions.assertEquals(task, manager.getTask(1));
    }

    @Test
    @DisplayName("Когда обновляется задача, тогда возвращается ответ")
    public void whenUpdatingTask_thenResponseReturn() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        Task task = new Task("task_title_1", "task_desc_1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        String json = gson.toJson(task);
        task.setId(1);

        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Task taskFromManager = manager.getTask(1);
        taskFromManager.setTitle("task_title_1 UPD");
        taskFromManager.setDescription("task_desc_1 UPD");
        json = gson.toJson(task);

        HttpRequest requestUpd = HttpRequest.newBuilder(URI.create(BASE_URL + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(requestUpd, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Обновлена задача с id 1", response.body(), "Некорректное тело ответа");
        Assertions.assertEquals(1, manager.getAllTasks().size());
        Assertions.assertEquals(task, manager.getTask(1));
    }

    @Test
    @DisplayName("Когда удаляется существующая задача, тогда возвращается ответ")
    public void whenDeleteExistingTask_thenResponseReturn() throws IOException, InterruptedException {
        Task task = new Task("task_title_1", "task_desc_1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        String json = gson.toJson(task);
        task.setId(1);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestDelete = HttpRequest.newBuilder(URI.create(BASE_URL + "/tasks/1"))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(requestDelete, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Задача с id 1 успешно удалена", response.body(), "Некорректное тело ответа");
        Assertions.assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    @DisplayName("Когда обновляется несуществующая задача, тогда возвращается ошибка")
    public void whenUpdatingNonExistingTask_thenErrorReturn() throws IOException, InterruptedException {
        Task task = new Task(123, "task_title_1", "task_desc_1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        String json = gson.toJson(task);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(406, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Задачи с id 123 не существует", response.body(), "Некорректное тело ответа");
    }

    @Test
    @DisplayName("Когда удаляется несуществующая задача, тогда возвращается ошибка")
    public void whenDeletingNonExistingTask_thenErrorReturn() throws IOException, InterruptedException {
        Task task = new Task(123, "task_title_1", "task_desc_1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        String json = gson.toJson(task);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/tasks/123"))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(404, response.statusCode(), "Некорректный код ответа");
        Assertions.assertEquals("Задача с id 123 не найдена", response.body(), "Некорректное тело ответа");
    }
}
