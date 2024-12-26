package com.example.defineemotion.service.impl;

import java.io.InputStream;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.defineemotion.model.CityHelplines;
import com.example.defineemotion.model.CountryCities;
import com.example.defineemotion.service.GeoDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.annotation.PostConstruct;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GeoDataServiceImpl implements GeoDataService {
    private static final Logger log = LoggerFactory.getLogger(GeoDataServiceImpl.class);

    private final List<CountryCities> geoData = new ArrayList<>();

    @PostConstruct
    public void init() {
        try (InputStream is = getClass().getResourceAsStream("/countries-cities.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<CountryCities> data = objectMapper.readValue(is, new TypeReference<>() {});
            geoData.addAll(data);
            log.info("Loaded geo data successfully: {}", data);
        } catch (Exception e) {
            log.error("Failed to load geo data", e);
        }
    }

    @Override
    public List<String> getAllCountries() {
        List<String> countries = geoData.stream()
                .map(CountryCities::getCountry)
                .toList();
        log.debug("Available countries: {}", countries);
        return countries;
    }

    @Override
    public List<String> getCitiesByCountry(String country) {
        List<String> cities = geoData.stream()
                .filter(item -> item.getCountry().equalsIgnoreCase(country))
                .flatMap(item -> item.getCities().stream().map(CityHelplines::getName))
                .toList();
        log.debug("Cities for country {}: {}", country, cities);
        return cities;
    }
}