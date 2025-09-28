package http;

import com.sun.net.httpserver.HttpServer;
import entities.manager.FileBackedTaskManager;
import http.handler.*;
import util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer httpServer;
    private static final FileBackedTaskManager fileBackedTaskManager = Managers.getDefault();

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TaskHttpHandler(fileBackedTaskManager));
            httpServer.createContext("/epics", new EpicHttpHandler(fileBackedTaskManager));
            httpServer.createContext("/subtasks", new SubtaskHttpHandler(fileBackedTaskManager));
            httpServer.createContext("/history", new HistoryHttpHandler(fileBackedTaskManager));
            httpServer.createContext("/prioritized", new PrioritizedTaskHttpHandler(fileBackedTaskManager));
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void stop() {
        httpServer.stop(60);
    }
}
