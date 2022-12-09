package kanban.manager;

import kanban.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private File file;

    @BeforeEach
    public void setUp() {
        file = new File("resources/test.csv"); // В начале каждого теста создается новый файл
        taskManager = new FileBackedTasksManager(file); // Который является полем нового менеджера
        initTasks(); // Инициализируем таски
    }
    @Test
    public void loadFromFile() { // Получаем информацию из файла
        FileBackedTasksManager fileBackedTasksManager2 = taskManager.loadFromFile(file);
        final List<Task> tasks = fileBackedTasksManager2.takeTasks();
        assertNotNull(tasks, "Список задач пустой");
    }

    @Test
    public void emptyTaskListTest() throws IOException { // Проверка работы по сохранению и восстановлению состояния - Пустой список задач.
        taskManager.deliteTasks();
        taskManager.deliteSubTasks();
        taskManager.deliteEpics();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        reader.readLine();
        String line = reader.readLine();
        assertTrue(line.isEmpty(), "Вторая строчка не пустая.");
    }

    @Test
    public void notEmptyTaskListTest() throws IOException { // Проверка работы по сохранению и восстановлению состояния - Пустой список задач.
        taskManager.deliteSubTasks();
        taskManager.deliteEpics();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        reader.readLine();
        String line = reader.readLine();
        assertFalse(line.isEmpty(), "Вторая строчка пустая.");
    }

    @Test
    public void historyListTest() throws IOException { // Проверка работы по сохранению и восстановлению состояния - Не пустой список истории.
        taskManager.getTasks(1);
        taskManager.getEpics(3);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String history = reader.readLine();
        while (reader.ready()) {
            if (!history.isEmpty()) {
                history = reader.readLine();
            } else {
                history = reader.readLine();
            }
        }
        assertEquals("1, 3, ", history, "История считана неверно.");
    }
    @Test
    public void emptyHistoryListTest() throws IOException { // Проверка работы по сохранению и восстановлению состояния - Пустой список истории.
        taskManager.getTasks(1);
        taskManager.getEpics(3);
        taskManager.clearHistory();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String history = reader.readLine();
        while (reader.ready()) {
            if (!history.isEmpty()) {
                history = reader.readLine();
            } else {
                history = reader.readLine();
            }
        }
        assertEquals("","", "История считана неверно.");
    }
}