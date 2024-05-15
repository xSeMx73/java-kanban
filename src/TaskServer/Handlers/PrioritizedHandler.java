package TaskServer.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHandler {
    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            send(exchange, gson.toJson(taskManager.getPrioritizedTasks()),200);
        } catch (Exception e) {
            send(exchange, ("Ошибка на сервере"),404);
        }
    }
}