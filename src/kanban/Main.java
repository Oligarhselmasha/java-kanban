package kanban;

import kanban.manager.Managers;
import kanban.manager.TaskManager;
import kanban.tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager manager = Managers.getDefault();

        manager.createTask("Какая-то задача 1", "Первая"); // Создал какую-то задачу, у нее id 1
        manager.createTask("Какая-то задача 2", "Вторая"); // Создал какую-то задачу, у нее id 2
        manager.createEpic("Какой-то эпик", "id 3"); // Создал какой-то эпик, у него id 3
        manager.createSubTask(manager.getTasks(1), manager.getEpics(3)); // Создал подзадачу эпика на основе 1
        manager.createSubTask(manager.getTasks(2), manager.getEpics(3)); // и 2 подзадачи, у них уже id 4 и 5 соответственно

        System.out.println(manager.takeEpicsTasks(manager.getEpics(3))); // Подзадачи, хранящиеся в эпике
        System.out.println(manager.getEpics(3)); // Сам эпик, пока что у него статус NEW

        manager.updateSubTask(manager.getSubTasks(4), TaskStatus.IN_PROGRESS); // Поменял у подзадачи 4 статус
        System.out.println(manager.getEpics(3)); // Теперь у эпика статус IN_PROGRESS

        manager.updateSubTask(manager.getSubTasks(4), TaskStatus.DONE); // Поменял у обоих задач, входящих в эпик
        manager.updateSubTask(manager.getSubTasks(5), TaskStatus.DONE); // статус на DONE
        System.out.println(manager.getEpics(3)); // Теперь у эпика статус DONE

        manager.deliteSubTasksId(4); // Удалил первый сабтаск, входящий в эпик
        manager.deliteSubTasksId(5); // Удалил второй сабтаск, входящий в эпик
        System.out.println(manager.getEpics(3)); // Теперь у эпика статус NEW

        for (Integer i = 6; i <= 15 ; i++) {  // Здесь в обертку завернул, чтобы далее запарсить
            manager.createTask("Рандомная задача", i.toString()); // Протестировали функциональность history manager
            manager.getTasks(i);
        }

        System.out.println(manager.getHistory());

        manager.getTasks(15);

        System.out.println(manager.getHistory());

    }
}
