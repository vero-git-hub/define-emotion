package com.example.defineemotion.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.defineemotion.model.Helpline;
import com.example.defineemotion.service.GeoDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HelplineController {

    private final GeoDataService geoDataService;

    @GetMapping("/helplines")
    public List<Helpline> getHelplines(@RequestParam String country, @RequestParam String city) {
        return geoDataService.getHelplinesByCountryAndCity(country, city);
    }
}