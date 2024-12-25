package com.example.defineemotion.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.defineemotion.dto.RegisterUserDto;
import com.example.defineemotion.service.UserService;
import org.springframework.security.core.Authentication;

import jakarta.validation.Valid;

@Controller
public class HomeController {

    private UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            String country = userService.getCurrentUserCountry(username);
            String city = userService.getCurrentUserCity(username);
            boolean hasAddress = (country != null && !country.isBlank()) && (city != null && !city.isBlank());
            model.addAttribute("hasAddress", hasAddress);
            if (hasAddress) {
                model.addAttribute("country", country);
                model.addAttribute("city", city);
            }
        } else {
            model.addAttribute("hasAddress", false);
        }

        model.addAttribute("activePage", "home");
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerUser", new RegisterUserDto());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(
            @Valid @ModelAttribute("registerUser") RegisterUserDto registerUserDto,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        boolean isRegistered = userService.registerUser(registerUserDto);
        if (!isRegistered) {
            model.addAttribute("error", "Username or email already taken.");
            return "register";
        }
        return "redirect:/login";
    }
}