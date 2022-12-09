package kanban.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private final ArrayList<Integer> subTasksIds = new ArrayList<>();    // Айдишники подзадач, содержащиеся в эпике
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
        super.setTaskType(TaskType.EPIC);
    }

    public Epic(String title, int id, TaskStatus taskStatus, String description, TaskType taskType,
                LocalDateTime startTime, long duration) {
        super(title, id, taskStatus, description, taskType, startTime, duration);
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

    public void clear(){
        subTasksIds.clear();
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void setStart (LocalDateTime startTime){
        super.setStartTime(startTime);
    }

    @Override
    public String toString() {
        return "" + super.getid() + ", " + TaskType.EPIC + ", " + super.getTitle() + ", " + super.getTaskStatus() +
                ", " + super.getDescription()  + ", " + super.getStartTime() + ", " + super.getDuration() + ", " +
                getEndTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasksIds, epic.subTasksIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksIds);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}
