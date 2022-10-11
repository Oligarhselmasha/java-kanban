package kanban.manager;

import kanban.tasks.Task;

import java.util.List;

public interface HistoryManager {

   void addTask (Task task);
   List<Task> getHistory();
}
