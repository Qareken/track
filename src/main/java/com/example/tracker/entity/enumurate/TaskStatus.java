package com.example.tracker.entity.enumurate;

import java.text.MessageFormat;

public enum TaskStatus {
    TODO("todo"), IN_PROGRESS("proccess"), DONE("done");
    private final String label;

    TaskStatus(String label) {
        this.label=label;
    }

    public String getLabel() {
        return label;
    }
    public static TaskStatus fromLabel(String label){
        for(TaskStatus status: TaskStatus.values()){
            if(status.getLabel().equalsIgnoreCase(label)){
                return status;
            }
        }
        throw new RuntimeException(MessageFormat.format("No category with label {} not found!!!",label));
    }
}
