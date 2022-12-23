package kanban.manager;

import kanban.tasks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task1;
    protected Task task2;
    protected Epic epic;
    protected Subtask subtask1;
    protected Subtask subtask2;


    protected void initTasks() throws IOException, InterruptedException { // Инициализируем таски
        task1 = taskManager.createTask("Test task", "Test task description",
                LocalDateTime.of(2022, 12, 9, 12, 0), 30); // id 1
        task2 = taskManager.createTask("Test task", "Test task description",
                LocalDateTime.of(2022, 12, 9, 13, 0), 30); // id 2
        epic = taskManager.createEpic("Test epic", "Test epic description"); // id 3
        subtask1 = taskManager.createSubTask(task1, epic); // id 4
        subtask2 = taskManager.createSubTask(task2, epic);// id 5
    }

    @Test
    void emptyEpic() throws IOException, InterruptedException { // Проверяет статус эпика, если он пустой
        taskManager.deliteSubTasksId(4); // Удаляем сабтаски эпика
        taskManager.deliteSubTasksId(5);
        String status = epic.getTaskStatus().toString();
        assertEquals("NEW", status, "Статус не NEW!");
    }

    @Test
    void allInNewEpic() { // Проверяет статус эпика у которого внутри все сабтаски имеют статус NEW
        String status = epic.getTaskStatus().toString();
        assertEquals("NEW", status, "Статус не NEW!");
    }

    @Test
    void allInDoneEpic() throws IOException, InterruptedException { // Проверяет статус эпика у которого внутри все сабтаски имеют статус DONE
        taskManager.updateSubTask(subtask1, TaskStatus.DONE);
        taskManager.updateSubTask(subtask2, TaskStatus.DONE);
        String status = epic.getTaskStatus().toString();
        assertEquals("DONE", status, "Статус не DONE!");
    }

    @Test
    void allInDoneAndNewEpic() throws IOException, InterruptedException { // Проверяет статус эпика у которого внутри все сабтаски имеют статус NEW и DONE
        taskManager.updateSubTask(subtask2, TaskStatus.DONE);
        String status = epic.getTaskStatus().toString();
        assertEquals("IN_PROGRESS", status, "Статус не IN_PROGRESS!");
    }

    @Test
    void allInINPROGRESSEpic() throws IOException, InterruptedException { // Проверяет статус эпика у которого внутри все сабтаски имеют статус IN_PROGRESS
        taskManager.updateSubTask(subtask1, TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subtask2, TaskStatus.IN_PROGRESS);
        String status = epic.getTaskStatus().toString();
        assertEquals("IN_PROGRESS", status, "Статус не IN_PROGRESS!");
    }

    @Test
    void createTask() throws IOException, InterruptedException { // Тест на проверку создания задачи
        Task taskNew = taskManager.createTask("Очередная задача", "Задача",
                LocalDateTime.of(2022, 12, 10, 12, 0), 30);
        Task savedTask = taskManager.getTasks(taskNew.getid());
        assertEquals(taskNew, savedTask, "Задачи не совпадают");
        assertNotNull(savedTask, "Задача не найдена");
        List<Task> tasks = taskManager.takeTasks();
        assertNotNull(tasks, "Задачи не возвращены");
        assertEquals(3, tasks.size(), "Неверное количество задач");
        assertEquals(taskNew, tasks.get(2), "Задачи не совпадают");
    }

    @Test
    void createSubTask() throws IOException, InterruptedException { // Тест на проверку создания подзадачи
        Subtask subtaskNew = taskManager.createSubTask(task1, epic);
        Subtask savedSubtask = taskManager.getSubTasks(subtaskNew.getid());
        assertEquals(subtaskNew, savedSubtask, "Задачи не совпадают");
        assertNotNull(savedSubtask, "Задача не найдена");
        List<Subtask> subTasks = taskManager.takeSubTasks();
        assertNotNull(subTasks, "Задачи не возвращены");
        assertEquals(3, subTasks.size(), "Неверное количество задач");
        assertEquals(subtaskNew, subTasks.get(2), "Задачи не совпадают");
    }

    @Test
    void createEpic() throws IOException, InterruptedException { // Тест на проверку создания эпика
        Epic epicNew = taskManager.createEpic("Тестовый эпик", "Тестовый эпик");
        Epic savedEpic = taskManager.getEpics(epicNew.getid());
        assertEquals(epicNew, savedEpic, "Задачи не совпадают");
        assertNotNull(savedEpic, "Задача не найдена");
        List<Epic> epics = taskManager.takeEpics();
        assertNotNull(epics, "Эпики не возвращены");
        assertEquals(2, epics.size(), "Неверное количество задач");
        assertEquals(epicNew, epics.get(1), "Задачи не совпадают");
    }

    @Test
    void add() throws IOException, InterruptedException { // Тест на проверку добавления подзадачи в эпик
        Subtask subtaskNew = taskManager.createSubTask(task1, epic);
        List<Subtask> subtasks = taskManager.takeEpicsTasks(epic);
        assertEquals(3, subtasks.size(), "Неверное количество задач");
        assertEquals(subtaskNew, subtasks.get(2), "Задачи не совпадают");
    }

    @Test
    void updateTask() throws IOException, InterruptedException { // Проверка обновления задачи
        taskManager.updateTask(task1, TaskStatus.DONE);
        String status = task1.getTaskStatus().toString();
        assertEquals("DONE", status, "Статус не обновился");
    }

    @Test
    void updateSubTask() throws IOException, InterruptedException { // Проверка обновления подзадачи
        taskManager.updateSubTask(subtask1, TaskStatus.IN_PROGRESS);
        String status = subtask1.getTaskStatus().toString();
        assertEquals("IN_PROGRESS", status, "Статус не обновился");
    }

    @Test
    void checkEpicStatus() throws IOException, InterruptedException { // Метод по проверку статуса эпика
        taskManager.updateSubTask(subtask1, TaskStatus.IN_PROGRESS);
        String status = taskManager.checkEpicStatus(epic).toString();
        assertEquals("IN_PROGRESS", status, "Статус не обновился");
    }


    @Test
    void takeTasks() {
        final List<Task> tasks = taskManager.takeTasks();
        List<Task> savedTasks = Arrays.asList(task1, task2);
        assertNotNull(savedTasks, "Таски не сохранились");
        assertEquals(savedTasks, tasks, "Таски не взялись");
    }

    @Test
    void takeSubTasks() {
        final List<Subtask> subTasks = taskManager.takeSubTasks();
        List<Subtask> savedSubTasks = Arrays.asList(subtask1, subtask2);
        assertNotNull(savedSubTasks, "Субтаски не сохранились");
        assertEquals(savedSubTasks, subTasks, "Субтаски не взялись");
    }

    @Test
    void takeEpics() {
        final List<Epic> epics = taskManager.takeEpics();
        List<Epic> savedEpics = Arrays.asList(epic);
        assertNotNull(savedEpics, "Эпики не сохранились");
        assertEquals(savedEpics, epics, "Эпики не взялись");
    }

    @Test
    void deliteTasks() throws IOException, InterruptedException {
        taskManager.deliteTasks();
        List<Task> tasks = taskManager.takeTasks();
        assertEquals(0, tasks.size(), "Таски не удалились");
    }

    @Test
    void deliteSubTasks() throws IOException, InterruptedException {
        taskManager.deliteSubTasks();
        List<Subtask> subTasks = taskManager.takeSubTasks();
        assertEquals(0, subTasks.size(), "Сабтаски не удалились");
        List<Subtask> subTasksOfEpic = taskManager.takeEpicsTasks(epic);
        assertEquals(0, subTasksOfEpic.size(), "Сабтаски из эпика не удалились");

    }

    @Test
    void deliteEpics() throws IOException, InterruptedException {
        taskManager.deliteEpics();
        List<Epic> epics = taskManager.takeEpics();
        assertEquals(0, epics.size(), "Эпики не удалились");
    }

    @Test
    void deliteTasksWrongId() { // Удаление таски с несуществующим id
        int id = 777;
        final RuntimeException exception = assertThrows(RuntimeException.class,
                () -> taskManager.deliteTasksId(id));
        assertEquals(exception.getMessage(), "Невозможно удалить задачу с id 777! В менеджере есть задачи со следующими id:");
    }


    @Test
    void deliteSubTasksId() throws IOException, InterruptedException {
        int id = 4;
        taskManager.deliteSubTasksId(id);
        assertEquals(1, taskManager.takeSubTasks().size());
    }

    @Test
    void deliteWrongEpicsId() { // Удаление эпика с несуществующим id
        int id = 666;
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> taskManager.deliteEpicsId(id));
        assertEquals(exception.getMessage(), "Недопустимый id.");
    }

    @Test
    void takeEpicsTasks() {
        List<Subtask> newList = Arrays.asList(subtask1, subtask2);
        List<Subtask> savedList = taskManager.takeEpicsTasks(epic);
        assertEquals(newList, savedList, "Сабтаски не сохранены в эпике");

    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        Task taskNew = taskManager.getTasks(1);
        assertEquals(task1, taskNew, "Таск не сохранен в менеджере");

    }

    @Test
    void getSubTasks() throws IOException, InterruptedException {
        Subtask subtaskNew = taskManager.getSubTasks(4);
        assertEquals(subtask1, subtaskNew, "СабТаск не сохранен в менеджере");

    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        Epic epicNew = taskManager.getEpics(3);
        assertEquals(epic, epicNew, "Эпик не сохранен в менеджере");

    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        taskManager.getSubTasks(4);
        taskManager.getTasks(1);
        taskManager.getEpics(3);
        List<Task> tasks = Arrays.asList(subtask1, task1, epic);
        List<Task> history = taskManager.getHistory();
        assertEquals(tasks, history, "История отображается неверно");

    }

    @Test
    void clearHistory() throws IOException, InterruptedException {
        taskManager.getSubTasks(4);
        taskManager.getTasks(1);
        taskManager.getEpics(3);
        taskManager.clearHistory();
        assertEquals(0, taskManager.getHistory().size(), "История не пуста");
    }

}
