package com.example.defineemotion.service.impl;

import java.io.InputStream;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.defineemotion.model.CountryCities;
import com.example.defineemotion.service.GeoDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.annotation.PostConstruct;

import java.util.List;

@Service
public class GeoDataServiceImpl implements GeoDataService {

    private final List<CountryCities> geoData = new ArrayList<>();

    @PostConstruct
    public void init() {
        try (InputStream is = getClass().getResourceAsStream("/countries-cities.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<CountryCities> data = objectMapper.readValue(is, new TypeReference<>() {});
            geoData.addAll(data);
        } catch (Exception e) {
            System.err.println("Failed to load geo data: " + e.getMessage());
        }
    }

    @Override
    public List<String> getAllCountries() {
        return geoData.stream()
                .map(CountryCities::getCountry)
                .toList();
    }

    @Override
    public List<String> getCitiesByCountry(String country) {
        return geoData.stream()
                .filter(item -> item.getCountry().equalsIgnoreCase(country))
                .flatMap(item -> item.getCities().stream())
                .toList();
    }
}