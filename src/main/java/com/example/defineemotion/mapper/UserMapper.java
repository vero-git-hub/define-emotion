package com.example.defineemotion.mapper;

import com.example.defineemotion.dto.RegisterUserDto;
import com.example.defineemotion.model.User;

public interface UserMapper {
    User toEntity(RegisterUserDto dto);
}