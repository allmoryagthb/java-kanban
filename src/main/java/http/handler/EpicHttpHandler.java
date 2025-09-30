package http.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import entities.manager.TaskManager;
import entities.tasks.Epic;
import entities.tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHttpHandler extends BaseHttpHandler {

    public EpicHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String httpMethod = exchange.getRequestMethod();
        String[] urlArr = exchange.getRequestURI().getPath().split("/");

        if (httpMethod.equalsIgnoreCase("GET") && urlArr.length == 2)
            getEpics(exchange);
        else if (httpMethod.equalsIgnoreCase("GET") && urlArr.length == 3)
            getEpicById(exchange, Integer.parseInt(urlArr[2]));
        else if (httpMethod.equalsIgnoreCase("GET")
                && urlArr.length == 4 && urlArr[3].equalsIgnoreCase("subtasks"))
            getEpicSubtasks(exchange, Integer.parseInt(urlArr[2]));
        else if (httpMethod.equalsIgnoreCase("POST"))
            postEpic(exchange);
        else if (httpMethod.equalsIgnoreCase("DELETE"))
            deleteEpic(exchange, Integer.parseInt(urlArr[2]));
        else
            sendNotFound(exchange, "Эндпоинт не найден");
    }

    private void getEpics(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(gson.toJson(manager.getAllEpics()).getBytes(StandardCharsets.UTF_8));
        }
    }

    private void getEpicById(HttpExchange exchange, int id) throws IOException {
        Epic epic = manager.getEpic(id);

        if (epic == null) {
            sendNotFound(exchange, "Эпик с id %d не найден".formatted(id));
        } else {
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(gson.toJson(epic).getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    private void getEpicSubtasks(HttpExchange exchange, int id) throws IOException {
        Epic epic = manager.getEpic(id);
        List<Subtask> subtaskList = manager.getEpicSubtasks(id);
        if (epic == null) {
            sendNotFound(exchange, "Эпик с id %d не найден".formatted(id));
        } else {
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(gson.toJson(subtaskList).getBytes(StandardCharsets.UTF_8));
            }
        }

    }

    private void postEpic(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic inputEpic = gson.fromJson(body, Epic.class);

        if (inputEpic.getId() == null) {
            int id = manager.addEpic(inputEpic);
            sendText(exchange, "Создан новый эпик с id %d".formatted(id), 201);
        } else if (inputEpic.getId() > 0 && manager.getEpic(inputEpic.getId()) != null) {
            boolean isSuccess = manager.updateEpic(inputEpic);
            if (isSuccess)
                sendText(exchange, "Обновлен эпик с id %d".formatted(inputEpic.getId()));
            else {
                sendHasOverlaps(exchange, "Произошла ошибка при обновлении эпика");
            }
        } else {
            sendNotFound(exchange, "Эпика с id %d не существует".formatted(inputEpic.getId()));
        }
    }

    private void deleteEpic(HttpExchange exchange, int id) throws IOException {
        boolean result = manager.deleteEpicById(id);
        if (result)
            sendText(exchange, "Эпик с id %d успешно удален".formatted(id));
        else {
            sendNotFound(exchange, "Эпик с id %d не найден".formatted(id));
        }
    }
}
