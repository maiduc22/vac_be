package hiep.com.springboot.services;

import hiep.com.springboot.payload.response.UserActivityInfo;

import java.util.List;

public interface SessionService {
    List<UserActivityInfo> getAllSessions();
}
