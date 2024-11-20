package hiep.com.springboot.services;

import hiep.com.springboot.models.entities.TimeOffRequestEntity;
import hiep.com.springboot.payload.request.TimeOffRequest;
import hiep.com.springboot.payload.response.TimeoffRequestResponse;
import hiep.com.springboot.common.enums.TimeOffStatus;

import java.util.List;
import java.util.UUID;

public interface TimeOffRequestService {
    TimeOffRequestEntity create(TimeOffRequest request);

    List<TimeOffRequestEntity> myRequest();

    List<TimeoffRequestResponse> getAll();

    boolean changeRequestStatus(UUID requestId, TimeOffStatus status);
}
