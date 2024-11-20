package hiep.com.springboot.payload.response;

import hiep.com.springboot.models.entities.DepartmentEntity;
import hiep.com.springboot.models.entities.UserEntity;
import lombok.Data;

import java.util.List;

@Data
public class DepartmentResponse extends DepartmentEntity {
    private List<UserEntity> users;
}
