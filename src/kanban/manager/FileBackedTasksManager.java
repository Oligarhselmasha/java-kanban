package kanban.manager;

import kanban.tasks.Epic;
import kanban.tasks.Subtask;
import kanban.tasks.Task;
import kanban.tasks.TaskStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public FileBackedTasksManager loadFromFile(File file){
        final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

           while (reader.ready()) {
               String line = reader.readLine();
               if (!line.isEmpty()){
                Task task = CSVTaskFormat.fromString(line);
                taskAdd(task);
                }

               if (!line.isEmpty()){
                   List<Integer> list = CSVTaskFormat.historyFromString(line);
                   for (Integer integer : list) {
                       super.getHistoryManager().addTask();
                   }

               }
           }
       } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
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
            CSVTaskFormat.toString(super.getHistoryManager());

        } catch (IOException e) {
            System.out.println("Ошибка записи в файл.");
        }

    }


    public void taskAdd (Task task){
        switch (task.getTaskType()){
            case TASK:
                tasks.put(task.getid(), task);
            case SUBTASK:
                subTasks.put(task.getid(), (Subtask) task);
            case EPIC:
                epics.put(task.getid(), (Epic) task);
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
    public ArrayList<Task> takeTasks() {
        return super.takeTasks();
    }

    @Override
    public ArrayList<Subtask> takeSubTasks() {
        return super.takeSubTasks();
    }

    @Override
    public ArrayList<Epic> takeEpics() {
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
    public ArrayList<Subtask> takeEpicsTasks(Epic epic) {
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
}
