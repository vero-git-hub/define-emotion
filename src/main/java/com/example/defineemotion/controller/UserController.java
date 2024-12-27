package com.example.defineemotion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.defineemotion.dto.EditProfileDto;
import com.example.defineemotion.service.GeoDataService;
import com.example.defineemotion.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final GeoDataService geoDataService;

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
            model.addAttribute("countryList", geoDataService.getAllCountries());
            if (userDto.getCountry() != null) {
                model.addAttribute("cityList", geoDataService.getCitiesByCountry(userDto.getCountry()));
            }
        }

        model.addAttribute("activePage", "profile");
        return "profile";
    }

    @PostMapping("/profile")
    public String updateUserProfile(@Valid @ModelAttribute("user") EditProfileDto editProfileDto,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", true);
            model.addAttribute("countryList", geoDataService.getAllCountries());
            return "profile";
        }                              
        userService.updateUserProfile(editProfileDto);
        return "redirect:/profile";
    }
}