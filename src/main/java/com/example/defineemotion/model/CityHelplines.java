package com.example.defineemotion.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityHelplines {
    private String name;
    private List<Helpline> helplines;
}