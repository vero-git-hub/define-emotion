package com.example.defineemotion.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.defineemotion.service.UserService;

import org.springframework.ui.Model;
import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addUserAttributes(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());

            String country = userService.getCurrentUserCountry(principal.getName());
            String city = userService.getCurrentUserCity(principal.getName());
            boolean hasAddress = (country != null && !country.isBlank()) && (city != null && !city.isBlank());

            model.addAttribute("hasAddress", hasAddress);
            if (hasAddress) {
                model.addAttribute("country", country);
                model.addAttribute("city", city);
            }
        } else {
            model.addAttribute("hasAddress", false);
        }
    }
}