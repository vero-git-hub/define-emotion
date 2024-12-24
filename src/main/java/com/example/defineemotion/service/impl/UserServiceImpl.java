package com.example.defineemotion.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.defineemotion.dto.EditProfileDto;
import com.example.defineemotion.dto.RegisterUserDto;
import com.example.defineemotion.mapper.UserMapper;
import com.example.defineemotion.model.User;
import com.example.defineemotion.repository.UserRepository;
import com.example.defineemotion.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean registerUser(RegisterUserDto registerUserDto) {
        if (userRepository.existsByUsername(registerUserDto.getUsername()) ||
            userRepository.existsByEmail(registerUserDto.getEmail())) {
            return false;
        }

        User user = userMapper.toEntity(registerUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return true;
    }

    @Override
    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public String getCurrentUserEmail(String username) {
        return userRepository.findByUsername(username)
                .map(User::getEmail)
                .orElse("Email not found");
    }

    @Override
    public RegisterUserDto getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    @Override
    public void updateUserProfile(EditProfileDto editProfileDto) {
        String currentUsername = getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEmail(editProfileDto.getEmail());
        user.setPhoneNumber(editProfileDto.getPhoneNumber());
        user.setCountry(editProfileDto.getCountry());
        user.setCity(editProfileDto.getCity());
        user.setStreet(editProfileDto.getStreet());
        user.setHouse(editProfileDto.getHouse());

        userRepository.save(user);
    }

    @Override
    public EditProfileDto getEditProfileByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    EditProfileDto dto = new EditProfileDto();
                    dto.setEmail(user.getEmail());
                    dto.setPhoneNumber(user.getPhoneNumber());
                    dto.setCountry(user.getCountry());
                    dto.setCity(user.getCity());
                    dto.setStreet(user.getStreet());
                    dto.setHouse(user.getHouse());
                    return dto;
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}