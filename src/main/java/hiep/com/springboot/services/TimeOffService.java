package hiep.com.springboot.services;

import hiep.com.springboot.models.entities.TimeOffEntity;

import java.util.List;

public interface TimeOffService {
    List<TimeOffEntity> getMyTimeOff();
}
