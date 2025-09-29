package http;

import com.sun.net.httpserver.HttpServer;
import entities.manager.TaskManager;
import http.handler.*;
import util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final int port;
    private static HttpServer httpServer;
    public final TaskManager manager;

    public HttpTaskServer() {
        this.port = 8080;
        this.manager = Managers.getInMemoryTaskManager();
    }

    public HttpTaskServer(int portNum, TaskManager manager) {
        this.port = portNum;
        this.manager = manager;
    }

    public static void main(String[] args) {
        new HttpTaskServer().start();
    }

    public void start() {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(port), 0);
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
