package com.example.defineemotion.service;

import com.example.defineemotion.dto.RegisterUserDto;

public interface UserService {

    boolean registerUser(RegisterUserDto registerUserDto);
}