package com.example.defineemotion.service;

import java.util.List;

public interface GeoDataService {
    List<String> getAllCountries();
    List<String> getCitiesByCountry(String countryName);
}