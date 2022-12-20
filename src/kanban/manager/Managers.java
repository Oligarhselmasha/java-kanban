package kanban.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kanban.server.HttpTaskManager;

import java.io.File;
import java.io.IOException;


public class Managers {
    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager(new File("resources/task.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
//        return gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        return gsonBuilder.create();
    }

    final static Gson gson = getGson();
}

