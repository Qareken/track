package com.example.tracker.mapper;

import com.example.tracker.dto.TaskDto;
import com.example.tracker.entity.Task;
import com.example.tracker.entity.User;
import com.example.tracker.entity.enumurate.TaskStatus;
import com.example.tracker.exception.TaskStatusException;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Component
@Mapper(componentModel = "spring", uses = UserMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
    default TaskStatus mapStatusNameToStatus(String label){
        for(TaskStatus status:TaskStatus.values()){
            if(status.getLabel().equalsIgnoreCase(label)) return status;
        }
        throw new TaskStatusException(MessageFormat.format("Task status with {} not found!!", label));
    }
    @Mapping(target = "authorId", source = "dto.author.id")
    @Mapping(target = "assigneeId", source = "dto.assignee.id")
    @Mapping(target = "observerIds", source = "dto.observers", qualifiedByName = "userToIdSet")
    Task toTask(TaskDto dto);

    // Custom method to map Set<User> to Set<String> of user IDs
    @Named("userToIdSet")
    default Set<String> userToIdSet(Set<User> users) {
        return users.stream()
                .map(User::getId)
                .collect(Collectors.toSet());
    }

    TaskDto toDto(Task task);
    default List<Task> toListEntity(List<TaskDto> taskDtos) {
        return taskDtos.stream()
                .map(this::toTask)
                .collect(Collectors.toList());
    }

}
