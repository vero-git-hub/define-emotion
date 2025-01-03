package com.example.defineemotion.service;

import java.util.List;

import com.example.defineemotion.model.Helpline;

public interface GeoDataService {

    List<String> getAllCountries();

    List<String> getCitiesByCountry(String countryName);
    
    List<Helpline> getHelplinesByCountryAndCity(String country, String city);
}