package com.petshop.api.controller;

import com.petshop.api.dto.response.AuthResponseDto;
import com.petshop.api.dto.request.CreateLoginDto;
import com.petshop.api.dto.request.CreateRegisterDto;
import com.petshop.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody CreateRegisterDto createRegisterDTO){return ResponseEntity.ok(authService.register(createRegisterDTO));}

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody CreateLoginDto createLoginDTO){return ResponseEntity.ok(authService.login(createLoginDTO));}



}
