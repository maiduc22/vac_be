package hiep.com.springboot.services;

import hiep.com.springboot.models.entities.AttendanceEntity;
import hiep.com.springboot.payload.response.AttendanceResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AttendanceService {
    boolean checkIn();

    boolean checkOut(String note);

    List<AttendanceResponse> getAll();

    void exportToExcel(HttpServletResponse response);

    List<AttendanceEntity> myAttendance();
}
