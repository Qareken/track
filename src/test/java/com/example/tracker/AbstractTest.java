package com.example.tracker;

import com.example.tracker.dto.TaskDto;
import com.example.tracker.entity.Task;
import com.example.tracker.entity.User;
import com.example.tracker.entity.enumurate.TaskStatus;
import com.example.tracker.mapper.TaskMapper;
import com.example.tracker.mapper.UserMapper;
import com.example.tracker.repository.TaskRepository;
import com.example.tracker.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@AutoConfigureWebTestClient
public class AbstractTest {
    @Value("${spring.data.mongodb.uri}")
    private  String mongoUri;
    protected static String FIRST_TASK = UUID.randomUUID().toString();
    protected static String FIRST_USER= UUID.randomUUID().toString();
    protected static String SECOND_TASK = UUID.randomUUID().toString();
    protected static String SECOND_USER = UUID.randomUUID().toString();
    protected static String THIRD_USER = UUID.randomUUID().toString();
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.8").withReuse(true);
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry){
        registry.add("mongoUri", mongoDBContainer::getReplicaSetUrl);
    }
    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected TaskServiceImpl taskService;
    @Autowired
    protected TaskRepository taskRepository;
    @Autowired
    protected TaskMapper taskMapper;
    @Autowired
    protected UserMapper userMapper;
    protected    List<User> listUser = List.of(new User(FIRST_USER, "John", "john@example.com"),
            new User(SECOND_USER, "Paul", "paul@example.com"),
            new User(THIRD_USER, "Lucy", "lucy@example.com"));

    @BeforeEach
    public void setUp(){

        TaskDto task1 = new TaskDto();
        task1.setId(FIRST_TASK);
        task1.setName("Task 1");
        task1.setDescription("This is task 1");
        task1.setCreatedAt(Instant.now());
        task1.setUpdatedAt(Instant.now());
        task1.setStatus(TaskStatus.DONE);
        task1.setAuthor(listUser.get(1));
        task1.setAssignee(listUser.get(2));
        task1.setObservers(new HashSet<>(Arrays.asList(listUser.get(0), listUser.get(2))));

        TaskDto task2 = new TaskDto();
        task2.setId(SECOND_TASK);
        task2.setName("Task 2");
        task2.setDescription("This is task 2");
        task2.setCreatedAt(Instant.now());
        task2.setUpdatedAt(Instant.now());
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setAuthor(listUser.get(0));
        task2.setAssignee(listUser.get(1));
        task2.setObservers(new HashSet<>(Arrays.asList(listUser.get(1), listUser.get(2))));

        taskService.create(task1).block();
        taskService.create(task2).block();

    }
    @AfterEach
    public void afterEach(){
        taskRepository.deleteAll();
    }


}
