package TaskServer;

import TaskServer.Handlers.*;
import TaskServer.adapter.*;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTaskManager;
import manager.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private static final int PORT = 8180;

    static Path dataServer = Path.of("dataBaseServer.csv");
    protected static File dataBaseServer = dataServer.toFile();
    private final Gson gson;

    public HttpTaskServer(TaskManager manager) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        gson = new Gson()
                .newBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        server.createContext("/tasks", new TaskHandler(manager, gson));
        server.createContext("/subtasks", new SubtaskHandler(manager, gson));
        server.createContext("/epics", new EpicHandler(manager, gson));
        server.createContext("/history", new HistoryHandler(manager, gson));
        server.createContext("/prioritized", new PrioritizedHandler(manager, gson));
        server.start();

    }



    public static void main(String[] args) throws IOException {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(dataBaseServer);
        HttpTaskServer server = new HttpTaskServer(taskManager);


    }
}
