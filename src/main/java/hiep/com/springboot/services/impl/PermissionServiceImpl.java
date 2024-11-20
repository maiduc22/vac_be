package hiep.com.springboot.services.impl;

import hiep.com.springboot.common.enums.ResourceCategory;
import hiep.com.springboot.models.entities.PermissionEntity;
import hiep.com.springboot.repository.PermissionRepository;
import hiep.com.springboot.services.PermissionService;
import hiep.com.springboot.config.i18n.LocaleStringService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    private final LocaleStringService localeStringService;

    @Override
    public List<PermissionEntity> findAll() {
        List<PermissionEntity> permissionEntities = this.permissionRepository.findAllActivated();

        ResourceCategory[] resourceCategories = ResourceCategory.values();

        permissionEntities.forEach(p -> {
            for (ResourceCategory perType : resourceCategories) {
                if (p.getResourceCode().equals(perType.getResourceCode())) {
                    p.setResourceName(this.localeStringService.getMessage(perType.getResourceName(), perType.getResourceName()));
                }
            }
            if (!StringUtils.hasLength(p.getResourceName())) {
                p.setResourceName(p.getResourceCode());
            }
        });

        return permissionEntities;
    }
}
