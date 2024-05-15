package TaskServer.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class SubtaskHandler extends BaseHandler {

        public SubtaskHandler (TaskManager taskManager, Gson gson) {
            super(taskManager, gson);
        }


        @Override
        public void handle(HttpExchange exchange) throws IOException {

            Map<Integer, Subtask> subtaskTemp = new HashMap<>();
            for (Subtask subtask : taskManager.getSubtasks()){
                subtaskTemp.put(subtask.getId(),subtask);
            }

            String[] path = exchange.getRequestURI().getPath().split("/");
            String method = exchange.getRequestMethod();
            InputStream inputStream = exchange.getRequestBody();
            switch (method) {
                case "GET" :
                    try {
                        if (path.length == 2) {
                            List<Subtask> subtasks = taskManager.getSubtasks();
                            String jsonSubtasks = gson.toJson(subtasks);
                            send200(exchange, jsonSubtasks);
                        } else {
                            Subtask subtask = taskManager.getSubtask(Integer.parseInt(path[2]));
                            if (subtask == null) {
                                send404(exchange, "Неверный ID");
                            } else {
                                send200(exchange, String.valueOf(subtask));
                            }
                        }

                    } catch (NoSuchElementException e) {
                        exchange.sendResponseHeaders(404, 0);
                        return;
                    }
                case "POST" :
                    String subtaskJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Subtask subtask = gson.fromJson(subtaskJson, Subtask.class);

                    try {
                        if (path.length == 2) {
                            taskManager.addSubtask(subtask);
                            if (taskManager.subtasksEquals(subtask)) {
                                send201(exchange, "Подзадача добавлена");
                                System.out.println("Пользователь добавил подзадачу");
                            } else {
                                send406(exchange, "Ошибка добавления подзадачи");
                            }
                            return;

                        } else if (subtaskTemp.containsKey(Integer.parseInt(path[2]))) {
                            subtask.setId(Integer.parseInt(path[2]));
                            taskManager.updateSubtask(subtask);
                            if (taskManager.subtasksEquals(subtask)) {
                                send201(exchange, "Подзадача обновлена");
                                System.out.println("Пользователь обновил подзадачу с Id-" + subtask.getId());
                                return;
                            } else {
                                send406(exchange, "Ошибка обновления, подзадача пересекается по времени с другой");
                            }
                        } else {
                            send404(exchange, "Неверно указан Id подзадачи");
                            return;
                        }
                    } catch (NoSuchElementException e) {
                        exchange.sendResponseHeaders(404, 0);
                    }
                    break;
                case "DELETE" :
                    try {
                        int subtaskId = Integer.parseInt(path[2]);
                        if (!subtaskTemp.containsKey(subtaskId)) {
                            send404(exchange, "Некорректно указан Id подзадачи");
                            return;
                        }
                        taskManager.deleteSubtask(subtaskId);
                        send200(exchange, "Подзадача удалена");
                    } catch (Exception e) {
                        exchange.sendResponseHeaders(404, 0);
                    }
                    break;
            }

        }

    }


