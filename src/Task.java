public class Task {

    private String title; // Название
    private int taskId; // Уникальный идентификационный номер задачи
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

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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
}
