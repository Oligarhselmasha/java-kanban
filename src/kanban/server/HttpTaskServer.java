package kanban.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import kanban.manager.Managers;
import kanban.manager.TaskManager;
import kanban.tasks.Epic;
import kanban.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.ArrayList;


import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    private static int PORT;
    private final Gson gson;
    private final TaskManager taskManager;
    private final HttpServer server;


    public HttpTaskServer(TaskManager taskManager, int port) throws IOException {
        PORT = port;
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
        server.start();
    }

    private void handler(HttpExchange exchange) {
        try {
            System.out.println("Идет обработка запроса " + exchange.getRequestURI().getPath());
            String path = exchange.getRequestURI().getPath().substring(7);
            String method = exchange.getRequestMethod();
            final String query = exchange.getRequestURI().getQuery();
            switch (method) {
                case "GET":
                    if (path.equals("task/")) {
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
                    if (path.equals("subtask/")) {
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
                    if (path.equals("epic/")) {
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
                    if (path.equals("subtask/epic/")) {
                        String idString = query.substring(3);
                        int id = parsePathId(idString);
                        String response = gson.toJson(taskManager.takeEpicsTasksById(id));
                        sendText(exchange, response);
                        return;
                    }
                    if (path.equals("")) {
                        String response = gson.toJson(taskManager.getPrioritizedTasks());
                        sendText(exchange, response);
                        return;
                    }
                    if (path.equals("history/")) {
                        String response = gson.toJson(taskManager.getHistory());
                        sendText(exchange, response);
                    }
                    break;
                case "POST":
                    if (path.equals("task/")) {
                        InputStream inputStream = exchange.getRequestBody();
                        String taskString = new String(inputStream.readAllBytes());
                        if (taskString.isEmpty()){
                            writeResponse(exchange, "Поля комментария не могут быть пустыми", 400);
                            return;
                        }
                        try {
                            Task task = gson.fromJson(taskString, Task.class);
                        }
                        catch (JsonSyntaxException exception){
                            writeResponse(exchange, "Переданное тело некорректно", 400);
                            return;
                        }
                        Task task = gson.fromJson(taskString, Task.class);
                        taskManager.addTask(task);
                        writeResponse(exchange, "Задача успешно добавлена!", 201);
                    }
                   break;
                case "DELETE":
                    if (path.equals("task/")) {
                        if (query == null) {
                            taskManager.deliteTasks();
                            exchange.sendResponseHeaders(200, 0);
                            return;
                        }
                        String idString = query.substring(3);
                        int id = parsePathId(idString);
                        taskManager.deliteTasksId(id);
                        exchange.sendResponseHeaders(200, 0);
                        return;
                    }
                    if (path.equals("subtask/")) {
                        if (query == null) {
                            taskManager.deliteSubTasks();
                            exchange.sendResponseHeaders(200, 0);
                            return;
                        }
                        String idString = query.substring(3);
                        int id = parsePathId(idString);
                        taskManager.deliteSubTasksId(id);
                        exchange.sendResponseHeaders(200, 0);
                        return;
                    }
                    if (path.equals("epic/")) {
                        if (query == null) {
                            taskManager.deliteEpics();
                            exchange.sendResponseHeaders(200, 0);
                            return;
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
            exchange.close();
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
    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes();
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
            exchange.close();
        }

    }
    public void stop(){
        server.stop(0);
    }
}


