package hiep.com.springboot.services.impl;

import hiep.com.springboot.common.error.BadRequestError;
import hiep.com.springboot.common.exception.ResponseException;
import hiep.com.springboot.models.entities.TimeOffHistoryEntity;
import hiep.com.springboot.repository.TimeOffHistoryRepository;
import hiep.com.springboot.security.utils.SecurityUtils;
import hiep.com.springboot.services.TimeOffHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TimeOffHistoryServiceImpl implements TimeOffHistoryService {
    private final TimeOffHistoryRepository timeOffHistoryRepository;

    public TimeOffHistoryServiceImpl(TimeOffHistoryRepository timeOffHistoryRepository) {
        this.timeOffHistoryRepository = timeOffHistoryRepository;
    }

    @Override
    public List<TimeOffHistoryEntity> getAll() {
        Optional<String> loginId = SecurityUtils.getCurrentUserLoginId();

        if (loginId.isEmpty()) {
            throw new ResponseException(BadRequestError.LOGIN_FAIL_DUE_INACTIVE_ACCOUNT);
        }

        UUID employeeId = UUID.fromString(loginId.get());

        List<TimeOffHistoryEntity> timeOffHistoryEntities = timeOffHistoryRepository.findAllByEmployeeId(employeeId);

        return timeOffHistoryEntities;
    }
}
