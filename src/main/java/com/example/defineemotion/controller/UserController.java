package com.example.defineemotion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.defineemotion.dto.EditProfileDto;
import com.example.defineemotion.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String userProfile(@RequestParam(value = "editMode", required = false) Boolean editMode, 
                            Model model) {
        String username = userService.getCurrentUsername();
        model.addAttribute("username", username);
        model.addAttribute("email", userService.getCurrentUserEmail(username));
        model.addAttribute("editMode", editMode);

        EditProfileDto userDto = userService.getEditProfileByUsername(username);
        model.addAttribute("user", userDto);

        if (Boolean.TRUE.equals(editMode)) {
            model.addAttribute("countryList", List.of("USA", "Germany", "UK", "France", "Italy"));
            model.addAttribute("cityList", List.of("New York", "Berlin", "London", "Paris", "Rome"));
        }

        return "profile";
    }

    @PostMapping("/profile")
    public String updateUserProfile(@Valid @ModelAttribute("user") EditProfileDto editProfileDto,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", true);
            return "profile";
        }
        userService.updateUserProfile(editProfileDto);
        return "redirect:/profile";
    }
}