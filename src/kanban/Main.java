package kanban;

import kanban.manager.Managers;
import kanban.manager.TaskManager;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        TaskManager manager = Managers.getDefault();

    }
}
