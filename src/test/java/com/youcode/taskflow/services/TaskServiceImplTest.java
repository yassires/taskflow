package com.youcode.taskflow.services;

import com.youcode.taskflow.dto.TaskDto;
import com.youcode.taskflow.entities.Tag;
import com.youcode.taskflow.entities.Task;
import com.youcode.taskflow.entities.User;
import com.youcode.taskflow.entities.enums.Role;
import com.youcode.taskflow.entities.enums.TaskStatus;
import com.youcode.taskflow.repository.TaskRepository;
import com.youcode.taskflow.repository.TagRepository;
import com.youcode.taskflow.repository.UserRepository;
import com.youcode.taskflow.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskServiceImpl taskService;


    @Test
    void testCreateTask() {
        int createdById = 1;
        TaskDto taskDto = createTaskDto(createdById);
        User createdBy = createUser(createdById);
        List<Integer> tagIds = Arrays.asList(1, 2);
        List<Tag> tags = Arrays.asList(new Tag(1, "Tag1"), new Tag(2, "Tag2"));

        Mockito.when(userRepository.findById(createdById)).thenReturn(Optional.of(createdBy));
        Mockito.when(tagRepository.findAllById(tagIds)).thenReturn(tags);
        Mockito.when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            savedTask.setId(1);
            return savedTask;
        });
        Mockito.when(modelMapper.map(any(), any())).thenCallRealMethod();


        TaskDto createdTaskDto = taskService.createTask(taskDto);

        assertNotNull(createdTaskDto);
        assertEquals(createdById, createdTaskDto.getCreatedBy().getId());

    }

    private TaskDto createTaskDto(int createdById) {
        TaskDto taskDto = new TaskDto();
        taskDto.setCreatedBy(createUser(createdById));
        taskDto.setStartDate(LocalDate.now().plusDays(4));
        taskDto.setEndDate(LocalDate.now().plusDays(10));
        taskDto.setTag(Arrays.asList(1, 2));
        return taskDto;
    }

    private User createUser(int userId) {
        User user = new User();
        user.setId(userId);
        user.setRole(Role.ADMIN);
        return user;
    }






}
