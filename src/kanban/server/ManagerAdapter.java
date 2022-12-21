package kanban.server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ManagerAdapter extends TypeAdapter<HttpTaskManager> {

    @Override
    public void write(JsonWriter jsonWriter, HttpTaskManager httpTaskManager) throws IOException {
        jsonWriter.jsonValue(httpTaskManager.toString());
    }

    @Override
    public HttpTaskManager read(JsonReader jsonReader) throws IOException {

        return null;
    }
}