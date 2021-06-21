package com.alchemistry.conrollers;

import com.alchemistry.dto.requsestdto.AuthRequest;
import com.alchemistry.dto.requsestdto.RegistrationRequest;
import com.alchemistry.dto.responsedto.LoginResponse;
import com.alchemistry.services.UserService;
import com.alchemistry.services.impl.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/alchemy/api/v1/auth/")
public class AuthRestController {

    private final UserService userService;
    private final LoginService loginService;

    @Autowired
    public AuthRestController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest requestDto) {
        try {
            return ResponseEntity.ok(loginService.toLoginResponse(requestDto));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("register")
    public ResponseEntity.BodyBuilder register(@RequestBody RegistrationRequest requestDto) {
        try {
            userService.register(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException("Saving user ERROR");
        }
    }
}
