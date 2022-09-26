
public class Subtask extends Task {


    private int epicId; // Айдишник эпика, к которому относится подзадача, по умолчанию 0

    public Subtask(Task task) { // Конструктор подзадачи, подразумеевается, что
        super(task.getTitle(), task.getDescription()); // изначально всегда создается задача
        this.epicId = 0;
    }

    public int getEpicId() { // Получение id эпика, в который входит подзадача
        return epicId;
    }

    public void setEpicId(int epicId) { // Установка id эпика, в который входит подзадача
        this.epicId = epicId;
    }
}
