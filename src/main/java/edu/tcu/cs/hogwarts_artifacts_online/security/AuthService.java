package edu.tcu.cs.hogwarts_artifacts_online.security;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import edu.tcu.cs.hogwarts_artifacts_online.client.rediscache.RedisCacheClient;
import edu.tcu.cs.hogwarts_artifacts_online.hogwartsuser.HogwartsUser;
import edu.tcu.cs.hogwarts_artifacts_online.hogwartsuser.MyUserPrincipal;
import edu.tcu.cs.hogwarts_artifacts_online.hogwartsuser.converter.UserToUserDtoConverter;
import edu.tcu.cs.hogwarts_artifacts_online.hogwartsuser.dto.UserDto;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;

    private final UserToUserDtoConverter userToUserDtoConverter;

    private final RedisCacheClient redisCacheClient;

    public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter,
            RedisCacheClient redisCacheClient) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.redisCacheClient = redisCacheClient;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        HogwartsUser hogwartsUser = principal.getHogwartsUser();
        UserDto userDto = this.userToUserDtoConverter.convert(hogwartsUser);
        String token = this.jwtProvider.createToken(authentication);
        // save the token in Redis.Key is "whitelist:{userId}",value is token.
        this.redisCacheClient.set("whitelist:" + hogwartsUser.getId(), token, 2, TimeUnit.HOURS);
        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("userInfo", userDto);
        loginResultMap.put("token", token);
        return loginResultMap;
    }

}
