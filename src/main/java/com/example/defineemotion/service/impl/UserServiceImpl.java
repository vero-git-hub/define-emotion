package com.example.defineemotion.service.impl;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.defineemotion.dto.EditProfileDto;
import com.example.defineemotion.dto.RegisterUserDto;
import com.example.defineemotion.mapper.UserMapper;
import com.example.defineemotion.model.User;
import com.example.defineemotion.repository.UserRepository;
import com.example.defineemotion.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Registers a new user.
     * @param registerUserDto the user data
     * @return true if the user was registered successfully, false otherwise
     */
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

    /**
     * Retrieves the username of the currently authenticated user.
     * @return the username of the currently authenticated user
     */
    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
            && !(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * Retrieves the email of the currently authenticated user.
     * @param username the username of the user
     * @return the email of the currently authenticated user
     */
    @Override
    public String getCurrentUserEmail(String username) {
        return userRepository.findByUsername(username)
                .map(User::getEmail)
                .orElse("Email not found");
    }

    /**
     * Retrieves the user data by username.
     * @param username the username of the user
     * @return the user data
     */
    @Override
    public RegisterUserDto getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    /**
     * Updates the user profile.
     * @param editProfileDto the updated user data
     */
    @Override
    public void updateUserProfile(EditProfileDto editProfileDto) {
        String currentUsername = getCurrentUsername();
        if (currentUsername == null) {
            throw new IllegalArgumentException("User not authenticated");
        }
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEmail(editProfileDto.getEmail());
        user.setCountry(editProfileDto.getCountry());
        user.setCity(editProfileDto.getCity());

        userRepository.save(user);
    }

    /**
     * Retrieves the user data for editing by username.
     * @param username the username of the user
     * @return the user data for editing
     */
    @Override
    public EditProfileDto getEditProfileByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    EditProfileDto dto = new EditProfileDto();
                    dto.setEmail(user.getEmail());
                    dto.setCountry(user.getCountry());
                    dto.setCity(user.getCity());
                    return dto;
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    /**
     * Retrieves the country of the currently authenticated user.
     * @param username the username of the user
     * @return the country of the currently authenticated user
     */
    @Override
    public String getCurrentUserCountry(String username) {
        return userRepository.findByUsername(username)
                .map(User::getCountry)
                .orElse(null);
    }

    /**
     * Retrieves the city of the currently authenticated user.
     * @param username the username of the user
     * @return the city of the currently authenticated user
     */
    @Override
    public String getCurrentUserCity(String username) {
        return userRepository.findByUsername(username)
                .map(User::getCity)
                .orElse(null);
    }
}