package hiep.com.springboot.services;

import hiep.com.springboot.common.UserAuthority;
import hiep.com.springboot.models.entities.UserEntity;
import hiep.com.springboot.payload.request.ChangePwdRequest;
import hiep.com.springboot.payload.request.LoginRequest;
import hiep.com.springboot.payload.request.UserRegisterRequest;
import hiep.com.springboot.payload.response.UserResponse;

public interface AccountService {
    UserEntity register(UserRegisterRequest request);

    String login(LoginRequest request);

    UserAuthority myAuthorities();

    UserResponse getProfile();

    boolean changePwd(ChangePwdRequest request);

    boolean logout();
}
