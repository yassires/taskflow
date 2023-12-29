package com.youcode.taskflow.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class TaskToTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "task")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "tag")
    private Tag tag;
}
