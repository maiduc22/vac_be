package hiep.com.springboot.payload.response;

import hiep.com.springboot.models.entities.AttendanceEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AttendanceResponse {
    private UUID id;

    private LocalDateTime start;

    private LocalDateTime end;

    private String note;

    private UUID employeeId;
    private String employeeName;
}
