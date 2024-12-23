package com.example.defineemotion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.defineemotion.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String userProfile(Model model) {
        String username = userService.getCurrentUsername();
        model.addAttribute("username", username);
        model.addAttribute("email", userService.getCurrentUserEmail(username));
        return "profile";
    }

}
