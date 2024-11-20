package hiep.com.springboot.services.impl;

import hiep.com.springboot.models.entities.UserEntity;
import hiep.com.springboot.payload.response.EventResponse;
import hiep.com.springboot.repository.UserRepository;
import hiep.com.springboot.services.EventService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private final UserRepository userRepository;

    public EventServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<EventResponse> getEvents() {
        List<UserEntity> users = userRepository.findAll();

        List<EventResponse> res = new ArrayList<>();

        for (UserEntity user : users) {
            EventResponse eventResponse = new EventResponse();

            //convert instant to localDate
            LocalDate joinDate = LocalDateTime.ofInstant(user.getCreatedAt(), ZoneOffset.UTC).toLocalDate();

            eventResponse.setFullName(user.getFullName() == null ? "" : user.getFullName());
            eventResponse.setJoinDate(joinDate);
            eventResponse.setDayOfBirth(user.getDayOfBirth());
            eventResponse.setAvatarFileId(user.getAvatarFileId());

            res.add(eventResponse);
        }

        return res;
    }
}
