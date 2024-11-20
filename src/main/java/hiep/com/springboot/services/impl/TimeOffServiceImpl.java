package hiep.com.springboot.services.impl;

import hiep.com.springboot.common.error.BadRequestError;
import hiep.com.springboot.common.exception.ResponseException;
import hiep.com.springboot.models.entities.TimeOffEntity;
import hiep.com.springboot.repository.TimeOffRepository;
import hiep.com.springboot.security.utils.SecurityUtils;
import hiep.com.springboot.services.TimeOffService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TimeOffServiceImpl implements TimeOffService {
    private final TimeOffRepository timeOffRepository;

    public TimeOffServiceImpl(TimeOffRepository timeOffRepository) {
        this.timeOffRepository = timeOffRepository;
    }

    @Override
    public List<TimeOffEntity> getMyTimeOff() {
        Optional<String> loginId = SecurityUtils.getCurrentUserLoginId();

        if (loginId.isEmpty()) {
            throw new ResponseException(BadRequestError.LOGIN_FAIL_DUE_INACTIVE_ACCOUNT);
        }

        return timeOffRepository.findAll().stream().filter(t -> t.getEmployeeId().equals(UUID.fromString(loginId.get())))
                .collect(Collectors.toList());
    }
}
