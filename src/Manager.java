import java.util.HashMap;

public class Manager {

    private int id = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>(); // Здесь хранятся все задачи
    private HashMap<Integer, Subtask> subTasks = new HashMap<>(); // Здесь хранятся подзадачи
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public void createTask (String title, String description){ // Создание таска
        Task task = new Task(title, description);
        task.setTaskStatus("NEW");
        task.setTaskId(id++);
        task.setDescription(description);
        tasks.put(task.getTaskId(), task);
    }
    public void createSubTask (String title, String description){ // Создание поздадач
        Subtask subtask = new Subtask(title, description);
        subtask.setTaskStatus("NEW");
        subtask.setTaskId(id++);
        subtask.setDescription(description);
        subTasks.put(subtask.getTaskId(), subtask);
    }
    public void createEpic (String title, String description){ //Создание Эпика
        Epic epic = new Epic(title, description);
        epic.setTaskStatus("NEW");
        epic.setTaskId(id++);
        epic.setDescription(description);
        epics.put(epic.getTaskId(), epic);
    }
    public void add(Task task, Subtask subtask){ // Добавление в подзадачу задач
        subtask.setTasksIds(task.getTaskId()); // Добавление в подзадачу задач
        checkSubTaskStatus(subtask);
        subTasks.put(subtask.getTaskId(), subtask);
    }

    public void add(Epic epic, Subtask subtask){ // Добавление в эпик подзадачаи
        subtask.setEpicId(epic.getTaskId()); // Сообщаем к какому эпику относится подзадача
        epic.setSubTasksIds(subtask.getTaskId()); // Добавление подзадачи в эпике
        checkEpicStatus(epic);
        epics.put(epic.getTaskId(), epic);
    }
    public void updateTask(Task task, String description, String status){
        task.setDescription(description);
        task.setTaskStatus(status);
        tasks.put(task.getTaskId(), task);
    }
    public void updateSubTask(Subtask subtask, String description, String status){
        subtask.setDescription(description);
        subtask.setTaskStatus(status);
        tasks.put(subtask.getTaskId(), subtask);
    }
    public void updateEpic(Epic epic, String description, String status){
        epic.setDescription(description);
        epic.setTaskStatus(status);
        tasks.put(epic.getTaskId(), epic);
    }
    public void checkSubTaskStatus (Subtask subtask) {
        if (subtask.tasksIds.isEmpty()){
            subtask.setTaskStatus("NEW");
        }
        else subtask.setTaskStatus("IN_PROGRESS");
        for (int tasksId : subtask.getTasksIds()) { // Проверка статуса
            if (tasks.get(tasksId).getTaskStatus() == "NEW"){
                subtask.setTaskStatus("NEW");
                continue;
            }
            if (tasks.get(tasksId).getTaskStatus() == "DONE" && tasks.get(tasksId).getTaskStatus() != "NEW"){
                subtask.setTaskStatus("DONE");
            }
            else subtask.setTaskStatus("IN_PROGRESS");
        }
    }
    public void checkEpicStatus (Epic epic) {
        if (epic.subTasksIds.isEmpty()){
            epic.setTaskStatus("NEW");
        }
        else epic.setTaskStatus("IN_PROGRESS");
        for (int tasksId : epic.getTasksIds()) {
            if (subTasks.get(tasksId).getTaskStatus() == "NEW") {
                epic.setTaskStatus("NEW");
                continue;
            }
            if (subTasks.get(tasksId).getTaskStatus() == "DONE" && epic.getTaskStatus() != "NEW") {
                epic.setTaskStatus("DONE");
            }
            else epic.setTaskStatus("IN_PROGRESS");
        }
    }
}
