import java.util.ArrayList;

public class Subtask extends Task{

    protected ArrayList<Integer> tasksIds; // Айдишники задач, содержащиеся в подзадаче

    private int epicId; // Айдишник эпика, к которому относится подзадача, по умолчанию 0

    public Subtask(String title, String description) { // Конструктор
        super(title,  description);
        this.epicId = 0;
    }

    public ArrayList<Integer> getTasksIds() { // Получение задач, входящих в подзадачу
        return tasksIds;
    }
    public ArrayList<Integer> getSubTasksIds() {
        return tasksIds;
    }

    public void setTasksIds(Integer id) { // Добавление задач, которые входят в подзадачу
        this.tasksIds.add(id);
    }

    public int getEpicId() { // Получение id эпика, в который входит подзадача
        return epicId;
    }

    public void setEpicId(int epicId) { // Установка id эпика, в который входит подзадача
        this.epicId = epicId;
    }

    public void del() { // Удаление всех задач, входящих в подзадачу
        this.tasksIds.clear();
    }
}
