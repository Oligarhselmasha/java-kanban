package kanban.tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private String title; // Название
    private int id; // Уникальный идентификационный номер задачи
    private TaskStatus taskStatus; // Статус
    private String description; // Описание
    private TaskType taskType;
    private LocalDateTime startTime; //  Начало задачи
    protected LocalDateTime endTime; //  Начало задачи
    private long duration; // Продолжительность задачи в минутах

    public Task(String title, String description) {
        this.title = title;
        this.taskStatus = TaskStatus.NEW;
        this.description = description;
    }

    public Task(String title, String description, LocalDateTime startTime, long duration) {
        this.title = title;
        this.taskStatus = TaskStatus.NEW;
        this.description = description;
        this.startTime = startTime;
        this.taskType = TaskType.TASK;
        this.endTime = setEndTime(startTime, duration);
        this.duration = duration;
//        this.format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    }


    public Task(String title, int id, TaskStatus taskStatus, String description, TaskType taskType,
                LocalDateTime startTime, long duration) { // Наиболее полный конструктор
        this.title = title;
        this.id = id;
        this.taskStatus = taskStatus;
        this.description = description;
        this.taskType = taskType;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = setEndTime(startTime, duration);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus status) {
        this.taskStatus = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "" + id + ", " + TaskType.TASK + ", " + title + ", " + taskStatus +
                ", " + description + ", " + startTime+ ", " + duration+ ", " + endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(getTitle(), task.getTitle()) && getTaskStatus() == task.getTaskStatus() && Objects.equals(getDescription(), task.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), id, getTaskStatus(), getDescription());
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
    public LocalDateTime setEndTime (LocalDateTime startTime, long duration){
        return startTime.plusMinutes(duration);
    }

    public void setEndTime (LocalDateTime endTime){
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
