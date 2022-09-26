import java.util.ArrayList;

public class Epic extends Task{

    protected ArrayList<Integer> subTasksIds; // Айдишники подзадач, содержащиеся в эпике


    public Epic(String title, String description) {
        super(title, description);
    }

    public ArrayList<Integer> getTasksIds() { // Получение подзадач, входящих в эпик
        return subTasksIds;
    }

    public void setSubTasksIds(Integer id) { // Добавление подзадач в эпик
        this.subTasksIds.add(id);
    }
    public void del () { // Удаление всех подзадач из эпика
        this.subTasksIds.clear();
    }
}
