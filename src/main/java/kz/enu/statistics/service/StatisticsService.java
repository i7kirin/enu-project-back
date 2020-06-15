package kz.enu.statistics.service;

import kz.enu.statistics.domain.*;
import kz.enu.statistics.dto.AverageSalary;
import kz.enu.statistics.dto.AvgSalaryByType;
import kz.enu.statistics.dto.GeneralInformation;
import kz.enu.statistics.dto.ITEmployment;

import java.util.List;
import java.util.Optional;

public interface StatisticsService {
    City create(City city);
    Optional<List<Information>> getAllInformationByCity(String cityId);
    Information createInformation(Information information);
    List<City> getAllCities();
    List<Professions> createProfession(String cityId, Integer year, Professions professions);
    List<AverageSalary> getAvgSalaryByCityAndYear(String city);
    List<AverageSalary> getGeneralInformation(Integer year);
    List<GeneralInformation> getEmployment(String city, Integer year);
    List<GeneralInformation> getYoungEmployment(Integer year);
    ProfessionType createProfessionType(ProfessionType professionType);
    List<ProfessionType> getProfessionType();
    List<AvgSalaryByType> getSalaryByProfession(Integer year, String id);
    List<ITEmployment> getItEmpl(String id, Integer year);
}

