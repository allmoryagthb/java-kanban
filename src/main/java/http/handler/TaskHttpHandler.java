package http.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import entities.manager.TaskManager;
import entities.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TaskHttpHandler extends BaseHttpHandler {

    public TaskHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String httpMethod = exchange.getRequestMethod();
        String[] urlArr = exchange.getRequestURI().getPath().split("/");

        if (httpMethod.equalsIgnoreCase("GET") && urlArr.length == 2)
            getTasks(exchange);
        else if (httpMethod.equalsIgnoreCase("GET") && urlArr.length == 3)
            getTaskById(exchange, Integer.parseInt(urlArr[2]));
        else if (httpMethod.equalsIgnoreCase("POST"))
            postTask(exchange);
        else if (httpMethod.equalsIgnoreCase("DELETE"))
            deleteTask(exchange, Integer.parseInt(urlArr[2]));

    }

    private void getTasks(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(gson.toJson(manager.getAllTasks()).getBytes(StandardCharsets.UTF_8));
        }
    }

    private void getTaskById(HttpExchange exchange, int id) throws IOException {
        Task task = manager.getTask(id);

        if (task == null) {
            sendNotFound(exchange, "Задача с id %d не найдена".formatted(id));
        } else {
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(gson.toJson(task).getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    private void postTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Task inputTask = gson.fromJson(body, Task.class);

        if (inputTask.getId() == null) {
            int id = manager.addTask(inputTask);
            if (id > 0)
                sendText(exchange, "Создана новая задача с id %d".formatted(id), 201);
            else
                sendHasOverlaps(exchange, "Произошла ошибка при добавлении новой задачи");
        } else if (inputTask.getId() > 0 && manager.getTask(inputTask.getId()) != null) {
            boolean isSuccess = manager.updateTask(inputTask);
            if (isSuccess)
                sendText(exchange, "Обновлена задача с id %d".formatted(inputTask.getId()), 200);
            else {
                sendHasOverlaps(exchange, "Произошла ошибка при обновлении задачи");
            }
        } else {
            sendHasOverlaps(exchange, "Задачи с id %d не существует".formatted(inputTask.getId()));
        }
    }

    private void deleteTask(HttpExchange exchange, int id) throws IOException {
        boolean result = manager.deleteTaskById(id);
        if (result)
            sendText(exchange, "Задача с id %d успешно удалена".formatted(id));
        else {
            sendNotFound(exchange, "Задача с id %d не найдена".formatted(id));
        }
    }
}
