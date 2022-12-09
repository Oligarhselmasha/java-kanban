package kanban.manager;

import kanban.tasks.Epic;
import kanban.tasks.Subtask;
import kanban.tasks.Task;
import kanban.tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int id = 1; // Изначальный id при старте - 1
    protected final Map<Integer, Task> tasks = new HashMap<>(); // Здесь хранятся все задачи, id - key
    protected final Map<Integer, Subtask> subTasks = new HashMap<>(); // Здесь хранятся все подзадачи, id - key
    protected final Map<Integer, Epic> epics = new HashMap<>(); // Здесь хранятся все эпики, id - key
    protected final HistoryManager historyManager = Managers.getDefaultHistory(); // При создании TaskManager создается HistoryManager
    protected final Set<Task> priorTasks = new TreeSet<>(new SortByStart());


    @Override
    public Task createTask(String title, String description, LocalDateTime startTime, long duration) { // Создание таска
        getPrioritizedTasks();
        Task task = new Task(title, description, startTime, duration);
        try {
            for (Task taskSorted : priorTasks) {
                if (task.getStartTime().isBefore(taskSorted.getStartTime()) &&
                        task.getEndTime().isAfter(taskSorted.getStartTime())) {
                    throw new IllegalStateException("Задачи пересекаются по времени, не удалось создать задачу!");
                }
                if (task.getStartTime().isAfter(taskSorted.getStartTime()) &&
                        task.getStartTime().isBefore(taskSorted.getEndTime())) {
                    throw new IllegalStateException("Задачи пересекаются по времени, не удалось создать задачу!");
                }
                if (task.getStartTime().isEqual(taskSorted.getStartTime())) {
                    throw new IllegalStateException("Задачи пересекаются по времени, не удалось создать задачу!");
                }
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return null;
        }
        task.setid(id++);
        task.setDescription(description);
        tasks.put(task.getid(), task);
        return task;
    }

    @Override
    public Subtask createSubTask(Task task, Epic epic) { // Создание поздадачи. Сабтаск возможно создать только на основе
        Subtask subtask = new Subtask(task); // имеющегося таска. Так же он не может существовать в отрыве от Эпика
        subtask.setid(id++);
        subtask.setEpicId(epic.getid());
        subTasks.put(subtask.getid(), subtask);
        epic.setSubTasksIds(subtask.getid()); // Добавили в эпик созданый сабтаск
        checkEpicTime(epic);
        epics.put(epic.getid(), epic); // Перезаписали хэшмапу
        return subtask;
    }

    @Override
    public Epic createEpic(String title, String description) { // Создание Эпика - задача, которая может содержать в себе
        Epic epic = new Epic(title, description); // подзадачи. Выполнение эпика возможно только при выполнении входящих
        epic.setid(id++); // в него подзадач
        epic.setDescription(description);
        epics.put(epic.getid(), epic);
        return epic;
    }

    @Override
    public void add(Epic epic, Subtask subtask) { // Добавление в ЭПИК подзадач
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
    public void updateSubTask(Subtask subtask, TaskStatus status) { // Обновление статуса подзадачи
        subtask.setTaskStatus(status); // Обновляем
        subTasks.put(subtask.getid(), subtask); // Перезаписываем
        TaskStatus epicStatus = checkEpicStatus(epics.get(subtask.getEpicId())); // Проверка статуса эпика, обновления
        epics.get(subtask.getEpicId()).setTaskStatus(epicStatus); // Установка статуса эпика
    }

    @Override
    public TaskStatus checkEpicStatus(Epic epic) { // Метод по проверке статуса эпика (К ПЕРЕРАБОТКЕ, лишние действия)
        if (epic.getSubTasksIds().isEmpty()) { // Если в эпике нет задач, то сразу ставим статус NEW
            epic.setTaskStatus(TaskStatus.NEW);
            return epic.getTaskStatus();
        }
        for (int tasksId : epic.getTasksIds()) { // Проверяем все сабтаски, входящие в эпик
            if (subTasks.get(tasksId).getTaskStatus() == TaskStatus.NEW && epic.getTaskStatus() == TaskStatus.NEW) {
                epic.setTaskStatus(TaskStatus.NEW); // Если все сабтаски имеют статус NEW, то и у эпика статус NEW
                continue;
            }
            if (subTasks.get(tasksId).getTaskStatus() == TaskStatus.DONE && epic.getTaskStatus() != TaskStatus.NEW) {
                epic.setTaskStatus(TaskStatus.DONE);
            } else epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
        return epic.getTaskStatus();
    }

    @Override
    public void checkEpicTime(Epic epic) {
        LocalDateTime startTime = LocalDateTime.MAX;
        LocalDateTime endTime = LocalDateTime.MIN;
        long duration = 0;
        for (int id : epic.getSubTasksIds()) {
            if (subTasks.get(id).getStartTime().isBefore(startTime)) {
                startTime = subTasks.get(id).getStartTime();
                epic.setStartTime(startTime);
            }
            if (subTasks.get(id).getEndTime().isAfter(endTime)) {
                endTime = subTasks.get(id).getEndTime();
                epic.setEndTime(endTime);
            }
            duration += subTasks.get(id).getDuration();
        }
        epic.setDuration(duration);
    }

    @Override
    public List<Task> takeTasks() { // Получение списка всех задач
        ArrayList<Task> takeTasks = new ArrayList<>();
        for (Integer integer : tasks.keySet()) {
            takeTasks.add(tasks.get(integer));
        }
        return takeTasks;
    }

    @Override
    public List<Subtask> takeSubTasks() { // Получение списка всех подзадач
        ArrayList<Subtask> takeTasks = new ArrayList<>();
        for (Integer integer : subTasks.keySet()) {
            takeTasks.add(subTasks.get(integer));
        }
        return takeTasks;
    }

    @Override
    public List<Epic> takeEpics() { // Получение списка всех эпиков
        ArrayList<Epic> takeTasks = new ArrayList<>();
        for (Integer integer : epics.keySet()) {
            takeTasks.add(epics.get(integer));
        }
        return takeTasks;
    }

    @Override
    public void deliteTasks() { // Удаление всех задач
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();

    }

    @Override
    public void deliteSubTasks() { // Удаление всех поцзадач
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clear();
            checkEpicStatus(epic);
            epic.setStart(null);
            epic.setEndTime(null);
        }
    }

    @Override
    public void deliteEpics() { // Удаление всех эпиков
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
    }

    @Override
    public void deliteTasksId(int id) { // Удаление задач по id
        if (!tasks.containsKey(id)) {
            throw new IllegalStateException("Недопустимый id.");
        }
        for (Integer integer : tasks.keySet()) {
            if (integer == id) {
                tasks.remove(id);
                break;
            }
        }
        historyManager.remove(id);
    }

    @Override
    public void deliteSubTasksId(int id) { // Удаление подзадач по id
        if (!subTasks.containsKey(id)) {
            throw new IllegalStateException("Недопустимый id.");
        }
        int epicId = subTasks.get(id).getEpicId(); // Пока не удалили задачу, сохраним номер эпика
        epics.get(epicId).deliteById(id);
        checkEpicTime(epics.get(epicId));
        for (Integer integer : subTasks.keySet()) {
            if (integer == id) {
                historyManager.remove(id);
                subTasks.remove(integer);
                break;
            }
        }
        TaskStatus epicStatus = checkEpicStatus(epics.get(epicId));
        epics.get(epicId).setTaskStatus(epicStatus);
    }

    @Override
    public void deliteEpicsId(int id) { // Удаление эпиков по id
        if (!epics.containsKey(id)) {
            throw new IllegalStateException("Недопустимый id.");
        }
        Epic epic = epics.remove(id);
        if (epic == null) {
            return;
        }
        historyManager.remove(id);
        for (int subTasksId : epic.getSubTasksIds()) {
            subTasks.remove(subTasksId);
            historyManager.remove(subTasksId);
        }
    }

    @Override
    public List<Subtask> takeEpicsTasks(Epic epic) { // Получение списка задач определнного эпика
        ArrayList<Subtask> takeTasks = new ArrayList<>();
        for (Integer tasksId : epic.getTasksIds()) {
            takeTasks.add(subTasks.get(tasksId));
        }
        return takeTasks;
    }

    @Override
    public Task getTasks(int id) { // Получение таска по id
        Task task = tasks.get(id);
        historyManager.addTask(task); // Здесь и далее метод добавления таска в линкед список менеджера истории
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
    public List<Task> getHistory() { // Получение списка задач
        return historyManager.getHistory();
    }

    @Override
    public void clearHistory() {
        historyManager.clearHistory();
    }


    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        priorTasks.clear();
        priorTasks.addAll(tasks.values());
        priorTasks.addAll(subTasks.values());
        return priorTasks;
    }

    static class SortByStart implements Comparator<Task> {
        public int compare(Task a, Task b) {
            if (a.getStartTime().isBefore(b.getStartTime())) {
                return -1;
            } else if (a.getStartTime().isEqual(b.getStartTime())) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
