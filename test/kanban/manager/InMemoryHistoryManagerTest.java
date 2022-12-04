package kanban.manager;

import kanban.tasks.Epic;
import kanban.tasks.Subtask;
import kanban.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    protected HistoryManager historyManager;
    protected Epic epic;
    protected Subtask subtask;
    protected Task task;

    @BeforeEach
    void setUp(){
        historyManager = new InMemoryHistoryManager();


            }
//
//    @Test
//    public void
}