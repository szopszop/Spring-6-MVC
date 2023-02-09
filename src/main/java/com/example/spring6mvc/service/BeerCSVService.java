package com.example.spring6mvc.service;

import com.example.spring6mvc.model.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCSVService {

    List<BeerCSVRecord> convertCSV(File csvFile);
}
