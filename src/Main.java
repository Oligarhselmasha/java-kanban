public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager manager = new Manager();
        Epic Move = manager.createEpic("Подготовка к переезду", "Давно пора!");
        Subtask Clean = manager.createSubTask(manager.createTask("Помыть полы и посуду", "Лучше с утра"), Move);
        Subtask Bag = manager.createSubTask(manager.createTask("Собрать чемодан", "Вес не более 20 кг!"), Move);
        manager.add(Move, Clean);
        manager.add(Move, Bag);

        Epic Reading = manager.createEpic("Читать 100 страниц в день", "Ну хотя-бы 50!");
        Subtask DayRead = manager.createSubTask(manager.createTask("Читать сегодня", "Не ленись!"), Reading);
        manager.add(Reading, DayRead);

        System.out.println(manager.takeTasks());
        System.out.println(manager.takeEpics());
        System.out.println(manager.takeSubTasks());

        manager.updateSubTask(Clean, "DONE");
        manager.updateSubTask(DayRead, "DONE");

        System.out.println(manager.takeEpics());
        System.out.println(manager.takeSubTasks());

        manager.deliteSubTasksId(Clean.getid());
        manager.deliteEpicsId(Reading.getid());

        System.out.println(manager.takeEpics());
        System.out.println(manager.takeSubTasks());
        System.out.println(manager.takeEpicsTasks(Move));
    }
}
