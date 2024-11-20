package hiep.com.springboot.repository;

import hiep.com.springboot.models.entities.AttendanceEntity;
import hiep.com.springboot.payload.response.AttendanceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, UUID> {
    @Query(value = "SELECT * FROM `attendance` a where date(a.start) = CURRENT_DATE() and a.employee_id = :id", nativeQuery = true)
    Optional<AttendanceEntity> findByEmployeeIdAndDate(String id);

    @Query("SELECT NEW hiep.com.springboot.payload.response.AttendanceResponse(a.id, a.start, a.end, a.note, a.employeeId, u.fullName) " +
            "FROM AttendanceEntity a " +
            "JOIN UserEntity u ON a.employeeId = u.id " +
            "ORDER BY u.fullName, a.start")
    List<AttendanceResponse> findAllE();

    @Query("from AttendanceEntity a where a.employeeId = :employeeId order by a.start")
    List<AttendanceEntity> getMyAttendance(UUID employeeId);
}
