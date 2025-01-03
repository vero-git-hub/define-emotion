package com.example.defineemotion.mapper.impl;

import org.springframework.stereotype.Component;

import com.example.defineemotion.dto.RegisterUserDto;
import com.example.defineemotion.mapper.UserMapper;
import com.example.defineemotion.model.User;

@Component
public class UserMapperImpl implements UserMapper {

    /**
     * Converts a register user DTO to a user entity.
     */
    @Override
    public User toEntity(RegisterUserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    /**
     * Converts a user entity to a register user DTO.
     */
    @Override
    public RegisterUserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        return new RegisterUserDto(
            user.getUsername(),
            user.getEmail(),
            null
        );
    }
}