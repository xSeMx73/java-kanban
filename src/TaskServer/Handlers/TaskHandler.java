package TaskServer.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import task.Task;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TaskHandler extends BaseHandler {


    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Map <Integer,Task> tasksTemp = new HashMap<>();
        for (Task task : taskManager.getTasks()){
            tasksTemp.put(task.getId(),task);
        }

        String[] path = exchange.getRequestURI().getPath().split("/");
        String method = exchange.getRequestMethod();
        InputStream inputStream = exchange.getRequestBody();
        switch (method) {
            case "GET" :
                try {
                if (path.length == 2) {
                    List<Task> tasks = taskManager.getTasks();
                    String jsonTasks = gson.toJson(tasks);
                    send(exchange, jsonTasks,200);
                } else {
                    Task task = taskManager.getTask(Integer.parseInt(path[2]));
                    if (task == null) {
                        send(exchange, "Неверный ID",404);
                    } else {
                        send(exchange, String.valueOf(task),200);
                    }
                }

                } catch (NoSuchElementException e) {
                        exchange.sendResponseHeaders(404, 0);
                       return;
                    }
            case "POST" :
                String taskJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Task task = gson.fromJson(taskJson, Task.class);

                try {
                    if (path.length == 2) {
                        taskManager.addTask(task);
                        if (taskManager.tasksEquals(task)) {
                            send(exchange, "Задача добавлена",201);
                            System.out.println("Пользователь добавил задачу");
                        } else {
                            send(exchange, "Задача пересекается по времени с другой",406);
                        }
                        return;

                    } else if (tasksTemp.containsKey(Integer.parseInt(path[2]))) {
                        task.setId(Integer.parseInt(path[2]));
                        taskManager.updateTask(task, Integer.parseInt(path[2]));
                        if (taskManager.tasksEquals(task)) {
                            send(exchange, "Задача обновлена",201);
                            System.out.println("Пользователь обновил задачу с Id-" + task.getId());
                            return;
                        } else {
                            send(exchange, "Задача пересекается по времени с другой",406);
                        }
                    } else {
                        send(exchange, "Неверно указан Id задачи",404);
                        return;
                    }
                } catch (NoSuchElementException e) {
                    exchange.sendResponseHeaders(404, 0);
                }
                break;
            case "DELETE" :
                try {
                int taskId = Integer.parseInt(path[2]);
                if (!tasksTemp.containsKey(taskId)) {
                   send(exchange, "Некорректно указан Id задачи",404);
                    return;
                }
                    taskManager.deleteTask(taskId);
                    send(exchange, "Задача удалена",200);
                } catch (Exception e) {
                    exchange.sendResponseHeaders(404, 0);
                }
                break;
        }

    }

}
