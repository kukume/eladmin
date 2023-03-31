package me.zhengjie.modules.security.rest;

import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kuku.utils.MD5Utils;
import me.kuku.utils.MyUtils;
import me.kuku.utils.StringUtils;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousDeleteMapping;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.modules.security.service.dto.AuthUserDto;
import me.zhengjie.modules.security.service.OnlineUserService;
import me.zhengjie.modules.system.domain.Menu;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {
    private final OnlineUserService onlineUserService;
    private final UserService userService;

    @Log("用户登录")
    @AnonymousPostMapping(value = "/login")
    public Object login(@Validated @RequestBody AuthUserDto authUser, HttpServletRequest request) {
        String username = authUser.getUsername();
        if (Objects.equals(username, "admin")) {
            User admin = userService.findByName("admin");
            if (admin == null) {
                User user = new User();
                user.setUsername("admin");
                String salt = MyUtils.randomLetter(5);
                user.setSalt(salt);
                user.setPassword(MD5Utils.toMD5("123456" + salt));
                userService.save(user);
            }
        }
        User userEntity = userService.findByName(username);
        String password = MD5Utils.toMD5(authUser.getPassword() + userEntity.getSalt());
        String savePassword = userEntity.getPassword();
        if (!Objects.equals(password, savePassword)) throw new IllegalStateException("账号或密码错误");
        StpUtil.login(userEntity.getId());
        String token = StpUtil.getTokenValue();
        onlineUserService.save(userEntity, token, request);
        Map<String, Object> userMap = new HashMap<>();
        List<String> roles = userEntity.getRoles().stream().flatMap(it -> it.getMenus().stream()).map(Menu::getPermission).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        userMap.put("user", userEntity);
        userMap.put("roles", roles);
        Map<String, Object> authInfo = new HashMap<>(2) {{
            put("token", token);
            put("user", userMap);
        }};
        StpUtil.getSession().set("user", userEntity);
        StpUtil.getSession().set("userEl", userMap);
        return ResponseEntity.ok(authInfo);
    }


    @GetMapping(value = "/info")
    public ResponseEntity<Object> getUserInfo() {
        return ResponseEntity.ok(StpUtil.getSession().get("userEl"));
    }



    @AnonymousDeleteMapping(value = "/logout")
    public ResponseEntity<Object> logout() {
        StpUtil.logout();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
