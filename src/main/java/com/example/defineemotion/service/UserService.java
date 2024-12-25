package com.example.defineemotion.service;

import com.example.defineemotion.dto.EditProfileDto;
import com.example.defineemotion.dto.RegisterUserDto;

public interface UserService {

    boolean registerUser(RegisterUserDto registerUserDto);

    String getCurrentUsername();

    String getCurrentUserEmail(String username);

    RegisterUserDto getUserByUsername(String username);

    void updateUserProfile(EditProfileDto editProfileDto);

    EditProfileDto getEditProfileByUsername(String username);

    String getCurrentUserCountry(String username);
    
    String getCurrentUserCity(String username);
}