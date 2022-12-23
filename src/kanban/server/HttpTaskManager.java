package kanban.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kanban.manager.*;
import kanban.tasks.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private KVTaskClient kvTaskClient;

    private static Gson gsonManager;

    public HttpTaskManager(int port) throws IOException, InterruptedException {
        this(port, false);
    }

    public HttpTaskManager(int port, boolean loads) throws IOException, InterruptedException {
        super(null);
        kvTaskClient = new KVTaskClient(port);
        gsonManager = new GsonBuilder().registerTypeAdapter(HttpTaskManager.class, new ManagerAdapter()).create();
        if (loads) {
            load();
        }
    }

    protected void addTasks(List<Task> tasks) {
        int id = 0;
        for (Task task : tasks) {
            TaskType type = task.getTaskType();
            if (task.getid() > id) {
                id = task.getid();
            }
            switch (type) {
                case TASK:
                    this.tasks.put(id, task);
                    break;
                case SUBTASK:
                    this.subTasks.put(id, (Subtask) task);
                    break;
                case EPIC:
                    this.epics.put(id, (Epic) task);
                    break;
            }
        }
        getPrioritizedTasks();
    }

    public void load() throws IOException, InterruptedException {
        ArrayList<Task> tasks = gsonManager.fromJson(kvTaskClient.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(tasks);

        ArrayList<Task> subtasks = gsonManager.fromJson(kvTaskClient.load("subtasks"), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        addTasks(subtasks);

        ArrayList<Task> epics = gsonManager.fromJson(kvTaskClient.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        addTasks(epics);

        ArrayList<Task> history = gsonManager.fromJson(kvTaskClient.load("history"), new TypeToken<ArrayList<Task>>() {
        }.getType());

        for (Task task : history) {
            historyManager.addTask(task);
        }
    }

    @Override
    public void save() throws IOException, InterruptedException {
        String jsonTasks = gsonManager.toJson(new ArrayList<>(tasks.values()));
        kvTaskClient.put("tasks", jsonTasks);

        String jsonSubtasks = gsonManager.toJson(new ArrayList<>(subTasks.values()));
        kvTaskClient.put("subtasks", jsonSubtasks);

        String jsonEpics = gsonManager.toJson(new ArrayList<>(epics.values()));
        kvTaskClient.put("epics", jsonEpics);

        String jsonHistory = gsonManager.toJson(getHistory());
        kvTaskClient.put("history", jsonHistory);
    }
}


