package kanban.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kanban.server.HttpTaskManager;

import java.io.IOException;


public class Managers {
    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager(8882);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }
}

