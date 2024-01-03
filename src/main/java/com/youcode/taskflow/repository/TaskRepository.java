package com.youcode.taskflow.repository;

import com.youcode.taskflow.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query(value = "SELECT * FROM Task t WHERE t.end_date < :currentDate AND t.status IN (:statuses)", nativeQuery = true)
    List<Task> findTasksToUpdateStatus(@Param("currentDate") LocalDate currentDate, @Param("statuses") List<String> statuses);


    @Modifying
    @Query(value = "UPDATE Task SET status = :newStatus WHERE id = :taskId", nativeQuery = true)
    void updateTaskStatus(@Param("taskId") Integer taskId, @Param("newStatus") String newStatus);

}

