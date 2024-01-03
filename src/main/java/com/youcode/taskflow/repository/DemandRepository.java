package com.youcode.taskflow.repository;

import com.youcode.taskflow.entities.Task;
import com.youcode.taskflow.entities.Demand;
import com.youcode.taskflow.entities.enums.Action;
import com.youcode.taskflow.entities.enums.DemandStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Long> {

    @Query("SELECT COUNT(t) FROM Demand t WHERE t.oldUser.id = :userId " +
            "AND t.type = :deletionType AND EXTRACT(MONTH FROM t.demandDate) = EXTRACT(MONTH FROM :currentDate)")
    int countMonthlyDeletionTokens(
            @Param("userId") Long userId,
            @Param("deletionType") Action deletionType,
            @Param("currentDate") LocalDate currentDate
    );

    @Query("SELECT COUNT(td) FROM TokenDemand td WHERE td.user.id = :userId " +
            "AND td.type = :modificationType AND EXTRACT(MONTH FROM td.demandDate) = EXTRACT(MONTH FROM :currentDate)")
    int countMonthlyModificationTokens(
            @Param("userId") Long userId,
            @Param("modificationType") Action modificationType,
            @Param("currentDate") LocalDate currentDate
    );

    boolean existsByTask(Task task);
    boolean existsByTaskAndStatus(Task task, DemandStatus status);
    @Query("SELECT t FROM Demand t WHERE t.status = 'PENDING'")
    List<Demand> findPendingTokenRequests();
}
