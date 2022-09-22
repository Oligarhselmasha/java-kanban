import java.util.ArrayList;

public class Epic extends Task{

    private ArrayList<Integer> tasksIds; // Айдишники задач, содержащиеся в эпике



    public Epic(String title, String taskStatus, String description) {
        super(title, description);
        tasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getTasksIds() {
        return tasksIds;
    }

    public void setTasksIds(ArrayList<Integer> tasksIds) {
        this.tasksIds = tasksIds;
    }

}
