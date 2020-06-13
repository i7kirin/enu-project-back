package kz.enu.statistics.service.impl;

import kz.enu.statistics.domain.*;
import kz.enu.statistics.dto.GeneralInformation;
import kz.enu.statistics.repository.*;
import kz.enu.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private InformationRepository informationRepository;
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private ProfessionsRepository professionsRepository;
    @Autowired
    private EmployedRepository employedRepository;
    @Autowired
    private UnemployedRepository unemployedRepository;

    @Override
    public City create(City city) {
        return cityRepository.save(city);
    }

    @Override
    public Optional<List<Information>> getAllInformationByCity(String cityId) {
        return informationRepository.findAllByCityId(cityId);
    }

    @Override
    public Information createInformation(Information information) {
        return informationRepository.save(information);
    }

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public List<Professions> createProfession(String cityId, Integer year, Professions professions) {
        Information information = informationRepository.findByYearAndAndCity_Id(year, cityId);
        if (information == null)
            throw new NullPointerException(String.format("City by id %s and year %s does not found!", cityId, year));
        List<Professions> infoProfessions = information.getProfessions();
        if (infoProfessions == null)
            infoProfessions = new ArrayList<>();

        infoProfessions.add(professionsRepository.save(professions));
        information.setProfessions(infoProfessions);
        return informationRepository.save(information).getProfessions();
    }

    @Override
    public List<Employed> createEmployed(String cityId, Integer year, Employed employed) {
        Information information = informationRepository.findByYearAndAndCity_Id(year, cityId);
        if (information == null)
            throw new NullPointerException(String.format("City by id %s and year %s does not found!", cityId, year));
        List<Employed> employedList = information.getEmployed();
        if (employedList == null)
            employedList = new ArrayList<>();
        employedList.add(employedRepository.save(employed));
        information.setEmployed(employedList);
        return informationRepository.save(information).getEmployed();
    }

    @Override
    public List<Unemployed> createUnemployed(String cityId, Integer year, Unemployed unemployed) {
        Information information = informationRepository.findByYearAndAndCity_Id(year, cityId);
        if (information == null)
            throw new NullPointerException(String.format("City by id %s and year %s does not found!", cityId, year));
        List<Unemployed> unemployedList = information.getUnemployed();
        if (unemployedList == null)
            unemployedList = new ArrayList<>();
        unemployedList.add(unemployedRepository.save(unemployed));
        information.setUnemployed(unemployedList);
        return informationRepository.save(information).getUnemployed();
    }

    @Override
    public List<GeneralInformation> getGeneralInfo() {
        List<City> all = cityRepository.findAll();
        List<GeneralInformation> generalInformation = new ArrayList<>();
        all.forEach(city -> {
            Optional<List<Information>> allByCityId = informationRepository.findAllByCityId(city.getId());
            GeneralInformation gi = new GeneralInformation();
            gi.setCity(city.getName());
            if (allByCityId.isPresent()){
                List<Information> information = allByCityId.get();
                information.forEach(i -> {
                    List<Unemployed> unemployed = i.getUnemployed();
                    List<Employed> employed = i.getEmployed();
                    List<Professions> professions = i.getProfessions();
                    gi.setUnemployedCount(unemployed.stream().reduce(0, (unemployedSum, u) -> unemployedSum + u.getCount(), Integer::sum));
                    gi.setEmployedCount(employed.stream().reduce(0, (employedSum, u) -> employedSum + u.getCount(), Integer::sum));
                    gi.setEmployerCount(professions.stream().reduce(0, (employerSum, u) -> employerSum + u.getEmployerCount(), Integer::sum));
                });
            }
            generalInformation.add(gi);
        });
        return generalInformation;
    }
}