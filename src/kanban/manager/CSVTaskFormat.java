package kanban.manager;

import kanban.tasks.*;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormat {

    public static String toString(Task task) {
        return task.toString();
    }

    public static String toString(HistoryManager historyManager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : historyManager.getHistory()) {
            sb.append(task.getid()).append(", ");
        }
        sb.delete(sb.length() - 1 , sb.length());
        return sb.toString();
    }

    public static Task fromString(String value) {
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        final String name = values [2];
        final TaskStatus status = TaskStatus.valueOf(values [3]);
        final String description = values [4];
        if (type == TaskType.SUBTASK){
            final int epicId = Integer.parseInt(values[5]);
            return new Subtask(name, id, status, description, type, epicId);
        }
        if (type == TaskType.TASK){
            return new Task(name, id, status, description, type);
        } else if (type == TaskType.EPIC) {
            Epic task = new Epic(name, id, status, description, type);
            task.setSubTasksIds();
            return task;
        }
        return null;
    }


    public static List<Integer> historyFromString(String value){
        List<Integer> list = new ArrayList<>();
        final String[] values = value.split(",");
        for (String s : values) {
            int id = Integer.parseInt(s);
            list.add(id);
        }
        return list;
    }

}
