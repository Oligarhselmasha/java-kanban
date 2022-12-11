package kanban.manager;

import kanban.tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormat {

    public static String toString(Task task) {
        return task.toString();
    }

    public static String toString(HistoryManager historyManager) { // Преобразует историю запросов в строку
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : historyManager.getHistory()) {
            stringBuilder.append(task.getid()).append(", ");
        }
        return stringBuilder.toString();
    }

    public static Task fromString(String value) { // Преобразует строку в задачу
        try {
            final String[] values = value.split(", ");
            final int id = Integer.parseInt(values[0]);
            final TaskType type = TaskType.valueOf(values[1]);
            final String name = values[2];
            final TaskStatus status = TaskStatus.valueOf(values[3]);
            final String description = values[4];
            final LocalDateTime startTime = LocalDateTime.parse(values[5]);
            final long duration = Long.parseLong(values[6]);
            switch (type) {
                case SUBTASK:
                    final int epicId = Integer.parseInt(values[8]);
                    return new Subtask(name, id, status, description, type, epicId, startTime, duration);
                case TASK:
                    return new Task(name, id, status, description, type, startTime, duration);
                case EPIC:
                    return new Epic(name, id, status, description, type, startTime, duration);
                default:
                    throw new IllegalStateException("Полученный объект не является объектом менеджера");
            }
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }
    public static List<Integer> historyFromString(String history) {
        List<Integer> list = new ArrayList<>();
        final String[] values = history.split(",");
        for (String value : values) {
            int id = Integer.parseInt(value);
            list.add(id);
        }
        return list;
    }
}

