package kanban.server;

import com.google.gson.Gson;
import kanban.manager.*;
import kanban.tasks.Subtask;
import kanban.tasks.Task;
import kanban.tasks.TaskStatus;

import java.io.File;
import java.io.IOException;

public class HttpTaskManager extends FileBackedTasksManager {

    KVTaskClient kvTaskClient;
    private final String key = "id1";
//    Gson gsonTask = Managers.getGson();


    public HttpTaskManager(File file) throws IOException, InterruptedException {
        super(file);
        kvTaskClient = new KVTaskClient();
    }


    @Override
    public void deliteTasks() throws IOException, InterruptedException {
        super.deliteTasks();
        saveToServer();
    }

    @Override
    public void deliteSubTasks() throws IOException, InterruptedException {
        super.deliteSubTasks();
        saveToServer();
    }

    @Override
    public void deliteEpics() throws IOException, InterruptedException {
        super.deliteEpics();
        saveToServer();
    }

    @Override
    public void deliteTasksId(int id) throws IllegalStateException, IOException, InterruptedException {
        super.deliteTasksId(id);
        saveToServer();
    }

    @Override
    public void deliteSubTasksId(int id) throws IOException, InterruptedException {
        super.deliteSubTasksId(id);
        saveToServer();
    }

    @Override
    public void deliteEpicsId(int id) throws IOException, InterruptedException {
        super.deliteEpicsId(id);
        saveToServer();
    }


    @Override
    public void updateTask(Task task, TaskStatus status) throws IOException, InterruptedException {
        super.updateTask(task, status);
        saveToServer();
    }

    @Override
    public void updateSubTask(Subtask subtask, TaskStatus status) throws IOException, InterruptedException {
        super.updateSubTask(subtask, status);
        saveToServer();
    }


    @Override
    public Task getTasks(int id) throws IOException, InterruptedException {
        Task task = super.getTasks(id);
//        saveToServer();
        return task;
    }


    @Override
    public void addTask(Task task) throws IOException, InterruptedException {
        super.addTask(task);
        saveToServer();
    }

    private void saveToServer() throws IOException, InterruptedException {
        String httpTaskManager = Managers.getGson().toJson(this, HttpTaskManager.class);
        kvTaskClient.put(key, httpTaskManager);
    }
}
