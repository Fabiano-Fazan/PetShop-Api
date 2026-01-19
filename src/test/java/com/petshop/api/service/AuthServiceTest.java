package com.petshop.api.service;

import com.petshop.api.dto.request.CreateLoginDto;
import com.petshop.api.dto.request.CreateRegisterDto;
import com.petshop.api.dto.response.AuthResponseDto;
import com.petshop.api.model.entities.User;
import com.petshop.api.repository.UserRepository;
import com.petshop.api.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;


    @Test
    @DisplayName("Should register user successfully and return token")
    void register_ShouldRegisterUser_WhenDataIsValid() {

        CreateRegisterDto registerDto = new CreateRegisterDto();
        registerDto.setEmail("test@email.com");
        registerDto.setName("Test User");
        registerDto.setPassword("plainPassword");
        User savedUser = new User();
        savedUser.setEmail("test@email.com");
        String encodedPassword = "encodedPassword";
        String jwtToken = "jwt-token-xyz";

        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtToken);

        AuthResponseDto result = authService.register(registerDto);

        assertThat(result).isNotNull();
        assertThat(result.getAcessToken()).isEqualTo(jwtToken);

        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(argThat(user ->
                user.getPassword().equals(encodedPassword) &&
                        user.getEmail().equals(registerDto.getEmail())
        ));
    }


    @Test
    @DisplayName("Should login successfully and return token")
    void login_ShouldReturnToken_WhenCredentialsAreValid() {

        CreateLoginDto loginDto = new CreateLoginDto();
        loginDto.setEmail("test@email.com");
        loginDto.setPassword("password123");
        User user = new User();
        user.setEmail("test@email.com");
        String jwtToken = "jwt-token-abc";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(jwtToken);

        AuthResponseDto result = authService.login(loginDto);

        assertThat(result).isNotNull();
        assertThat(result.getAcessToken()).isEqualTo(jwtToken);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }


    @Test
    @DisplayName("Should throw BadCredentialsException when authentication fails")
    void login_ShouldThrowException_WhenAuthManagerFails() {

        CreateLoginDto loginDto = new CreateLoginDto();
        loginDto.setEmail("wrong@email.com");
        loginDto.setPassword("wrongPass");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.login(loginDto))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Bad credentials");

        verify(userRepository, never()).findByEmail(any());
        verify(jwtService, never()).generateToken(any());
    }


    @Test
    @DisplayName("Should throw BadCredentialsException when user not found in DB")
    void login_ShouldThrowException_WhenUserNotFoundInDb() {

        CreateLoginDto loginDto = new CreateLoginDto();
        loginDto.setEmail("ghost@email.com");
        loginDto.setPassword("pass");

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginDto))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid email or password");

        verify(jwtService, never()).generateToken(any());
    }
}