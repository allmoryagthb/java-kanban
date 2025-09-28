package http.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import entities.manager.FileBackedTaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PrioritizedTaskHttpHandler extends BaseHttpHandler {


    public PrioritizedTaskHttpHandler(FileBackedTaskManager fileBackedTaskManager) {
        super(fileBackedTaskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String httpMethod = exchange.getRequestMethod();

        if (httpMethod.equalsIgnoreCase("GET"))
            getHistory(exchange);
        else
            sendNotFound(exchange, "Эндпоинт не найден");
    }

    private void getHistory(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(gson.toJson(fileBackedTaskManager.getPrioritizedTasks()).getBytes(StandardCharsets.UTF_8));
        }
    }
}
