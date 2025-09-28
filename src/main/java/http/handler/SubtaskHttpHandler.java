package http.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import entities.manager.FileBackedTaskManager;
import entities.tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SubtaskHttpHandler extends BaseHttpHandler {

    public SubtaskHttpHandler(FileBackedTaskManager fileBackedTaskManager) {
        super(fileBackedTaskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String httpMethod = exchange.getRequestMethod();
        String[] urlArr = exchange.getRequestURI().getPath().split("/");

        if (httpMethod.equalsIgnoreCase("GET") && urlArr.length == 2)
            getSubtasks(exchange);
        else if (httpMethod.equalsIgnoreCase("GET") && urlArr.length == 3)
            getSubtaskById(exchange, Integer.parseInt(urlArr[2]));
        else if (httpMethod.equalsIgnoreCase("POST"))
            postSubtask(exchange);
        else if (httpMethod.equalsIgnoreCase("DELETE"))
            deleteSubtask(exchange, Integer.parseInt(urlArr[2]));
        else
            sendNotFound(exchange, "Эндпоинт не найден");
    }

    private void getSubtasks(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(gson.toJson(fileBackedTaskManager.getAllSubtasks()).getBytes(StandardCharsets.UTF_8));
        }
    }

    private void getSubtaskById(HttpExchange exchange, int id) throws IOException {
        Subtask subtask = fileBackedTaskManager.getSubtask(id);

        if (subtask == null) {
            sendNotFound(exchange, "Подзадача с id %d не найдена".formatted(id));
        } else {
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(gson.toJson(subtask).getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    private void postSubtask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Subtask inputSubtask = gson.fromJson(body, Subtask.class);

        if (inputSubtask.getId() == null) {
            int id = fileBackedTaskManager.addSubtask(inputSubtask);
            if (id > 0)
                sendText(exchange, "Создана новая подзадача с id %d".formatted(id), 201);
            else
                sendHasOverlaps(exchange, "Ошибка при создании подзадачи");
        } else {
            boolean result = fileBackedTaskManager.updateSubtask(inputSubtask);
            if (result)
                sendText(exchange, "Обновлена подзадача с id %d".formatted(inputSubtask.getId()));
            else {
                sendHasOverlaps(exchange, "Произошла ошибка при обновлении подзадачи");
            }
        }
    }

    private void deleteSubtask(HttpExchange exchange, int id) throws IOException {
        boolean result = fileBackedTaskManager.deleteSubtaskById(id);
        if (result)
            sendText(exchange, "Подзадача с id %d успешно удалена".formatted(id));
        else {
            sendNotFound(exchange, "Подзадача с id %d не найдена".formatted(id));
        }
    }
}
