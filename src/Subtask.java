public class Subtask extends Task{

    private int epicId; // Айдишник подзадачи

    public Subtask(String title, int taskId, String description, int epicId) {
        super(title,  description);
        this.epicId = epicId;
    }
}
