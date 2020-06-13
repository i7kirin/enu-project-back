package kz.enu.statistics.service;

import kz.enu.statistics.domain.*;
import kz.enu.statistics.dto.GeneralInformation;

import java.util.List;
import java.util.Optional;

public interface StatisticsService {
    City create(City city);
    Optional<List<Information>> getAllInformationByCity(String cityId);
    Information createInformation(Information information);
    List<City> getAllCities();
    List<Professions> createProfession(String cityId, Integer year, Professions professions);
    List<Employed> createEmployed(String cityId, Integer year, Employed employed);
    List<Unemployed> createUnemployed(String cityId, Integer year, Unemployed unemployed);

    List<GeneralInformation> getGeneralInfo();
}
