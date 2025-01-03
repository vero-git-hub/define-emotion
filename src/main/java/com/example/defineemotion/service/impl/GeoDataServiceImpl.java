package com.example.defineemotion.service.impl;

import java.io.InputStream;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.defineemotion.model.CityHelplines;
import com.example.defineemotion.model.CountryCities;
import com.example.defineemotion.model.Helpline;
import com.example.defineemotion.service.GeoDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class GeoDataServiceImpl implements GeoDataService {
    
    private final List<CountryCities> geoData = new ArrayList<>();

    /**
     * Initializes the service by loading the geo data from the JSON file.
     */
    @PostConstruct
    public void init() {
        try (InputStream is = getClass().getResourceAsStream("/country-cities.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<CountryCities> data = objectMapper.readValue(is, new TypeReference<>() {});
            geoData.addAll(data);
        } catch (Exception e) {
            log.error("Failed to load geo data", e);
        }
    }

    /**
     * Retrieves all available countries.
     * @return a list of all available countries
     */
    @Override
    public List<String> getAllCountries() {
        List<String> countries = geoData.stream()
                .map(CountryCities::getCountry)
                .toList();
        return countries;
    }

    /**
     * Retrieves all cities for the given country.
     * @param country the country to retrieve the cities for
     * @return a list of all cities for the given country
     */
    @Override
    public List<String> getCitiesByCountry(String country) {
        List<String> cities = geoData.stream()
                .filter(item -> item.getCountry().equalsIgnoreCase(country))
                .flatMap(item -> item.getCities().stream().map(CityHelplines::getName))
                .toList();
        return cities;
    }

    /**
     * Retrieves all helplines for the given country and city.
     * @param country the country to retrieve the helplines for
     * @param city the city to retrieve the helplines for
     * @return a list of all helplines for the given country and city
     */
    @Override
    public List<Helpline> getHelplinesByCountryAndCity(String country, String city) {
        return geoData.stream()
                .filter(item -> item.getCountry().equalsIgnoreCase(country))
                .flatMap(item -> item.getCities().stream())
                .filter(cityItem -> cityItem.getName().equalsIgnoreCase(city))
                .flatMap(cityItem -> cityItem.getHelplines().stream())
                .toList();
    }
}