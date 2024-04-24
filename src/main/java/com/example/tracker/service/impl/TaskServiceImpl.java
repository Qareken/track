package com.example.tracker.service.impl;

import com.example.tracker.dto.TaskDto;
import com.example.tracker.dto.UserDto;
import com.example.tracker.entity.Task;
import com.example.tracker.entity.User;
import com.example.tracker.mapper.TaskMapper;
import com.example.tracker.mapper.UserMapper;
import com.example.tracker.repository.TaskRepository;
import com.example.tracker.repository.UserRepository;
import com.example.tracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;

    @Override
    public Flux<TaskDto> findAll() {
        Flux<Task> taskFlux = taskRepository.findAll();
        return taskFlux.flatMap(task -> {
            String authorId = task.getAuthorId() != null ? task.getAuthorId() : "unknown";
            String assigneeId = task.getAssigneeId() != null ? task.getAssigneeId() : "unknown";
            Mono<User> authorMono = userRepository.findById(authorId);
            Mono<User> assigneeMono = userRepository.findById(assigneeId);
            Mono<Set<User>> observerFlux = userRepository.findAllById(task.getObserverIds()).collect(Collectors.toSet());
            return Flux.zip(Mono.just(task), authorMono.defaultIfEmpty(new User()), assigneeMono.defaultIfEmpty(new User()), observerFlux)
                    .map(tuple->{
                        Task forDto = tuple.getT1();
                        User assignee = tuple.getT3();
                        User author =tuple.getT2();
                        forDto.setAssignee(assignee.getId()!=null?assignee:null);
                        forDto.setAuthor(author.getId()!=null?author:null);
                        forDto.setObservers(tuple.getT4());
                        return taskMapper.toDto(forDto);
                    });
        });


    }

    @Override
    public Mono<TaskDto> findById(String id) {
        System.out.println("Searching for task with id: " + id);
        var taskMono = taskRepository.findById(id);

        return taskMono.flatMap(task -> {

            System.out.println("Task found: " + task);
            String authorId = task.getAuthorId() != null ? task.getAuthorId() : "unknown";
            String assigneeId = task.getAssigneeId() != null ? task.getAssigneeId() : "unknown";

            Mono<Set<User>> observersMono = Flux.fromIterable(task.getObserverIds())
                    .flatMap(userRepository::findById)
                    .collect(Collectors.toSet());

            Mono<User> authorMono = userRepository.findById(authorId).defaultIfEmpty(new User());
            Mono<User> assigneeMono = userRepository.findById(assigneeId).defaultIfEmpty(new User());

            return Mono.zip(taskMono, authorMono.defaultIfEmpty(new User()), assigneeMono.defaultIfEmpty(new User()), observersMono).map(tuple -> {
                Task foundTask = tuple.getT1();

                System.out.println("Task details: " + foundTask);
                if (tuple.getT2().getId() == null) {
                    foundTask.setAuthor(null);
                } else {
                    foundTask.setAuthor(tuple.getT2());
                }
                if (tuple.getT3().getId() != null) {
                    foundTask.setAssignee(null);
                } else {
                    foundTask.setAssignee(tuple.getT3());
                }

                foundTask.setAssignee(tuple.getT3());
                foundTask.setObservers(tuple.getT4());
                System.out.println("Task details: " + foundTask);
                return taskMapper.toDto(foundTask);
            });
        }).switchIfEmpty(Mono.defer(() -> {
            System.out.println("Task with id " + id + " not found");
            return Mono.empty();
        }));
    }

    private Mono<TaskDto> findWithDetails(Mono<Task> taskMono) {
        Mono<Set<User>> observersMono = taskMono.flatMap(task -> Flux.fromIterable(task.getObserverIds()).flatMap(userRepository::findById).collect(Collectors.toSet()));
        Mono<User> authorMono = taskMono.flatMap(task -> userRepository.findById(task.getAuthorId()));
        Mono<User> assigneeMono = taskMono.flatMap(task -> userRepository.findById(task.getAssigneeId()));
        return Mono.zip(taskMono, authorMono, assigneeMono, observersMono).map(tuple -> {
            Task task = tuple.getT1();
            System.out.println(task.getId());
            task.setAuthor(tuple.getT2());
            System.out.println(task.getAuthor());
            task.setAssignee(tuple.getT3());
            System.out.println(task.getAssignee());
            task.setObservers(tuple.getT4());
            System.out.println(task.getAssigneeId());
            return taskMapper.toDto(task);
        });
    }

    @Override
    public Mono<TaskDto> create(TaskDto taskDto) {
        var task = taskMapper.toTask(taskDto);
        return userService.createOrUpdate(taskDto.getAuthor()).flatMap(author -> {

                    task.setAuthorId(author.getId());
                    return userService.createOrUpdate(taskDto.getAssignee());
                }
        ).flatMap(assignee -> {
            task.setAssigneeId(assignee.getId());

            return Flux.fromIterable(taskDto.getObservers()).flatMap(userService::createOrUpdate)
                    .collect(Collectors.toSet());
        }).flatMap(observers -> {
            task.setObserverIds(observers.stream().map(user -> {

                return user.getId();
            }).collect(Collectors.toSet()));
            return taskRepository.save(task).map(taskMapper::toDto);
        });
    }

    @Override
    public Mono<TaskDto> addObservers(String taskId, UserDto userDto) {
        var userMono = userRepository.findUserByUsername(userDto.getUsername()).switchIfEmpty(Mono.defer(() -> userRepository.save(userMapper.toEntity(userDto))));
        Mono<Task> taskMono = taskRepository.findById(taskId);
        return Mono.zip(taskMono, userMono).flatMap(tuple -> {
            User user = tuple.getT2();
            Task task = tuple.getT1();
            Set<String> observerIds = task.getObserverIds();
            if (!observerIds.contains(user.getId())) {
                observerIds.add(user.getId());
                task.setObserverIds(observerIds);
                return findWithDetails(taskRepository.save(task));
            } else {
                return Mono.empty();
            }
        });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }

    @Override
    public Mono<TaskDto> update(String id, TaskDto taskDto) {
        var task = taskMapper.toTask(taskDto);
        AtomicBoolean check = new AtomicBoolean(false);
        return taskRepository.findById(id).flatMap(taskForUpdate -> {
            if (taskDto.getName() != null) {
                taskForUpdate.setName(taskDto.getName());
                check.set(true);
            }
            if (taskDto.getDescription() != null) {
                taskForUpdate.setDescription(taskDto.getDescription());
                check.set(true);
            }
            if (taskDto.getStatus() != null) {
                taskForUpdate.setStatus(taskDto.getStatus());
                check.set(true);
            }
            if (taskDto.getAuthor() != null) {
                taskForUpdate.setAuthor(taskDto.getAuthor());
                check.set(true);
            }
            if (taskDto.getAssignee() != null) {
                taskForUpdate.setAssignee(taskDto.getAssignee());
                check.set(true);
            }
            if (taskDto.getObservers() != null) {
                taskForUpdate.setObservers(taskDto.getObservers());
                check.set(true);
            }
            return create(taskMapper.toDto(taskForUpdate));
        });
    }

    @Override
    public Mono<Void> deleteUser(String userId) {
        return userRepository.deleteById(userId).thenMany(taskRepository.findAll())
                .flatMap(task -> {
                    if (userId.equals(task.getAssigneeId())) task.setAssigneeId(null);
                    if (userId.equals(task.getAuthorId())) task.setAuthorId(null);
                    task.getObserverIds().remove(userId);
                    return taskRepository.save(task);
                }).then();
    }
}
