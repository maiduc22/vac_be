package hiep.com.springboot.services;

import hiep.com.springboot.payload.response.EventResponse;

import java.util.List;

public interface EventService {
    List<EventResponse> getEvents();
}
