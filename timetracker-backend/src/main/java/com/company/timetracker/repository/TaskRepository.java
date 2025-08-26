package com.company.timetracker.repository;

import com.company.timetracker.entity.Employee;
import com.company.timetracker.entity.Project;
import com.company.timetracker.entity.Task;
import com.company.timetracker.entity.TaskStatus;
import com.company.timetracker.entity.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProject(Project project);

    List<Task> findByProjectId(Long projectId);

    List<Task> findByAssignedEmployee(Employee employee);

    List<Task> findByAssignedEmployeeId(Long employeeId);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(TaskPriority priority);

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.code = :code")
    Optional<Task> findByProjectIdAndCode(@Param("projectId") Long projectId, @Param("code") String code);

    @Query("SELECT t FROM Task t WHERE t.assignedEmployee.id = :employeeId AND t.status IN :statuses")
    List<Task> findByAssignedEmployeeIdAndStatusIn(@Param("employeeId") Long employeeId, @Param("statuses") List<TaskStatus> statuses);

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.status = :status ORDER BY t.priority DESC, t.dueDate ASC")
    List<Task> findByProjectIdAndStatusOrderByPriorityAndDueDate(@Param("projectId") Long projectId, @Param("status") TaskStatus status);

    @Query("SELECT t FROM Task t WHERE t.dueDate < :date AND t.status NOT IN ('COMPLETED', 'CANCELLED')")
    List<Task> findOverdueTasks(@Param("date") LocalDateTime date);

    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :startDate AND :endDate AND t.status NOT IN ('COMPLETED', 'CANCELLED')")
    List<Task> findTasksDueBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) FROM Task t WHERE t")
    List<Task> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status")
    long countByStatus(@Param("status") TaskStatus status);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignedEmployee.id = :employeeId AND t.status = :status")
    long countByAssignedEmployeeIdAndStatus(@Param("employeeId") Long employeeId, @Param("status") TaskStatus status);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.id = :projectId AND t.status = :status")
    long countByProjectIdAndStatus(@Param("projectId") Long projectId, @Param("status") TaskStatus status);

    @Query("SELECT t FROM Task t WHERE t.status IN ('OPEN', 'IN_PROGRESS') ORDER BY t.priority DESC, t.dueDate ASC")
    List<Task> findActiveTasks();

    @Query("SELECT SUM(te.hours) FROM Task t JOIN t.timeEntries te WHERE t.id = :taskId")
    Double getTotalLoggedHours(@Param("taskId") Long taskId);
}
