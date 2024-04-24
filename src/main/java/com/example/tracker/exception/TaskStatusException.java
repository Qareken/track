package com.example.tracker.exception;

public class TaskStatusException extends RuntimeException{
    private String message;

    public TaskStatusException(String message) {
        this.message = message;
    }
}
