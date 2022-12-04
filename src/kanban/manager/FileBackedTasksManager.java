package kanban.manager;

import kanban.tasks.Epic;
import kanban.tasks.Subtask;
import kanban.tasks.Task;
import kanban.tasks.TaskStatus;

import java.io.*;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        File file = new File("resources/task.csv");

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.createTask("1 задача", "Обычная задача"); // id1
        fileBackedTasksManager.createTask("2 задача", "Обычная задача"); // id2
        fileBackedTasksManager.createEpic("1 эпик", "1 Эпик"); // id3
        fileBackedTasksManager.createSubTask(fileBackedTasksManager.getTasks(1), fileBackedTasksManager.getEpics(3)); // id4
        fileBackedTasksManager.createSubTask(fileBackedTasksManager.getTasks(2), fileBackedTasksManager.getEpics(3)); // id5
        fileBackedTasksManager.clearHistory(); // Запускаем чтобы очистить из памяти запросы для создания сабтаски
        fileBackedTasksManager.getTasks(1);
        fileBackedTasksManager.getSubTasks(4);
        fileBackedTasksManager.getEpics(3);

        System.out.println(fileBackedTasksManager.getHistory()); // Вызвали историю первого

        FileBackedTasksManager fileBackedTasksManager1 = FileBackedTasksManager.loadFromFile(file);

        System.out.println(fileBackedTasksManager1.getHistory()); // Соответсвует восстановленному
    }

    public static FileBackedTasksManager loadFromFile(File file) { // Получаем информацию из файла
        final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) { // Создали ридер
            reader.readLine(); // Считали первую "ненужную" строчку
            while (reader.ready()) { // Пока ридер готов...
                int generatedId = 0; // Значение id в менеджере
                String line = reader.readLine();
                if (!line.isEmpty()) { // ...наполняем мапы тасками, сабтасками и эпиками
                    Task task = CSVTaskFormat.fromString(line);
                    addTask(task);
                    if (task.getid() > generatedId) {
                        generatedId = task.getid();
                        id = generatedId;
                    }
                }
                else break;
            }
            for (Integer id : subTasks.keySet()) { // Узнаем у каждого сабтаска к какому эпику он относится
                int epicId = subTasks.get(id).getEpicId();
                epics.get(epicId).setSubTasksIds(id); // Добавляем эпик в сабтаску
            }
            reader.readLine(); // Считали пустую строку

            while (reader.ready()) { // Пока ридер готов...
                String line = reader.readLine();
                if (!line.isEmpty()) {
                    List<Integer> list = CSVTaskFormat.historyFromString(line); // Получили лист из строки с историей
                    for (Integer id : list) { // Поочередно добавляем каждую задачу в хистори менеджер
                        if (tasks.containsKey(id)) {
                            getHistoryManager().addTask(tasks.get(id));
                        }
                        if (subTasks.containsKey(id)) {
                            getHistoryManager().addTask(subTasks.get(id));
                        }
                        if (epics.containsKey(id)) {
                            getHistoryManager().addTask(epics.get(id));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Критическая ошибка");
        }
        return fileBackedTasksManager;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id, type, name, status, description, epic\n");
            for (Task task : super.takeTasks()) {
                writer.write(CSVTaskFormat.toString(task) + "\n");
            }
            for (Task task : super.takeSubTasks()) {
                writer.write(CSVTaskFormat.toString(task) + "\n");
            }
            for (Task task : super.takeEpics()) {
                writer.write(CSVTaskFormat.toString(task) + "\n");
            }
            writer.write("\n");
            writer.write(CSVTaskFormat.toString(historyManager));

        } catch (IOException e) {
            System.out.println("Ошибка записи в файл.");
        }
    }

    public static void addTask(Task task) { // Наполняем мапы задачами, полученными из файла
        switch (task.getTaskType()) {
            case TASK:
                tasks.put(task.getid(), task);
                break;
            case SUBTASK:
                subTasks.put(task.getid(), (Subtask) task);
                break;
            case EPIC:
                epics.put(task.getid(), (Epic) task);
                break;
        }
    }

    @Override
    public Task createTask(String title, String description) {
        Task task = super.createTask(title, description);
        save();
        return task;
    }

    @Override
    public Subtask createSubTask(Task task, Epic epic) {
        Subtask subtask = super.createSubTask(task, epic);
        save();
        return subtask;
    }

    @Override
    public Epic createEpic(String title, String description) {
        Epic epic = super.createEpic(title, description);
        save();
        return epic;
    }

    @Override
    public void add(Epic epic, Subtask subtask) {
        super.add(epic, subtask);
        save();
    }

    @Override
    public void updateTask(Task task, TaskStatus status) {
        super.updateTask(task, status);
        save();
    }

    @Override
    public void updateSubTask(Subtask subtask, TaskStatus status) {
        super.updateSubTask(subtask, status);
        save();
    }

    @Override
    public TaskStatus checkEpicStatus(Epic epic) {
        return super.checkEpicStatus(epic);
    }

    @Override
    public List<Task> takeTasks() {
        return super.takeTasks();
    }

    @Override
    public List<Subtask> takeSubTasks() {
        return super.takeSubTasks();
    }

    @Override
    public List<Epic> takeEpics() {
        return super.takeEpics();
    }

    @Override
    public void deliteTasks() {
        super.deliteTasks();
        save();
    }

    @Override
    public void deliteSubTasks() {
        super.deliteSubTasks();
        save();
    }

    @Override
    public void deliteEpics() {
        super.deliteEpics();
        save();
    }

    @Override
    public void deliteTasksId(int id) {
        super.deliteTasksId(id);
        save();
    }

    @Override
    public void deliteSubTasksId(int id) {
        super.deliteSubTasksId(id);
        save();
    }

    @Override
    public void deliteEpicsId(int id) {
        super.deliteEpicsId(id);
        save();
    }

    @Override
    public List<Subtask> takeEpicsTasks(Epic epic) {
        return super.takeEpicsTasks(epic);
    }

    @Override
    public Task getTasks(int id) {
        Task task = super.getTasks(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubTasks(int id) {
        Subtask subtask = super.getSubTasks(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpics(int id) {
        Epic epic = super.getEpics(id);
        save();
        return epic;
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public void clearHistory() {
        super.clearHistory();
        save();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }
}
