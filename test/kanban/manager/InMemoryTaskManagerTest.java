package kanban.manager;

import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        taskManager = new InMemoryTaskManager();
        initTasks();
    }

}