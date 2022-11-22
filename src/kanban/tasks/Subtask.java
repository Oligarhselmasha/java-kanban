package kanban.tasks;

public class Subtask extends Task {


    private int epicId; // Айдишник эпика, к которому относится подзадача, по умолчанию 0

    public Subtask(Task task) { // Конструктор подзадачи, подразумеевается, что
        super(task.getTitle(), task.getDescription()); // изначально всегда создается задача;
    }

    public Subtask(String title, int id, TaskStatus taskStatus, String description, TaskType taskType, int epicId) {
        super(title, id, taskStatus, description, taskType);
        this.epicId = epicId;
    }


    public int getEpicId() { // Получение id эпика, в который входит подзадача
        return epicId;
    }

    public void setEpicId(int epicId) { // Установка id эпика, в который входит подзадача
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "" + super.getid() + ", " + TaskType.SUBTASK + ", " + super.getTitle() + ", " + super.getTaskStatus() +
                ", " + super.getDescription() + ", " + epicId;
    }
}
