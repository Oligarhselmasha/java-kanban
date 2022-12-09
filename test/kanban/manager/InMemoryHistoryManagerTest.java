package kanban.manager;

import kanban.tasks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    protected HistoryManager historyManager;
    protected Epic epic;
    protected Subtask subtask;
    protected Task task;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Test task", 1, TaskStatus.NEW, "Test task description", TaskType.TASK,
                LocalDateTime.of(2022, 12, 9, 13, 00), 30);
        epic = new Epic("Test epic", 2, TaskStatus.NEW, "Test epic description", TaskType.EPIC,
                LocalDateTime.of(2022, 12, 9, 13, 00), 30);
        subtask = new Subtask("Test subtask", 3, TaskStatus.NEW, "Test subtask description",
                TaskType.SUBTASK, epic.getid(),
                LocalDateTime.of(2022, 12, 9, 13, 00), 30);

    }

    @Test
    void removeBegin() {  // Удаление из начала
        historyManager.addTask(task);
        historyManager.addTask(epic);
        historyManager.addTask(subtask);
        historyManager.remove(1);
        List<Task> tasks = Arrays.asList(epic, subtask);
        assertEquals(tasks, historyManager.getHistory());
    }

    @Test
    void removeMiddle() {  // Удаление из середины
        historyManager.addTask(task);
        historyManager.addTask(epic);
        historyManager.addTask(subtask);
        historyManager.remove(2);
        List<Task> tasks = Arrays.asList(task, subtask);
        assertEquals(tasks, historyManager.getHistory());
    }

    @Test
    void removeEnd() {  // Удаление из конца
        historyManager.addTask(task);
        historyManager.addTask(epic);
        historyManager.addTask(subtask);
        historyManager.remove(3);
        List<Task> tasks = Arrays.asList(task, epic);
        assertEquals(tasks, historyManager.getHistory());
    }

    @Test
    void addTask() { // Тест на добавление
        historyManager.addTask(task);
        historyManager.addTask(subtask);
        historyManager.addTask(epic);
        List<Task> tasks = Arrays.asList(task, subtask, epic);
        assertEquals(tasks, historyManager.getHistory());
    }

    @Test
    void getHistoryEmpty() { // Получение пустой история задач
        int historySize = historyManager.getHistory().size();
        assertEquals(0, historySize, "История не пуста");
    }

    @Test
    void getHistoryDouble() { // Дублирование
        historyManager.addTask(task);
        historyManager.addTask(task);
        historyManager.addTask(epic);
        historyManager.addTask(epic);
        historyManager.addTask(subtask);
        historyManager.addTask(subtask);
        List<Task> tasks = Arrays.asList(task, epic, subtask);
        assertEquals(tasks, historyManager.getHistory());
    }

    @Test
    void clearHistory() { // Тест на очистку истории
        historyManager.addTask(task);
        historyManager.addTask(epic);
        historyManager.addTask(subtask);
        historyManager.clearHistory();
        int historySize = historyManager.getHistory().size();
        assertEquals(0, historySize, "История не пуста");
    }
}