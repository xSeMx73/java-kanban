package TaskServer.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Epic;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class EpicHandler extends BaseHandler {

    public EpicHandler (TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Map<Integer, Epic> epicTemp = new HashMap<>();
        for (Epic epic : taskManager.getEpics()){
            epicTemp.put(epic.getId(),epic);
        }

        String[] path = exchange.getRequestURI().getPath().split("/");
        String method = exchange.getRequestMethod();
        InputStream inputStream = exchange.getRequestBody();
        switch (method) {
            case "GET" :
                try {
                    if (path.length == 2) {
                        List<Epic> epics = taskManager.getEpics();
                        String jsonEpics = gson.toJson(epics);
                        send(exchange, jsonEpics, 200);
                    } else {
                        Epic epic = taskManager.getEpic(Integer.parseInt(path[2]));
                        if (epic == null) {
                            send(exchange, "Неверный ID",404);
                        } else {
                            send(exchange, String.valueOf(epic),200);
                        }
                    }

                } catch (NoSuchElementException e) {
                    exchange.sendResponseHeaders(404, 0);
                    return;
                }
            case "POST" :
                String epicJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Epic epic = gson.fromJson(epicJson, Epic.class);
                Epic epic1 = new Epic(epic.getTitle(),epic.getDescription()); // без этого epicSubtasksID = null и addSubIdToEpic не работает

                try {
                    if (path.length == 2) {
                        taskManager.addEpic(epic1);
                        if (taskManager.epicsEquals(epic1)) {
                            send(exchange, "Эпик добавлен",201);
                            System.out.println("Пользователь добавил эпик");
                        } else {
                            send(exchange, "Ошибка добавления эпика",406);
                        }
                        return;

                    } else if (epicTemp.containsKey(Integer.parseInt(path[2]))) {
                        epic1.setId(Integer.parseInt(path[2]));
                        taskManager.updateEpic(epic1);
                        if (taskManager.epicsEquals(epic1)) {
                            send(exchange, "Эпик обновлен",201);
                            System.out.println("Пользователь обновил эпик с Id-" + epic1.getId());
                        } else {
                            send(exchange, "Ошибка обновления эпика",406);
                        }
                    } else {
                        send(exchange, "Неверно указан Id эпика",404);
                    }
                } catch (NoSuchElementException e) {
                    exchange.sendResponseHeaders(404, 0);
                }
                break;
            case "DELETE" :
                try {
                    int epicId = Integer.parseInt(path[2]);
                    if (!epicTemp.containsKey(epicId)) {
                        send(exchange, "Некорректно указан Id эпика",404);
                    } else {
                        taskManager.deleteEpic(epicId);
                    }
                    send(exchange, "Эпик удален",200);
                } catch (Exception e) {
                    exchange.sendResponseHeaders(404, 0);
                }
                break;
        }
    }
}


