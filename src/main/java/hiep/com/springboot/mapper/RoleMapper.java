package hiep.com.springboot.mapper;

import hiep.com.springboot.models.entities.RoleEntity;
import hiep.com.springboot.payload.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleResponse from(RoleEntity entity);
}
