package hiep.com.springboot.services;

import hiep.com.springboot.payload.request.UserUpdateRequest;
import hiep.com.springboot.payload.response.UserResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    List<UserResponse> findAll();

    UserResponse update(String id, UserUpdateRequest request);

    UserResponse getById(String id);

    void exportToExcel(HttpServletResponse response);
}
