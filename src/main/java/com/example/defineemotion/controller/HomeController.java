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
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    /**
     * Shows the home page.
     * @param model the model to add attributes to
     * @return the view name
     */
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

    /**
     * Shows the login page.
     * @return the view name
     */
    @GetMapping("/login")
    public String loginPage() {
        return "/auth/login";
    }

    /**
     * Shows the registration page.
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerUser", new RegisterUserDto());
        return "/auth/register";
    }

    /**
     * Processes the registration form.
     * @param registerUserDto the user data
     * @param bindingResult the binding result
     * @param model the model to add attributes to
     * @return the view name
     */
    @PostMapping("/register")
    public String processRegistration(
            @Valid @ModelAttribute("registerUser") RegisterUserDto registerUserDto,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "/auth/register";
        }
        boolean isRegistered = userService.registerUser(registerUserDto);
        if (!isRegistered) {
            model.addAttribute("error", "Username or email already taken.");
            return "/auth/register";
        }
        return "redirect:/login";
    }
}