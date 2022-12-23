package kanban.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kanban.server.HttpTaskManager;
import kanban.server.HttpTaskServer;
import kanban.server.KVServer;
import kanban.tasks.Epic;
import kanban.tasks.Subtask;
import kanban.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    HttpClient client = HttpClient.newHttpClient();

    String urlBase = "http://localhost:8883/";

    static Gson gson;

    @BeforeAll
    static void setUpKVServer() throws IOException {
        gson = Managers.getGson();
    }

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        this.kvServer = new KVServer();
        kvServer.start();
        taskManager = new HttpTaskManager(8882); // Который является полем нового менеджера
        initTasks();
        this.httpTaskServer = new HttpTaskServer(taskManager, 8883);
    }

    @AfterEach
    void endAll() throws IOException, InterruptedException {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    void deliteTasks() {
        URI url = URI.create(urlBase + "tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            Map<Integer, Task> testTasks = taskManager.getTasks();
            assertEquals(testTasks.size(), 0, "Задачи не удалились!");
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void deliteSubTasksId() {
        URI url = URI.create(urlBase + "tasks/subtask/?id=4");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            List<Subtask> testSubtasks = taskManager.takeSubTasks();
            assertEquals(testSubtasks.size(), 1, "Задачи не удалились!");
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getTasks() {
        URI url = URI.create(urlBase + "tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Task task = gson.fromJson(response.body(), Task.class);
            assertEquals(task, task1, "Полученная с сервера задача не соответствует тестовой");
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getSubTasks() {
        URI url = URI.create(urlBase + "tasks/subtask/?id=4");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Task task = gson.fromJson(response.body(), Task.class);
            assertEquals(task, subtask1, "Полученная с сервера задача не соответствует тестовой");
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getEpics() {
        URI url = URI.create(urlBase + "tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Epic epic = gson.fromJson(response.body(), Epic.class);
            assertEquals(epic, this.epic, "Полученная с сервера задача не соответствует тестовой");
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }


    @Test
    void takeTasks() {
        URI url = URI.create("http://localhost:8883/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
    }

    @Test
    void takeSubTasks() {
        URI url = URI.create(urlBase + "tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ArrayList<Subtask> testSubtasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Subtask>>() {
            }.getType());
            assertEquals(2, testSubtasks.size(), "Количество полученных задач не соответствуют менеджеру");
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void takeEpics() {
        URI url = URI.create(urlBase + "tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ArrayList<Epic> testEpics = gson.fromJson(response.body(), new TypeToken<ArrayList<Epic>>() {
            }.getType());
            assertEquals(1, testEpics.size(), "Количество полученных задач не соответствуют менеджеру");
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void takeEpicsTasks() {
        URI url = URI.create(urlBase + "tasks/subtask/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ArrayList<Subtask> testSubtasksEpic = gson.fromJson(response.body(), new TypeToken<ArrayList<Subtask>>() {
            }.getType());
            assertEquals(2, testSubtasksEpic.size(), "Количество полученных задач не соответствуют менеджеру");
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        taskManager.getTasks(task1.getid());
        taskManager.getTasks(task2.getid());
        URI url = URI.create(urlBase + "tasks/history/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ArrayList<Task> testHistory = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
            }.getType());
            assertEquals(2, testHistory.size(), "Количество полученных задач не соответствуют менеджеру");
            assertEquals(task1, testHistory.get(0), "Не соответствуют менеджеру");
            assertEquals(task2, testHistory.get(1), "Не соответствуют менеджеру");
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getPrioritizedTasks() {
        URI url = URI.create(urlBase + "tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Set<Task> priorTasks = gson.fromJson(response.body(), new TypeToken<Set<Task>>() {
            }.getType());
            assertNotNull(priorTasks, "Задачи по приоритету не сформированы");
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }

    }

    @Test
    void load() throws IOException, InterruptedException {
        taskManager.getTasks(task1.getid());
        taskManager.getSubTasks(subtask1.getid());
        taskManager.getEpics(epic.getid());
        taskManager.getTasks(task2.getid());
        HttpTaskManager taskManagerLoaded = new HttpTaskManager(8882, true);
        final List<Task> tasks = taskManagerLoaded.takeTasks();
        assertNotNull(tasks);
        assertEquals(2, tasks.size(), "Количество задач не соответствует восстановленному!");
        final List<Epic> epics = taskManagerLoaded.takeEpics();
        assertNotNull(epics);
        assertEquals(1, epics.size(), "Количество задач не соответствует восстановленному!");
        final List<Subtask> subtsaks = taskManagerLoaded.takeSubTasks();
        assertNotNull(subtsaks);
        assertEquals(2, subtsaks.size(), "Количество задач не соответствует восстановленному!");
        final List<Task> history = taskManagerLoaded.getHistory();
        assertNotNull(history);
        assertEquals(4, history.size(), "История восстановлена не в полном объеме!");
    }
}