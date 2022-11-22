package kanban.tasks;

import java.util.Objects;

public class Task {

    private String title; // Название
    private int id; // Уникальный идентификационный номер задачи
    private TaskStatus taskStatus; // Статус
    private String description; // Описание
    private TaskType taskType;


    public Task(String title, String description) {
        this.title = title;
        this.taskStatus = TaskStatus.NEW;
        this.description = description;
    }


    public Task(String title, int id, TaskStatus taskStatus, String description, TaskType taskType) { // Наиболее полный конструктор для FileBackedTasksManager
        this.title = title;
        this.id = id;
        this.taskStatus = taskStatus;
        this.description = description;
        this.taskType = taskType;
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
                ", " + description;
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
}
