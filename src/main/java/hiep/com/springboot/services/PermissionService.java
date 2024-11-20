package hiep.com.springboot.services;

import hiep.com.springboot.models.entities.PermissionEntity;

import java.util.List;

public interface PermissionService {
    List<PermissionEntity> findAll();
}
