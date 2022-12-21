package kanban.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import kanban.manager.Managers;
import kanban.manager.TaskManager;
import kanban.tasks.Epic;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.ArrayList;


import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    private static final int PORT = 8883;
    private Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
        server.start();
    }

    private void handler(HttpExchange exchange) {
        try {
            System.out.println("Идет обработка запроса " + exchange.getRequestURI().getPath());
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            final String query = exchange.getRequestURI().getQuery();
            switch (method) {
                case "GET":
                    if (path.equals("/tasks/task/")) {
                        if (query == null) {
                            String response = gson.toJson(taskManager.takeTasks());
                            sendText(exchange, response);
                            return;
                        }
                        String idString = query.substring(3);
                        int id = parsePathId(idString);
                        String response = gson.toJson(taskManager.getTasks(id));
                        sendText(exchange, response);
                        return;
                    }
                    if (path.equals("/tasks/subtask/")) {
                        if (query == null) {
                            String response = gson.toJson(taskManager.takeSubTasks());
                            sendText(exchange, response);
                            return;
                        }
                        String idString = query.substring(3);
                        int id = parsePathId(idString);
                        String response = gson.toJson(taskManager.getSubTasks(id));
                        sendText(exchange, response);
                        return;
                    }
                    if (path.equals("/tasks/epic/")) {
                        if (query == null) {
                            Type epicsArrType = new TypeToken<ArrayList<Epic>>() {}.getType();
                            String response = gson.toJson(taskManager.takeEpics(), epicsArrType);
                            sendText(exchange, response);
                            return;
                        }
                        String idString = query.substring(3);
                        int id = parsePathId(idString);
                        String response = gson.toJson(taskManager.getEpics(id));
                        sendText(exchange, response);
                        return;
                    }
                    if (path.equals("/tasks/subtask/epic/")) {
                        String idString = query.substring(3);
                        int id = parsePathId(idString);
                        String response = gson.toJson(taskManager.takeEpicsTasksById(id));
                        sendText(exchange, response);
                        return;
                    }
                    if (path.equals("/tasks/")) {
                        String response = gson.toJson(taskManager.getPrioritizedTasks());
                        sendText(exchange, response);
                        return;
                    }
                    if (path.equals("/tasks/history/")) {
                        String response = gson.toJson(taskManager.getHistory());
                        sendText(exchange, response);
                    }
                    break;
                case "POST":
                    return;
                case "DELETE":
                    if (path.equals("/tasks/task/")) {
                        if (query == null) {
                            taskManager.deliteTasks();
                            exchange.sendResponseHeaders(200, 0);
                        }
                        String idString = query.substring(3);
                        int id = parsePathId(idString);
                        taskManager.deliteTasksId(id);
                        exchange.sendResponseHeaders(200, 0);
                        return;
                    }
                    if (path.equals("/tasks/subtask/")) {
                        if (query == null) {
                            taskManager.deliteSubTasks();
                            exchange.sendResponseHeaders(200, 0);
                        }
                        String idString = query.substring(3);
                        int id = parsePathId(idString);
                        taskManager.deliteSubTasksId(id);
                        exchange.sendResponseHeaders(200, 0);
                        return;
                    }
                    if (path.equals("/tasks/epic/")) {
                        if (query == null) {
                            taskManager.deliteEpics();
                            exchange.sendResponseHeaders(200, 0);
                        }
                        String idString = query.substring(3);
                        int id = parsePathId(idString);
                        taskManager.deliteEpicsId(id);
                        exchange.sendResponseHeaders(200, 0);
                        return;
                    }
                    break;
                default:
                    System.out.println("Ждем GET или DELETE запрос, а получаем" + method);
                    exchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception e) {
            System.out.println("Произошло падение сервера");
        } finally {
            exchange.close();
        }
    }

    private void sendText(HttpExchange httpExchange , String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, resp.length);
        httpExchange.getResponseBody().write(resp);
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}


