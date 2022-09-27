public class Task {

    private String title; // Название
    private int id; // Уникальный идентификационный номер задачи
    private String taskStatus; // Статус
    private String description; // Описание


    public Task(String title, String description) {
        this.title = title;
        this.taskStatus = "NEW";
        this.description = description;
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

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", taskStatus='" + taskStatus + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
