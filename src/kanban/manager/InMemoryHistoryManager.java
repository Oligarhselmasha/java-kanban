package kanban.manager;

import kanban.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    final static LinkedList<Task> lastids = new LinkedList<>();
    final static int MAX_VALUE = 10;

    @Override
    public void addTask(Task task) {
        if (lastids.size() == MAX_VALUE) {
            lastids.removeFirst();
        }
        lastids.addLast(task);
    }

    @Override
    public List<Task> getHistory() { // Получение списка последних задач
        return lastids;
    }
}
