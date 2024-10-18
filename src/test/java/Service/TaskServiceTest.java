package Service;

import Exceptions.TaskValidationException;
import Exceptions.UserNotExistException;
import Model.Enums.TaskStatus;
import Model.Tag;
import Model.Task;
import Model.User;
import Repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private User userAuth;
    private User assigneeUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userAuth = new User();
        userAuth.setId(1L);
        assigneeUser = new User();
        assigneeUser.setId(2L);

        Task task = new Task("Task title", "Task description", LocalDate.now().plusDays(5), LocalDate.now().plusDays(10), userAuth, assigneeUser, TaskStatus.NOT_STARTED);
    }

    @Test
    void testInsertTask_ValidTask_Success() {
        Task task = new Task("Task title", "Task description", LocalDate.now().plusDays(5), LocalDate.now().plusDays(10), userAuth, assigneeUser, TaskStatus.NOT_STARTED);
        task.setListTags(Arrays.asList(new Tag("Tag1"), new Tag("Tag2")));

        Mockito.when(userService.findUserById(userAuth.getId())).thenReturn(userAuth);
        Mockito.when(userService.findUserById(assigneeUser.getId())).thenReturn(assigneeUser);

        taskService.insertTask(task, userAuth);

        // Verify that the taskRepository.addTask was called once
        Mockito.verify(taskRepository, Mockito.times(1)).addTask(task);
    }

    @Test
    void testInsertTask_NullTask_ThrowsTaskValidationException() {
        TaskValidationException exception = assertThrows(TaskValidationException.class, () -> {
            taskService.insertTask(null, userAuth);
        });

        assertEquals("task information cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testInsertTask_EmptyTask_ThrowsTaskValidationException() {
        Task emptyTask = new Task();
        TaskValidationException exception = assertThrows(TaskValidationException.class, () -> {
            taskService.insertTask(emptyTask, userAuth);
        });

        assertEquals("task information cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testInsertTask_InvalidDates_ThrowsTaskValidationException() {
        Task task = new Task("Task title", "Task description", LocalDate.now().plusDays(10), LocalDate.now().plusDays(5), userAuth, assigneeUser, TaskStatus.NOT_STARTED);
        task.setListTags(Arrays.asList(new Tag("Tag1")));

        TaskValidationException exception = assertThrows(TaskValidationException.class, () -> {
            taskService.insertTask(task, userAuth);
        });

        assertEquals("start due should be after or equal start date.", exception.getMessage());
    }

    @Test
    void testInsertTask_AssignedUserNotExist_ThrowsUserNotExistException() {
        Task task = new Task("Task title", "Task description", LocalDate.now().plusDays(5), LocalDate.now().plusDays(10), userAuth, assigneeUser, TaskStatus.NOT_STARTED);
        task.setListTags(Arrays.asList(new Tag("Tag1")));

        Mockito.when(userService.findUserById(assigneeUser.getId())).thenReturn(null);

        UserNotExistException exception = assertThrows(UserNotExistException.class, () -> {
            taskService.insertTask(task, userAuth);
        });

        assertEquals("assigned user not exists", exception.getMessage());
    }

    @Test
    void testInsertTask_CreatedUserNotExist_ThrowsUserNotExistException() {
        Task task = new Task("Task title", "Task description", LocalDate.now().plusDays(5), LocalDate.now().plusDays(10), userAuth, assigneeUser, TaskStatus.NOT_STARTED);
        task.setListTags(Arrays.asList(new Tag("Tag1")));

        Mockito.when(userService.findUserById(userAuth.getId())).thenReturn(null);  // Mock no user found

        UserNotExistException exception = assertThrows(UserNotExistException.class, () -> {
            taskService.insertTask(task, userAuth);
        });

        assertEquals("created user not exists", exception.getMessage());
    }

    @Test
    void testInsertTask_MissingTags_ThrowsTaskValidationException() {
        Task task = new Task("Task title", "Task description", LocalDate.now().plusDays(5), LocalDate.now().plusDays(10), userAuth, assigneeUser, TaskStatus.NOT_STARTED);

        TaskValidationException exception = assertThrows(TaskValidationException.class, () -> {
            taskService.insertTask(task, userAuth);
        });

        assertEquals("Tags are required.", exception.getMessage());
    }

    @Test
    void testInsertTask_InvalidStartDate_ThrowsTaskValidationException() {
        Task task = new Task("Task title", "Task description", LocalDate.now().plusDays(2), LocalDate.now().plusDays(10), userAuth, assigneeUser, TaskStatus.NOT_STARTED);
        task.setListTags(Arrays.asList(new Tag("Tag1")));

        TaskValidationException exception = assertThrows(TaskValidationException.class, () -> {
            taskService.insertTask(task, userAuth);
        });

        assertEquals("start date should be at least 3 days after the current date.", exception.getMessage());
    }

}