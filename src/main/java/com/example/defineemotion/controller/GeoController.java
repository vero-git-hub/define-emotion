package com.example.defineemotion.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.defineemotion.service.GeoDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GeoController {
    
    private final GeoDataService geoDataService;

    @GetMapping("/cities")
    public List<String> getCities(@RequestParam String country) {
        return geoDataService.getCitiesByCountry(country);
    }
}