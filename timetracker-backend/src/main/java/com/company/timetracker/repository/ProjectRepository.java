package com.company.timetracker.repository;

import com.company.timetracker.entity.Employee;
import com.company.timetracker.entity.Project;
import com.company.timetracker.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByCode(String code);

    List<Project> findByStatus(ProjectStatus status);

    List<Project> findByClientName(String clientName);

    @Query("SELECT p FROM Project p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Project> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT p FROM Project p WHERE p.status = 'ACTIVE' ORDER BY p.name")
    List<Project> findAllActiveOrderByName();

    @Query("SELECT p FROM Project p WHERE p.startDate <= :date AND (p.endDate IS NULL OR p.endDate >= :date)")
    List<Project> findActiveOnDate(@Param("date") LocalDateTime date);

    @Query("SELECT p FROM Project p JOIN p.assignedEmployees e WHERE e = :employee AND p.status = :status")
    List<Project> findByAssignedEmployeeAndStatus(@Param("employee") Employee employee, @Param("status") ProjectStatus status);

    @Query("SELECT p FROM Project p JOIN p.assignedEmployees e WHERE e.id = :employeeId")
    List<Project> findByAssignedEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = :status")
    long countByStatus(@Param("status") ProjectStatus status);

    @Query("SELECT p FROM Project p WHERE p.endDate < :date AND p.status != 'COMPLETED'")
    List<Project> findOverdueProjects(@Param("date") LocalDateTime date);

    boolean existsByCode(String code);

    @Query("SELECT DISTINCT p.clientName FROM Project p WHERE p.clientName IS NOT NULL ORDER BY p.clientName")
    List<String> findAllClientNames();
}
