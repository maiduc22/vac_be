package hiep.com.springboot.services;

import hiep.com.springboot.models.entities.TimeOffHistoryEntity;

import java.util.List;

public interface TimeOffHistoryService {
    List<TimeOffHistoryEntity> getAll();
}
