package kanban.tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private int epicId; // Айдишник эпика, к которому относится подзадача, по умолчанию 0

    public Subtask(Task task) { // Конструктор подзадачи, подразумеевается, что
        super(task.getTitle(), task.getDescription(), task.getStartTime(), task.getDuration()); // изначально всегда создается задача;
        super.setTaskType(TaskType.SUBTASK);
    }

    public Subtask(String title, int id, TaskStatus taskStatus, String description, TaskType taskType, int epicId,
                   LocalDateTime startTime, long duration) {
        super(title, id, taskStatus, description, taskType, startTime, duration);
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
                ", " + super.getDescription() + ", " + super.getStartTime() + ", " + super.getDuration() + ", " + super.getEndTime() + ", " + epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
