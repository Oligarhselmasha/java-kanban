package kanban.manager;

import kanban.tasks.Epic;
import kanban.tasks.Subtask;
import kanban.tasks.Task;
import kanban.tasks.TaskStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface TaskManager {
//    Task createTask(String title, String description);

    Task createTask(String title, String description, LocalDateTime startTime, long duration) throws IOException, InterruptedException;

    Subtask createSubTask(Task task, Epic epic) throws IOException, InterruptedException;

    Epic createEpic(String title, String description) throws IOException, InterruptedException;

    void add(Epic epic, Subtask subtask) throws IOException, InterruptedException;

    void updateTask(Task task, TaskStatus status) throws IOException, InterruptedException;

    void updateSubTask(Subtask subtask, TaskStatus status) throws IOException, InterruptedException;

    TaskStatus checkEpicStatus(Epic epic);

    List<Task> takeTasks();

    List<Subtask> takeSubTasks();

    List<Epic> takeEpics();

    void deliteTasks() throws IOException, InterruptedException;

    void deliteSubTasks() throws IOException, InterruptedException;

    void deliteEpics() throws IOException, InterruptedException;

    void deliteTasksId(int id) throws IOException, InterruptedException;

    void deliteSubTasksId(int id) throws IOException, InterruptedException;

    void deliteEpicsId(int id) throws IOException, InterruptedException;

    List takeEpicsTasks(Epic epic);

    Task getTasks(int id) throws IOException, InterruptedException;

    Subtask getSubTasks(int id) throws IOException, InterruptedException;

    Epic getEpics(int id) throws IOException, InterruptedException;

    List<Task> getHistory();

    void clearHistory () throws IOException, InterruptedException;

    void setId(int id);

    void checkEpicTime(Epic epic);
    Set<Task> getPrioritizedTasks();

    public void addTask(Task task) throws IOException, InterruptedException;

    List<Subtask> takeEpicsTasksById(int id);



}
