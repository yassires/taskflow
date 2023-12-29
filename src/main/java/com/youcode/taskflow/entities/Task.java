package com.youcode.taskflow.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.youcode.taskflow.entities.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;


}
