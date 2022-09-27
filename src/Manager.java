import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private int id = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>(); // Здесь хранятся все задачи
    private HashMap<Integer, Subtask> subTasks = new HashMap<>(); // Здесь хранятся все подзадачи
    private HashMap<Integer, Epic> epics = new HashMap<>(); //Здесь хранятся все эпики

    public Task createTask(String title, String description) { // Создание таска
        Task task = new Task(title, description);
        task.setid(id++);
        task.setDescription(description);
        tasks.put(task.getid(), task);
        return task;
    }

    public Subtask createSubTask(Task task, Epic epic) { // Создание поздадачи
        Subtask subtask = new Subtask(task);
        subtask.setid(id++);
        subtask.setEpicId(epic.getid());
        subTasks.put(subtask.getid(), subtask);
        return subtask;
    }

    public Epic createEpic(String title, String description) { //Создание Эпика
        Epic epic = new Epic(title, description);
        epic.setid(id++);
        epic.setDescription(description);
        epics.put(epic.getid(), epic);
        return epic;
    }

    public void add(Epic epic, Subtask subtask) { // Добавление в эпик подзадач
        subtask.setEpicId(epic.getid()); // Сообщаем к какому эпику относится подзадача
        epic.setSubTasksIds(subtask.getid()); // Добавление подзадачи в эпик
        String status = checkEpicStatus(epic); // Проверка статуса
        epic.setTaskStatus(status); // Установка статуса
        epics.put(epic.getid(), epic); // Добавляем эпик в мапу эпиков
    }

    public void updateTask(Task task, String status) { // Обновляем статус задачи
        task.setTaskStatus(status);
        tasks.put(task.getid(), task);
    }

    public void updateSubTask(Subtask subtask, String status) { // Обновляем статус подзадачи
        subtask.setTaskStatus(status);
        subTasks.put(subtask.getid(), subtask);
        String epicStatus = checkEpicStatus(epics.get(subtask.getEpicId())); // Проверка статуса эпика, после добавления
        epics.get(subtask.getEpicId()).setTaskStatus(epicStatus); // Установка статуса эпика
    }

    public String checkEpicStatus(Epic epic) { // Метод по проверке статуса эпика
        if (epic.subTasksIds.isEmpty()) {
            epic.setTaskStatus("NEW");
        } else epic.setTaskStatus("IN_PROGRESS");
        for (int tasksId : epic.getTasksIds()) {
            if (subTasks.get(tasksId).getTaskStatus() == "NEW" && epic.getTaskStatus() != "DONE") {
                epic.setTaskStatus("NEW");
                continue;
            }
            if (subTasks.get(tasksId).getTaskStatus() == "DONE" && epic.getTaskStatus() != "NEW") {
                epic.setTaskStatus("DONE");
            } else epic.setTaskStatus("IN_PROGRESS");
        }
        return epic.getTaskStatus();
    }

    public ArrayList takeTasks() { // Получение списка всех задач
        ArrayList<Task> takeTasks = new ArrayList<>();
        for (Integer integer : tasks.keySet()) {
            takeTasks.add(tasks.get(integer));
        }
        return takeTasks;
    }

    public ArrayList takeSubTasks() { // Получение списка всех подзадач
        ArrayList<Subtask> takeTasks = new ArrayList<>();
        for (Integer integer : subTasks.keySet()) {
            takeTasks.add(subTasks.get(integer));
        }
        return takeTasks;
    }

    public ArrayList takeEpics() { // Получение списка всех эпиков
        ArrayList<Epic> takeTasks = new ArrayList<>();
        for (Integer integer : epics.keySet()) {
            takeTasks.add(epics.get(integer));
        }
        return takeTasks;
    }

    public void deliteTasks() { // Удаление всех задач
        tasks.clear();
    }

    public void deliteSubTasks() { // Удаление всех поцзадач
        subTasks.clear();
    }

    public void deliteEpics() { // Удаление всех эпиков
        epics.clear();
    }

    public void deliteTasksId(int id) { // Удаление задач по id

        for (Integer integer : tasks.keySet()) {
            if (integer == id) {
                tasks.remove(id);
            }
        }
    }

    public void deliteSubTasksId(int id) { // Удаление подзадач по id
        int epicId = subTasks.get(id).getEpicId(); // Пока не удалили задачу, сохраним номер эпика
        epics.get(epicId).deliteById(id);
        for (Integer integer : subTasks.keySet()) {
            if (integer == id) {
                subTasks.remove(integer);
                break;
            }
        }
        String epicStatus = checkEpicStatus(epics.get(epicId));
        epics.get(epicId).setTaskStatus(epicStatus);
    }

    public void deliteEpicsId(int id) { // Удаление эпиков по id

        for (Integer integer : epics.keySet()) {
            if (integer == id) {
                epics.remove(id);
                break;
            }
        }
    }

    public ArrayList takeEpicsTasks(Epic epic) { // Получение списка задач определнного эпика
        ArrayList<Subtask> takeTasks = new ArrayList<>();
        for (Integer tasksId : epic.getTasksIds()) {
            takeTasks.add(subTasks.get(tasksId));
        }
        return takeTasks;
    }
}
