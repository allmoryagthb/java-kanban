package http;

import com.sun.net.httpserver.HttpServer;
import entities.manager.InMemoryTaskManager;
import http.handler.*;
import util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer httpServer;
    public final InMemoryTaskManager manager = Managers.getInMemoryTaskManager();

    public static void main(String[] args) {
        new HttpTaskServer().start();
    }

    public void start() {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TaskHttpHandler(manager));
            httpServer.createContext("/epics", new EpicHttpHandler(manager));
            httpServer.createContext("/subtasks", new SubtaskHttpHandler(manager));
            httpServer.createContext("/history", new HistoryHttpHandler(manager));
            httpServer.createContext("/prioritized", new PrioritizedTaskHttpHandler(manager));
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        httpServer.stop(1);
    }
}
