package kanban;

import kanban.manager.Managers;
import kanban.manager.TaskManager;
import kanban.server.HttpTaskServer;
import kanban.server.KVServer;
import kanban.tasks.Epic;
import kanban.tasks.Subtask;
import kanban.tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        /* Тестировать на порту 8883*/

        new KVServer().start();

        TaskManager manager = Managers.getDefault();

        Task task1 = manager.createTask("Задача 1", "Описание",
                LocalDateTime.of(2022, 12, 9, 13, 00), 30);
        Task task2 = manager.createTask("Задача 2", "Описание",
                LocalDateTime.of(2022, 12, 9, 12, 43), 15); // id2
        Epic epic = manager.createEpic("Эпик 1", "Описание"); // id3
        Subtask subtask1 = manager.createSubTask(task1, epic); // id4
        Subtask subtask2 = manager.createSubTask(task2, epic); // id5
        manager.getTasks(task1.getid());
        manager.getTasks(task2.getid());

        new  HttpTaskServer(manager);

    }
}
