package hiep.com.springboot.services.impl;

import hiep.com.springboot.models.entities.AttendanceEntity;
import hiep.com.springboot.models.entities.UserEntity;
import hiep.com.springboot.payload.response.AttendanceResponse;
import hiep.com.springboot.repository.AttendanceRepository;
import hiep.com.springboot.repository.UserRepository;
import hiep.com.springboot.security.utils.SecurityUtils;
import hiep.com.springboot.services.AttendanceService;
import hiep.com.springboot.utils.ExcelUtils;
import hiep.com.springboot.utils.StringPool;
import hiep.com.springboot.common.error.BadRequestError;
import hiep.com.springboot.common.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final UserRepository userRepository;

    private final AttendanceRepository attendanceRepository;

    private static final String TEMPLATE_ATTENDANCE_FILE_PATH = "templates/excel/Template_ATTENDANCE.xlsx";
    private static final String TEMPLATE_ATTENDANCE_FILE_NAME = "Danh_sach_cham_cong";
    private static final int TEMPLATE_ATTENDANCE_SHEET_NUMBER = 0;
    private static final int ATTENDANCE_START_ROW = 2;

    @Override
    public boolean checkIn() {
        String loginId = SecurityUtils.getCurrentUserLoginId().get();

        Optional<UserEntity> userOtp = userRepository.findById(UUID.fromString(loginId));

        if (userOtp.isEmpty()) {
            throw new ResponseException(BadRequestError.LOGIN_FAIL_DUE_INACTIVE_ACCOUNT);
        }

        UserEntity user = userOtp.get();

        AttendanceEntity attendanceEntity = new AttendanceEntity();

        attendanceEntity.setStart(LocalDateTime.now());
        attendanceEntity.setEmployeeId(user.getId());

        attendanceRepository.save(attendanceEntity);

        return true;
    }

    @Override
    public boolean checkOut(String note) {
        String loginId = SecurityUtils.getCurrentUserLoginId().get();

        Optional<UserEntity> userOtp = userRepository.findById(UUID.fromString(loginId));

        if (userOtp.isEmpty()) {
            throw new ResponseException(BadRequestError.LOGIN_FAIL_DUE_INACTIVE_ACCOUNT);
        }

        Optional<AttendanceEntity> attendanceOtp = attendanceRepository.findByEmployeeIdAndDate(loginId);

        if (attendanceOtp.isEmpty()) {
            throw new ResponseException(BadRequestError.CHECKOUT_FAIL);
        }

        AttendanceEntity attendanceEntity = attendanceOtp.get();

        attendanceEntity.setNote(note);
        attendanceEntity.setEnd(LocalDateTime.now());

        attendanceRepository.save(attendanceEntity);

        return true;
    }

    @Override
    public List<AttendanceResponse> getAll() {
        return attendanceRepository.findAllE();
    }

    @Override
    public void exportToExcel(HttpServletResponse response) {
        InputStream data = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(TEMPLATE_ATTENDANCE_FILE_PATH));

        List<AttendanceResponse> attendanceEntities = attendanceRepository.findAllE();

        XSSFWorkbook workbook;

        try {
            workbook = new XSSFWorkbook(data);
        } catch (IOException e) {
            throw new ResponseException(BadRequestError.OPEN_DEPARTMENT_TEMPLATE_FAIL);
        }

        Sheet attendanceSheet = workbook.getSheetAt(TEMPLATE_ATTENDANCE_SHEET_NUMBER);

        if (Objects.isNull(attendanceSheet)) {
            throw new ResponseException(BadRequestError.ATTENDANCE_SHEET_TEMPLATE_REQUIRED);
        }

        // Lấy tháng hiện tại
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();

        // Tạo tiêu đề
        String title = "Quản lý chấm công tháng " + currentMonth;

        // Thêm tiêu đề vào file Excel
        Row titleRow = attendanceSheet.getRow(0);
        CellStyle titleCellStyle = ExcelUtils.createTitleCellStyle(workbook); // Tạo cell style cho tiêu đề
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(titleCellStyle);

        Map<String, Double> totalMap = new HashMap<>();

        for (int i = 0; i < attendanceEntities.size(); i++) {
            AttendanceResponse attendance = attendanceEntities.get(i);

            Row currentRow = attendanceSheet.createRow(ATTENDANCE_START_ROW + i);

            CellStyle cellStyle = ExcelUtils.createValueCellStyle(workbook);

            // Tính số công dựa trên công thức (end - start >= 8 => 1, end - start >= 4 và <= 8 => 0.5)
            double workingDays = 0;
            if (attendance.getStart() != null && attendance.getEnd() != null) {
                LocalTime startTime = attendance.getStart().toLocalTime();
                LocalTime endTime = attendance.getEnd().toLocalTime();
                Duration duration = Duration.between(startTime, endTime);
                double workingHours = duration.toHours(); // Chuyển khoảng thời gian thành số giờ

                if (workingHours >= 8) {
                    workingDays = 1;
                } else if (workingHours >= 4 && workingHours < 8) {
                    workingDays = 0.5;
                } else {
                    workingDays = 0;
                }

                double currentWorkingDays = totalMap.getOrDefault(attendance.getEmployeeId().toString(), 0.0);
                totalMap.put(attendance.getEmployeeId().toString(), currentWorkingDays + workingDays);
            }

            ExcelUtils.createCell(currentRow, 0, cellStyle, String.valueOf(i + 1));
            ExcelUtils.createCell(currentRow, 1, cellStyle, attendance.getEmployeeName());
            ExcelUtils.createCell(currentRow, 2, cellStyle, attendance.getStart() != null ? attendance.getStart().toLocalDate().toString() : "");
            ExcelUtils.createCell(currentRow, 3, cellStyle, attendance.getStart() != null ? attendance.getStart().format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString() : "");
            ExcelUtils.createCell(currentRow, 4, cellStyle, attendance.getEnd() != null ? attendance.getEnd().format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString() : "");
            ExcelUtils.createCell(currentRow, 5, cellStyle, attendance.getNote());
            ExcelUtils.createCell(currentRow, 6, cellStyle, String.valueOf(workingDays));
        }

        int rowIndex = ATTENDANCE_START_ROW; // Dòng bắt đầu của bảng chấm công

        for (Map.Entry<String, Double> entry : totalMap.entrySet()) {
            String employeeId = entry.getKey();
            Double totalWorkingDays = entry.getValue();

            UserEntity user = userRepository.getById(UUID.fromString(employeeId));
            // Tìm các dòng có cùng tên nhân viên và gộp lại
            List<Row> rowsToMerge = new ArrayList<>();
            while (rowIndex < ATTENDANCE_START_ROW + attendanceEntities.size()) {
                Row row = attendanceSheet.getRow(rowIndex);
                if (row.getCell(1).getStringCellValue().equals(user.getFullName())) {
                    rowsToMerge.add(row);
                }
                rowIndex++;
            }

            // Gộp các dòng có cùng tên nhân viên lại thành một dòng duy nhất
            if (!rowsToMerge.isEmpty()) {
                Row firstRow = rowsToMerge.get(0);
                if (rowsToMerge.size() > 1) { // Kiểm tra có ít nhất 2 dòng cùng tên nhân viên mới thực hiện gộp
                    CellStyle cellStyle = ExcelUtils.createValueCellStyle(workbook);
                    ExcelUtils.createCell(firstRow, 7, cellStyle, String.valueOf(totalWorkingDays));
                    attendanceSheet.addMergedRegion(new CellRangeAddress(firstRow.getRowNum(), firstRow.getRowNum() + rowsToMerge.size() - 1, 7, 7));
                } else { // Nếu không có ít nhất 2 dòng, chỉ cập nhật giá trị trong ô đầu tiên
                    CellStyle cellStyle = ExcelUtils.createValueCellStyle(workbook);
                    ExcelUtils.createCell(firstRow, 7, cellStyle, String.valueOf(totalWorkingDays));
                }
            }

            rowIndex = ATTENDANCE_START_ROW;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            String filename = TEMPLATE_ATTENDANCE_FILE_NAME + StringPool.UNDERLINE + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
                    StringPool.XLSX;
            workbook.write(out);
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Content-Disposition", " attachment; filename=" + filename);
            IOUtils.copy(new ByteArrayInputStream(out.toByteArray()), response.getOutputStream());
            response.flushBuffer();
            out.close();
            workbook.close();
        } catch (IOException e) {
            throw new ResponseException(BadRequestError.DOWNLOAD_TEMPLATE_TEMPLATE_FAIL);

        }
    }

    @Override
    public List<AttendanceEntity> myAttendance() {
        String loginId = SecurityUtils.getCurrentUserLoginId().get();

        Optional<UserEntity> userOtp = userRepository.findById(UUID.fromString(loginId));

        if (userOtp.isEmpty()) {
            throw new ResponseException(BadRequestError.LOGIN_FAIL_DUE_INACTIVE_ACCOUNT);
        }

        return attendanceRepository.getMyAttendance(userOtp.get().getId());
    }
}
