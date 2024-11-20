package hiep.com.springboot.security.services;

import hiep.com.springboot.common.UserAuthority;

public interface AuthorityService {

    UserAuthority getUserAuthority(String userId);
}
