package com.company.timetracker.repository;

import com.company.timetracker.entity.Employee;
import com.company.timetracker.entity.EmployeeRole;
import com.company.timetracker.entity.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByKeycloakId(String keycloakId);

    Optional<Employee> findByEmail(String email);

    List<Employee> findByStatus(EmployeeStatus status);

    List<Employee> findByRole(EmployeeRole role);

    List<Employee> findByDepartment(String department);

    List<Employee> findByStatusAndRole(EmployeeStatus status, EmployeeRole role);

    @Query("SELECT e FROM Employee e WHERE e.status = :status AND e.role IN :roles")
    List<Employee> findByStatusAndRoleIn(@Param("status") EmployeeStatus status, @Param("roles") List<EmployeeRole> roles);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Employee> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT e FROM Employee e WHERE e.status = 'ACTIVE' ORDER BY e.lastName, e.firstName")
    List<Employee> findAllActiveOrderByName();

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.status = :status")
    long countByStatus(@Param("status") EmployeeStatus status);

    boolean existsByEmail(String email);

    boolean existsByKeycloakId(String keycloakId);
}
