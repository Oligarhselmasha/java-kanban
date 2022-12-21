package kanban.server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String uri;
    private final String apiToken;
    private final int PORT = 8882;



    public KVTaskClient() throws IOException, InterruptedException {
        this.uri = "http://localhost:" + PORT + "/";
        this.apiToken = register(uri);
    }

    private String register(String uri) throws IOException, InterruptedException {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri + "register")).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException();
            }
            return response.body();
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка при регистрации");
        }
    }

    public String load(String uri, String key) throws IOException, InterruptedException {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            String uriLoad = String.format("%sload/%s?API_TOKEN=%s", uri, key, apiToken);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uriLoad)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException();
            }
            return response.body();
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка при загрузке с сервера");
        }
    }

    public void put (String key, String value) throws IOException, InterruptedException {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            String uriPut = String.format("%ssave/%s?API_TOKEN=%s", uri, key, apiToken);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uriPut)).POST(HttpRequest.BodyPublishers.ofString(value)).build();
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() != 200) {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка при передачи данных на сервер");
        }
    }
}
