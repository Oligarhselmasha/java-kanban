package kanban.tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subTasksIds = new ArrayList<>();    // Айдишники подзадач, содержащиеся в эпике

    public Epic(String title, String description) {
        super(title, description);
    }

    public ArrayList<Integer> getTasksIds() { // Получение подзадач, входящих в эпик
        return subTasksIds;
    }

    public void setSubTasksIds(int id) { // Добавление подзадач в эпик
        this.subTasksIds.add(id);
    }

    public void deliteById(Integer id) {
        subTasksIds.remove(id);
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }
}
