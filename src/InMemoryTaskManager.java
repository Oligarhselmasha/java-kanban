import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int id = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>(); // Здесь хранятся все задачи
    private final HashMap<Integer, Subtask> subTasks = new HashMap<>(); // Здесь хранятся все подзадачи
    private final HashMap<Integer, Epic> epics = new HashMap<>(); //Здесь хранятся все эпики
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Task createTask(String title, String description) { // Создание таска
        Task task = new Task(title, description);
        task.setid(id++);
        task.setDescription(description);
        tasks.put(task.getid(), task);
        return task;
    }

    @Override
    public Subtask createSubTask(Task task, Epic epic) { // Создание поздадачи
        Subtask subtask = new Subtask(task);
        subtask.setid(id++);
        subtask.setEpicId(epic.getid());
        subTasks.put(subtask.getid(), subtask);
        return subtask;
    }

    @Override
    public Epic createEpic(String title, String description) { //Создание Эпика
        Epic epic = new Epic(title, description);
        epic.setid(id++);
        epic.setDescription(description);
        epics.put(epic.getid(), epic);
        return epic;
    }

    @Override
    public void add(Epic epic, Subtask subtask) { // Добавление в эпик подзадач
        subtask.setEpicId(epic.getid()); // Сообщаем к какому эпику относится подзадача
        epic.setSubTasksIds(subtask.getid()); // Добавление подзадачи в эпик
        TaskStatus status = checkEpicStatus(epic); // Проверка статуса
        epic.setTaskStatus(status); // Установка статуса
        epics.put(epic.getid(), epic); // Добавляем эпик в мапу эпиков
    }

    @Override
    public void updateTask(Task task, TaskStatus status) { // Обновляем статус задачи
        task.setTaskStatus(status);
        tasks.put(task.getid(), task);
    }

    @Override
    public void updateSubTask(Subtask subtask, TaskStatus status) { // Обновляем статус подзадачи
        subtask.setTaskStatus(status);
        subTasks.put(subtask.getid(), subtask);
        TaskStatus epicStatus = checkEpicStatus(epics.get(subtask.getEpicId())); // Проверка статуса эпика, после добавления
        epics.get(subtask.getEpicId()).setTaskStatus(epicStatus); // Установка статуса эпика
    }

    @Override
    public TaskStatus checkEpicStatus(Epic epic) { // Метод по проверке статуса эпика
        if (epic.subTasksIds.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        for (int tasksId : epic.getTasksIds()) {
            if (subTasks.get(tasksId).getTaskStatus() == TaskStatus.NEW && epic.getTaskStatus() != TaskStatus.DONE) {
                epic.setTaskStatus(TaskStatus.NEW);
                continue;
            }
            if (subTasks.get(tasksId).getTaskStatus() == TaskStatus.DONE && epic.getTaskStatus() != TaskStatus.NEW) {
                epic.setTaskStatus(TaskStatus.DONE);
            } else epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
        return epic.getTaskStatus();
    }

    @Override
    public ArrayList takeTasks() { // Получение списка всех задач
        ArrayList<Task> takeTasks = new ArrayList<>();
        for (Integer integer : tasks.keySet()) {
            takeTasks.add(tasks.get(integer));
        }
        return takeTasks;
    }

    @Override
    public ArrayList takeSubTasks() { // Получение списка всех подзадач
        ArrayList<Subtask> takeTasks = new ArrayList<>();
        for (Integer integer : subTasks.keySet()) {
            takeTasks.add(subTasks.get(integer));
        }
        return takeTasks;
    }

    @Override
    public ArrayList takeEpics() { // Получение списка всех эпиков
        ArrayList<Epic> takeTasks = new ArrayList<>();
        for (Integer integer : epics.keySet()) {
            takeTasks.add(epics.get(integer));
        }
        return takeTasks;
    }

    @Override
    public void deliteTasks() { // Удаление всех задач
        tasks.clear();
    }

    @Override
    public void deliteSubTasks() { // Удаление всех поцзадач
        subTasks.clear();
    }

    @Override
    public void deliteEpics() { // Удаление всех эпиков
        epics.clear();
    }

    @Override
    public void deliteTasksId(int id) { // Удаление задач по id
        for (Integer integer : tasks.keySet()) {
            if (integer == id) {
                tasks.remove(id);
            }
        }
    }

    @Override
    public void deliteSubTasksId(int id) { // Удаление подзадач по id
        int epicId = subTasks.get(id).getEpicId(); // Пока не удалили задачу, сохраним номер эпика
        epics.get(epicId).deliteById(id);
        for (Integer integer : subTasks.keySet()) {
            if (integer == id) {
                subTasks.remove(integer);
                break;
            }
        }
        TaskStatus epicStatus = checkEpicStatus(epics.get(epicId));
        epics.get(epicId).setTaskStatus(epicStatus);
    }

    @Override
    public void deliteEpicsId(int id) { // Удаление эпиков по id

        for (Integer integer : epics.keySet()) {
            if (integer == id) {
                epics.remove(id);
                break;
            }
        }
    }

    @Override
    public ArrayList takeEpicsTasks(Epic epic) { // Получение списка задач определнного эпика
        ArrayList<Subtask> takeTasks = new ArrayList<>();
        for (Integer tasksId : epic.getTasksIds()) {
            takeTasks.add(subTasks.get(tasksId));
        }
        return takeTasks;
    }

    @Override
    public Task getTasks(int id) { // Получение таска по id
        Task task = tasks.get(id);
        historyManager.addTask(task); // Здесь и далее метод добавления таска в список
        return task;
    }

    @Override
    public Subtask getSubTasks(int id) { // Получение сабтаска по id
        Subtask subtask = subTasks.get(id);
        historyManager.addTask(subtask);
        return subtask;
    }

    @Override
    public Epic getEpics(int id) { // Получение эпика по id
        Epic epic = epics.get(id);
        historyManager.addTask(epic);
        return epic;
    }

    @Override
    public List<Task> getHistory() { // Получение списка 10 последних задач
        return historyManager.getHistory();
    }
}
