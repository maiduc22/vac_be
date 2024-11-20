package hiep.com.springboot.mapper;

import hiep.com.springboot.models.entities.TimeOffRequestEntity;
import hiep.com.springboot.payload.request.TimeOffRequest;
import hiep.com.springboot.payload.response.TimeoffRequestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TimeOffRequestMapper {
    TimeOffRequestMapper INSTANCE = Mappers.getMapper(TimeOffRequestMapper.class);

    TimeOffRequestEntity from(TimeOffRequest request);

    TimeoffRequestResponse from(TimeOffRequestEntity entity);
}
