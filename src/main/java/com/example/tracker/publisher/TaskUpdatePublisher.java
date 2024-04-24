package com.example.tracker.publisher;

import com.example.tracker.dto.TaskDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class TaskUpdatePublisher {
    private final Sinks.Many<TaskDto> taskModeUpdateSink;

    @Autowired
    public TaskUpdatePublisher() {
        this.taskModeUpdateSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(TaskDto itemModel) {
        taskModeUpdateSink.tryEmitNext(itemModel);
    }

    public Sinks.Many<TaskDto> getUpdatesSinks() {
        return taskModeUpdateSink;
    }
}
