package kanban.manager;

import kanban.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;
    private int size;
    private final Map<Integer, Node> nodeMap = new HashMap<>();


    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Node current = head; current != null; current = current.next) {
            tasks.add(current.task);
        }
        return tasks;
    }


    private void linkLast(Task task) {
        Node node = new Node(task, null, null);

        if (size == 0) {
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }

        tail = node;
        size++;
        nodeMap.put(task.getid(), node);
    }

    private void removeNode(int id) {
        size--;
        Node nodeTemp = nodeMap.get(id);
        if (nodeTemp == head) {
            head = head.next;
            nodeMap.remove(id);
            return;
        } else if (nodeTemp == tail) {
            tail = tail.prev;
            tail.next = null;
            nodeMap.remove(id);
            return;
        }
        for (Node node = head; node != null; node = node.next) {
            if (node == nodeTemp) {
                node.prev.next = node.next;
                nodeMap.remove(id);
                break;
            }
        }
    }

    private void clearNodes() {
        head = null;
        tail = null;
        size = 0;
        nodeMap.clear();
    }

    @Override
    public void remove(int id) {
        if (nodeMap.containsKey(id)) {
            removeNode(id);
        } else System.out.println("Элемента с id " + id + " в истории просмотров не найдено.");
    }

    @Override
    public void addTask(Task task) {
        int id = task.getid(); // Извлекли id таски
        if (nodeMap.containsKey(id)) { // Проверили есть ли таска в нодах
            remove(id); // При необходимости удалили
        }
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() { // Получение списка последних задач
        return getTasks();
    }

    @Override
    public void clearHistory() {
        clearNodes();
    }

    private static class Node {
        private final Task task;
        private Node next;
        private Node prev;


        public Node(Task task, Node next, Node prev) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "task=" + task +
                    ", next=" + next +
                    ", prev=" + prev +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node node = (Node) o;
            return Objects.equals(task, node.task) && Objects.equals(next, node.next) && Objects.equals(prev, node.prev);
        }

        @Override
        public int hashCode() {
            return Objects.hash(task, next, prev);
        }
    }
}
