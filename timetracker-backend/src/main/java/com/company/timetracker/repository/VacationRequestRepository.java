package com.company.timetracker.repository;

import com.company.timetracker.entity.Employee;
import com.company.timetracker.entity.VacationRequest;
import com.company.timetracker.entity.VacationStatus;
import com.company.timetracker.entity.VacationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {

    List<VacationRequest> findByEmployee(Employee employee);

    List<VacationRequest> findByEmployeeId(Long employeeId);

    List<VacationRequest> findByStatus(VacationStatus status);

    List<VacationRequest> findByType(VacationType type);

    @Query("SELECT vr FROM VacationRequest vr WHERE vr.employee.id = :employeeId AND vr.status = :status ORDER BY vr.startDate DESC")
    List<VacationRequest> findByEmployeeIdAndStatusOrderByStartDateDesc(@Param("employeeId") Long employeeId, 
                                                                       @Param("status") VacationStatus status);

    @Query("SELECT vr FROM VacationRequest vr WHERE vr.startDate BETWEEN :startDate AND :endDate OR vr.endDate BETWEEN :startDate AND :endDate")
    List<VacationRequest> findByDateRange(@Param("startDate") LocalDate startDate, 
                                         @Param("endDate") LocalDate endDate);

    @Query("SELECT vr FROM VacationRequest vr WHERE vr.employee.id = :employeeId AND ((vr.startDate BETWEEN :startDate AND :endDate) OR (vr.endDate BETWEEN :startDate AND :endDate) OR (vr.startDate <= :startDate AND vr.endDate >= :endDate))")
    List<VacationRequest> findByEmployeeIdAndOverlappingDates(@Param("employeeId") Long employeeId,
                                                             @Param("startDate") LocalDate startDate,
                                                             @Param("endDate") LocalDate endDate);

    @Query("SELECT vr FROM VacationRequest vr WHERE vr.status = 'PENDING' ORDER BY vr.createdAt ASC")
    List<VacationRequest> findPendingRequestsOrderByCreatedAt();

    @Query("SELECT vr FROM VacationRequest vr WHERE vr.status = 'APPROVED' AND vr.startDate <= :date AND vr.endDate >= :date")
    List<VacationRequest> findApprovedVacationsOnDate(@Param("date") LocalDate date);

    @Query("SELECT vr FROM VacationRequest vr WHERE vr.employee.id = :employeeId AND vr.status = 'APPROVED' AND YEAR(vr.startDate) = :year")
    List<VacationRequest> findApprovedVacationsByEmployeeIdAndYear(@Param("employeeId") Long employeeId, 
                                                                  @Param("year") int year);

    @Query("SELECT SUM(DATEDIFF(vr.endDate, vr.startDate) + 1) FROM VacationRequest vr WHERE vr.employee.id = :employeeId AND vr.status = 'APPROVED' AND vr.type = :type AND YEAR(vr.startDate) = :year")
    Integer sumApprovedDaysByEmployeeIdAndTypeAndYear(@Param("employeeId") Long employeeId,
                                                     @Param("type") VacationType type,
                                                     @Param("year") int year);

    @Query("SELECT COUNT(vr) FROM VacationRequest vr WHERE vr.status = :status")
    long countByStatus(@Param("status") VacationStatus status);

    @Query("SELECT COUNT(vr) FROM VacationRequest vr WHERE vr.employee.id = :employeeId AND vr.status = :status")
    long countByEmployeeIdAndStatus(@Param("employeeId") Long employeeId, @Param("status") VacationStatus status);

    @Query("SELECT vr FROM VacationRequest vr WHERE vr.approvalSkipped = true ORDER BY vr.createdAt DESC")
    List<VacationRequest> findSkippedApprovals();

    @Query("SELECT vr FROM VacationRequest vr WHERE vr.startDate < :currentDate AND vr.status = 'PENDING'")
    List<VacationRequest> findExpiredPendingRequests(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT vr FROM VacationRequest vr WHERE vr.employee.id = :employeeId AND vr.status IN ('PENDING', 'APPROVED') ORDER BY vr.startDate ASC")
    List<VacationRequest> findUpcomingVacationsByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT DISTINCT vr.employee FROM VacationRequest vr WHERE vr.status = 'APPROVED' AND vr.startDate <= :date AND vr.endDate >= :date")
    List<Employee> findEmployeesOnVacationOnDate(@Param("date") LocalDate date);
}
