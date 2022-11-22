package kanban.tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subTasksIds = new ArrayList<>();    // Айдишники подзадач, содержащиеся в эпике

    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(String title, int id, TaskStatus taskStatus, String description, TaskType taskType) {
        super(title, id, taskStatus, description, taskType);
    }

    public ArrayList<Integer> getTasksIds() { // Получение подзадач, входящих в эпик
        return subTasksIds;
    }

    public void setSubTasksIds(int id) { // Добавление подзадач в эпик
        this.subTasksIds.add(id);
    }

    public void deliteById(Integer id) { // Метод удаляет указанные айдишники позадач, привязанные к эпику
        subTasksIds.remove(id);
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    @Override
    public String toString() {
        return "" + super.getid() + ", " + TaskType.EPIC + ", " + super.getTitle() + ", " + super.getTaskStatus() +
                ", " + super.getDescription();
    }
}
