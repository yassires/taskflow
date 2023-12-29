package com.youcode.taskflow.entities;


import com.youcode.taskflow.entities.enums.Action;
import com.youcode.taskflow.entities.enums.TaskReplacementStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class TaskReplacement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "task")
    private Task task;

    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "old_user")
    private User oldUser;

    @ManyToOne
    @JoinColumn(name = "new_user")
    private User newUser;

    @Enumerated(EnumType.STRING)
    private Action action;

    @Enumerated(EnumType.STRING)
    private TaskReplacementStatus status;
}
