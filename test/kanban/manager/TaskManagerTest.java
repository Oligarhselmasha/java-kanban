package kanban.manager;

import kanban.manager.TaskManager;
import kanban.tasks.Epic;
import kanban.tasks.Subtask;
import kanban.tasks.Task;
import kanban.tasks.TaskStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    protected void initTasks(){ // Инициализируем таски

    }
    @Test
    Task createTask(String title, String description);

    Subtask createSubTask(Task task, Epic epic);

    Epic createEpic(String title, String description);

    void add(Epic epic, Subtask subtask);

    void updateTask(Task task, TaskStatus status);

    void updateSubTask(Subtask subtask, TaskStatus status);

    TaskStatus checkEpicStatus(Epic epic);

    List takeTasks();

    List takeSubTasks();

    List takeEpics();

    void deliteTasks();

    void deliteSubTasks();

    void deliteEpics();

    void deliteTasksId(int id);

    void deliteSubTasksId(int id);

    void deliteEpicsId(int id);

    List takeEpicsTasks(Epic epic);

    Task getTasks(int id);

    Subtask getSubTasks(int id);

    Epic getEpics(int id);

    List<Task> getHistory();

    public void clearHistory ();

}
