package kanban;

import kanban.manager.Managers;
import kanban.manager.TaskManager;
import kanban.tasks.Epic;
import kanban.tasks.Task;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        TaskManager manager = Managers.getDefault();
        Task task1 = manager.createTask("Задача 1", "Описание",
                LocalDateTime.of(2022,12,9,13,00), 30);
        Task task2 = manager.createTask("Задача 2", "Описание",
                LocalDateTime.of(2022,12,9,12,46), 15);

        System.out.println(manager.getPrioritizedTasks());




    }
}
