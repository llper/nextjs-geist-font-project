package com.company.timetracker.repository;

import com.company.timetracker.entity.Employee;
import com.company.timetracker.entity.Task;
import com.company.timetracker.entity.TimeEntry;
import com.company.timetracker.entity.TimeEntryStatus;
import com.company.timetracker.entity.TimeEntryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {

    List<TimeEntry> findByEmployee(Employee employee);

    List<TimeEntry> findByEmployeeId(Long employeeId);

    List<TimeEntry> findByTask(Task task);

    List<TimeEntry> findByTaskId(Long taskId);

    List<TimeEntry> findByType(TimeEntryType type);

    List<TimeEntry> findByStatus(TimeEntryStatus status);

    @Query("SELECT te FROM TimeEntry te WHERE te.employee.id = :employeeId AND te.entryDate BETWEEN :startDate AND :endDate")
    List<TimeEntry> findByEmployeeIdAndEntryDateBetween(@Param("employeeId") Long employeeId, 
                                                        @Param("startDate") LocalDateTime startDate, 
                                                        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT te FROM TimeEntry te WHERE te.entryDate BETWEEN :startDate AND :endDate")
    List<TimeEntry> findByEntryDateBetween(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT te FROM TimeEntry te WHERE te.employee.id = :employeeId AND te.type = :type AND te.entryDate BETWEEN :startDate AND :endDate")
    List<TimeEntry> findByEmployeeIdAndTypeAndEntryDateBetween(@Param("employeeId") Long employeeId,
                                                              @Param("type") TimeEntryType type,
                                                              @Param("startDate") LocalDateTime startDate,
                                                              @Param("endDate") LocalDateTime endDate);

    @Query("SELECT te FROM TimeEntry te WHERE te.task.project.id = :projectId AND te.entryDate BETWEEN :startDate AND :endDate")
    List<TimeEntry> findByProjectIdAndEntryDateBetween(@Param("projectId") Long projectId,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(te.hours) FROM TimeEntry te WHERE te.employee.id = :employeeId AND te.type = :type AND te.entryDate BETWEEN :startDate AND :endDate")
    Double sumHoursByEmployeeIdAndTypeAndEntryDateBetween(@Param("employeeId") Long employeeId,
                                                         @Param("type") TimeEntryType type,
                                                         @Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(te.hours) FROM TimeEntry te WHERE te.task.id = :taskId")
    Double sumHoursByTaskId(@Param("taskId") Long taskId);

    @Query("SELECT SUM(te.hours) FROM TimeEntry te WHERE te.task.project.id = :projectId")
    Double sumHoursByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT te FROM TimeEntry te WHERE te.status = 'PENDING' ORDER BY te.entryDate DESC")
    List<TimeEntry> findPendingTimeEntries();

    @Query("SELECT te FROM TimeEntry te WHERE te.employee.id = :employeeId AND te.status = :status ORDER BY te.entryDate DESC")
    List<TimeEntry> findByEmployeeIdAndStatusOrderByEntryDateDesc(@Param("employeeId") Long employeeId, 
                                                                 @Param("status") TimeEntryStatus status);

    @Query("SELECT COUNT(te) FROM TimeEntry te WHERE te.status = :status")
    long countByStatus(@Param("status") TimeEntryStatus status);

    @Query("SELECT COUNT(te) FROM TimeEntry te WHERE te.employee.id = :employeeId AND te.status = :status")
    long countByEmployeeIdAndStatus(@Param("employeeId") Long employeeId, @Param("status") TimeEntryStatus status);

    @Query("SELECT te FROM TimeEntry te WHERE te.employee.id = :employeeId AND DATE(te.entryDate) = DATE(:date)")
    List<TimeEntry> findByEmployeeIdAndDate(@Param("employeeId") Long employeeId, @Param("date") LocalDateTime date);

    @Query("SELECT DISTINCT DATE(te.entryDate) FROM TimeEntry te WHERE te.employee.id = :employeeId AND te.entryDate BETWEEN :startDate AND :endDate ORDER BY te.entryDate")
    List<LocalDateTime> findDistinctEntryDatesByEmployeeIdAndDateRange(@Param("employeeId") Long employeeId,
                                                                      @Param("startDate") LocalDateTime startDate,
                                                                      @Param("endDate") LocalDateTime endDate);
}
