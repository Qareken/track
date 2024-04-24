package com.example.tracker.mapper;

import com.example.tracker.dto.TaskDto;
import com.example.tracker.entity.Task;
import com.example.tracker.entity.User;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-24T10:42:44+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task toTask(TaskDto dto) {
        if ( dto == null ) {
            return null;
        }

        Task task = new Task();

        task.setAuthorId( dtoAuthorId( dto ) );
        task.setAssigneeId( dtoAssigneeId( dto ) );
        task.setObserverIds( userToIdSet( dto.getObservers() ) );
        task.setId( dto.getId() );
        task.setName( dto.getName() );
        task.setDescription( dto.getDescription() );
        task.setCreatedAt( dto.getCreatedAt() );
        task.setUpdatedAt( dto.getUpdatedAt() );
        task.setStatus( dto.getStatus() );
        task.setAuthor( dto.getAuthor() );
        task.setAssignee( dto.getAssignee() );
        Set<User> set1 = dto.getObservers();
        if ( set1 != null ) {
            task.setObservers( new LinkedHashSet<User>( set1 ) );
        }

        return task;
    }

    @Override
    public TaskDto toDto(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskDto taskDto = new TaskDto();

        taskDto.setId( task.getId() );
        taskDto.setName( task.getName() );
        taskDto.setDescription( task.getDescription() );
        taskDto.setCreatedAt( task.getCreatedAt() );
        taskDto.setUpdatedAt( task.getUpdatedAt() );
        taskDto.setStatus( task.getStatus() );
        taskDto.setAuthor( task.getAuthor() );
        taskDto.setAssignee( task.getAssignee() );
        Set<User> set = task.getObservers();
        if ( set != null ) {
            taskDto.setObservers( new LinkedHashSet<User>( set ) );
        }

        return taskDto;
    }

    private String dtoAuthorId(TaskDto taskDto) {
        if ( taskDto == null ) {
            return null;
        }
        User author = taskDto.getAuthor();
        if ( author == null ) {
            return null;
        }
        String id = author.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String dtoAssigneeId(TaskDto taskDto) {
        if ( taskDto == null ) {
            return null;
        }
        User assignee = taskDto.getAssignee();
        if ( assignee == null ) {
            return null;
        }
        String id = assignee.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
