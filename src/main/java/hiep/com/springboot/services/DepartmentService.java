package hiep.com.springboot.services;

import hiep.com.springboot.models.entities.DepartmentEntity;
import hiep.com.springboot.payload.request.DepartmentCreateOrUpdateRequest;
import hiep.com.springboot.payload.response.DepartmentResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DepartmentService {
    DepartmentEntity create(DepartmentCreateOrUpdateRequest request);

    DepartmentEntity update(String id, DepartmentCreateOrUpdateRequest request);

    boolean delete(String id);

    List<DepartmentResponse> getAll();

    DepartmentResponse detail(String id);

    boolean deleteUser(String departmentId, String userId);

    boolean addUser(String departmentId, List<String> userIds);

    void exportToExcel(HttpServletResponse response);
}
