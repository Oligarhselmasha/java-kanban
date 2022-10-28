package kanban;

import kanban.manager.Managers;
import kanban.manager.TaskManager;


public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager manager = Managers.getDefault();

        manager.createTask("1 задача", "Обычная задача"); // id1
        manager.createTask("2 задача", "Обычная задача"); // id2
        manager.createTask("3 задача", "Обычная задача"); // id3
        manager.createEpic("1 эпик", "1 Эпик"); // id4
        manager.createSubTask(manager.getTasks(1), manager.getEpics(4)); // id5
        manager.createSubTask(manager.getTasks(2), manager.getEpics(4)); // id6
        manager.createTask("4 задача", "Обычная задача"); // id7
        manager.createTask("5 задача", "Обычная задача"); // id8
        manager.clearHistory(); // Очистил историю, чтобы отбросить те случаи, когда зывыл get() для создания сабтаски
        manager.getTasks(2); // Обратился к 2 задаче
        manager.getTasks(1); // Обратился к 1 задаче
        manager.getTasks(1); // Обратился к 1 задаче
        manager.getTasks(2); // Обратился к 2 задаче
        manager.getTasks(8); // Обратился к 5 задаче
        System.out.println(manager.getHistory()); // Вывелась история 1, 2, 5
        manager.deliteTasksId(8); // Удалил 5 задачу
        System.out.println(manager.getHistory()); // Вывелась история 1, 2
        manager.deliteTasksId(1); // Удалил 1 задачу
        System.out.println(manager.getHistory()); // Вывелась история 2
        manager.deliteTasksId(2); // Удалил 2 задачу
        manager.getEpics(4); // Запросил 1 эпик
        System.out.println(manager.getHistory()); // Вывелась история 1 эпик
        manager.getSubTasks(5); // Запросил 1 сабтаск 1 эпика
        System.out.println(manager.getHistory()); // Вывелась история 1 эпик, 1 сабтаск
        manager.getSubTasks(6); // Запросил 2 сабтаск 1 эпика
        System.out.println(manager.getHistory()); // Вывелась история 1 эпик, 1 сабтаск, 2 сабтаск
        manager.deliteSubTasksId(6); // Удалил 2 сабтаск
        System.out.println(manager.getHistory()); // Вывелась история 1 эпик, 1 сабтаск
        manager.getTasks(7); // Запросил 4 задачу
        System.out.println(manager.getHistory()); // Вывелась история 1 эпик, 1 сабтаск, 4 задача
        manager.deliteEpicsId(4); // Удалил эпик
        System.out.println(manager.getHistory()); // Удалив эпик - удалился оставшийся у него сабтаск, история пуста

    }
}
