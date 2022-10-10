import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    final List<Task> lastids = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (lastids.size() == 10) {
            lastids.remove(0);
        }
        lastids.add(task);
    }

    @Override
    public List<Task> getHistory() { // Получение списка последних задач
        return lastids;
    }
}
