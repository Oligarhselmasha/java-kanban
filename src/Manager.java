import java.util.HashMap;

public class Manager {

    private int id = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public Task create (String title, String description){
        Task task = new Task(title, description);
        return task;
    }

    public void add(Task task){
        task.setTaskId(id++);
        tasks.put(task.getTaskId(), task);
    }

    public void add(Subtask subtask){
        subtask.setTaskId(id++);
        subTasks.put(subtask.getTaskId(), subtask);
    }
    public void add(Epic epic){
        epic.setTaskId(id++);
        epics.put(epic.getTaskId(), epic);
    }
    public void update(Task task){

    }
    public void update(Subtask subtask){

    }
}
