package com.example.defineemotion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.defineemotion.service.GeoDataService;

@RestController
@RequestMapping("/api")
public class GeoController {

    @Autowired
    private GeoDataService geoDataService;

    @GetMapping("/cities")
    public List<String> getCities(@RequestParam String country) {
        return geoDataService.getCitiesByCountry(country);
    }
}