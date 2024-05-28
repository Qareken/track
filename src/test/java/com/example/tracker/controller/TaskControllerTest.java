package com.example.tracker.controller;

import com.example.tracker.AbstractTest;
import com.example.tracker.dto.TaskDto;
import com.example.tracker.entity.Task;
import com.example.tracker.entity.User;
import com.example.tracker.entity.enumurate.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;


import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class TaskControllerTest extends AbstractTest {
    @Test
    public void whenGetAllTask_thenReturnListOfFromDataBase() {
        UserDetails userDetails = customUserDetailsService.findByUsername("paul@example.com").block();
        assert userDetails != null;
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
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
        List<TaskDto> expectedList = List.of(
                task2, task1);
////        var responseList = webTestClient.get().uri("/api/v1/track/task")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(TaskDto.class)
//                .returnResult().getResponseBody();
////        assertThat(responseList)
//                .isNotNull()
//                .hasSize(2)
//                .extracting(TaskDto::getId)
//                .containsExactlyInAnyOrderElementsOf(expectedList.stream().map(TaskDto::getId).collect(Collectors.toSet()));
    }
    @Test
    public void whetGetTaskById_thenReturnTask(){

    }
}
