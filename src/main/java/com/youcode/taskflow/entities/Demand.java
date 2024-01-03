package com.youcode.taskflow.entities;


import com.youcode.taskflow.entities.enums.Action;
import com.youcode.taskflow.entities.enums.DemandStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class Demand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "old_user")
    private User oldUser;

    @ManyToOne
    @JoinColumn(name = "new_user")
    private User newUser;

    @Enumerated(EnumType.STRING)
    private Action action;

    @Enumerated(EnumType.STRING)
    private DemandStatus status;
}
