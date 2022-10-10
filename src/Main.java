public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager manager = Managers.getDefault();

        for (Integer i = 1; i <= 10; i++) {
            manager.createTask("Задача под номером ", i.toString());
        }

        for (int i = 1; i <= 10; i++) {
            System.out.println(manager.getTasks(i));
        }

        System.out.println(manager.getHistory());

        manager.createTask("Задача под номером ", "11");

        System.out.println(manager.getTasks(11));
        System.out.println(manager.getHistory());
    }
}
