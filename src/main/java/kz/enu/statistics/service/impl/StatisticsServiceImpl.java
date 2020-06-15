package kz.enu.statistics.service.impl;

import kz.enu.statistics.domain.*;
import kz.enu.statistics.dto.AverageSalary;
import kz.enu.statistics.dto.AvgSalaryByType;
import kz.enu.statistics.dto.GeneralInformation;
import kz.enu.statistics.dto.ITEmployment;
import kz.enu.statistics.repository.*;
import kz.enu.statistics.service.StatisticsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

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
    private ProfessionTypeRepository professionTypeRepository;

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
    public List<AverageSalary> getAvgSalaryByCityAndYear(String city) {
        String map = "function(){" +
                " var key = {name: this.city.name, year: this.year}; " +
                " for (var idx = 0; idx < this.professions.length; idx++){ " +
                " var value = { count: 1, slry: this.professions[idx].salary}; " +
                " emit(key, value); " +
                "} " +
                " }";
        String reduce = "function(keyCityName, salaryValue){ " +
                " reducedVal = { count: 0, slry: 0 }; " +
                " for (var idx = 0; idx < salaryValue.length; idx++) { " +
                "       reducedVal.count += salaryValue[idx].count; " +
                "       reducedVal.slry += salaryValue[idx].slry; " +
                "   }" +
                " return reducedVal; " +
                " }";
        Query query = new Query();
        query.addCriteria(Criteria.where("city.name").is(city));
        query.limit(5);
        MapReduceOptions mapReduceOptions = new MapReduceOptions();
        mapReduceOptions.finalizeFunction("function(key, reducedVal){ " +
                " reducedVal.avg = reducedVal.slry/reducedVal.count; " +
                " return reducedVal;}");
        MapReduceResults<AverageSalary> information = mongoOperations.mapReduce(query, "information", map, reduce, mapReduceOptions, AverageSalary.class);
        List<AverageSalary> avg = new ArrayList<>();
        for (AverageSalary valueObject : information){
            avg.add(valueObject);
        }
        return avg;
    }

    public List<AverageSalary> getGeneralInformation(Integer year){
        String map = "function(){" +
                " var key = {name: this.city.name, latitude: this.city.latitude, longitude: this.city.longitude}; " +
                " for (var idx = 0; idx < this.professions.length; idx++){ " +
                " var value = { employer: this.professions[idx].employerCount," +
                "  employee: this.professions[idx].employeeCount," +
                "  unemployed: this.professions[idx].unemployedCount}; " +
                " emit(key, value); " +
                "} " +
                " }";
        String reduce = "function(keyCityName, value){ " +
                " reducedVal = {employer:0, employee:0, unemployed:0}; " +
                " for (var idx = 0; idx < value.length; idx++) { " +
                "       reducedVal.employer += value[idx].employer; " +
                "       reducedVal.employee += value[idx].employee; " +
                "       reducedVal.unemployed += value[idx].unemployed; " +
                "   }" +
                " return reducedVal; " +
                " }";

        Query query = new Query();
        query.addCriteria(Criteria.where("year").is(year));
        MapReduceResults<AverageSalary> information = mongoOperations.mapReduce(query,"information", map, reduce, AverageSalary.class);

        List<AverageSalary> list = new ArrayList<>();
        for (AverageSalary generalInformation : information){
            list.add(generalInformation);
        }
        return list;
    }

    @Override
    public List<GeneralInformation> getEmployment(String city, Integer year) {
        String map = "function(){" +
                " var key = this.city.name; " +
                " for (var idx = 0; idx < this.professions.length; idx++){ " +
                " var value = {employee: this.professions[idx].employeeCount," +
                "  unemployed: this.professions[idx].unemployedCount}; " +
                " emit(key, value); " +
                "} " +
                " }";
        String reduce = "function(keyCityName, value){ " +
                " reducedVal = {employee:0, unemployed:0}; " +
                " for (var idx = 0; idx < value.length; idx++) { " +
                "       reducedVal.employee += value[idx].employee; " +
                "       reducedVal.unemployed += value[idx].unemployed; " +
                "   }" +
                " return reducedVal; " +
                " }";

        Query query = new Query();
        query.addCriteria(Criteria.where("year").is(year));
        query.addCriteria(Criteria.where("city.name").is(city));
        List<GeneralInformation> list = new ArrayList<>();
        MapReduceResults<GeneralInformation> information = mongoOperations.mapReduce(query,"information", map, reduce, GeneralInformation.class);

        for (GeneralInformation generalInformation : information){
            list.add(generalInformation);
        }
        return list;
    }

    @Override
    public List<GeneralInformation> getYoungEmployment(Integer year) {
        String map = "function(){" +
                " var key = this.city.name; " +
                " for (var idx = 0; idx < this.professions.length; idx++){ " +
                " var value = { " +
                "  unemployed: this.professions[idx].unemployedCount, " +
                "  age: this.professions[idx].age}; " +
                " emit(key, value); " +
                "} " +
                " }";
        String reduce = "function(keyCityName, value){ " +
                " reducedVal = {unemployed: 0}; " +
                " for (var idx = 0; idx < value.length; idx++) { " +
                "if (value[idx].age >= 18 && value[idx].age <= 25) {" +
                "       reducedVal.unemployed += value[idx].unemployed; " +
                "       }" +
                "   }" +
                " return reducedVal; " +
                " }";

        Query query = new Query();
        query.addCriteria(Criteria.where("year").is(year));

        MapReduceResults<GeneralInformation> information = mongoOperations.mapReduce(query,"information", map, reduce, GeneralInformation.class);

        List<GeneralInformation> list = new ArrayList<>();
        for (GeneralInformation generalInformation : information){
            list.add(generalInformation);
        }
        return list;
    }

    @Override
    public ProfessionType createProfessionType(ProfessionType professionType) {
        return professionTypeRepository.save(professionType);
    }

    @Override
    public List<ProfessionType> getProfessionType() {
        return professionTypeRepository.findAll();
    }

    @Override
    public List<AvgSalaryByType> getSalaryByProfession(Integer year, String id) {

        List<Information> all = informationRepository.findByYearAndProfessions_ProfessionType_id(year, id);
        List<AvgSalaryByType> avgSalaryByTypes = new ArrayList<>();
        all.forEach(a -> {
            AvgSalaryByType type = new AvgSalaryByType();
            List<Professions> collect = a.getProfessions().stream().filter(p -> p.getProfessionType().getId().equals(id)).collect(Collectors.toList());
            type.setName(a.getCity().getName());
            type.setAvgSalary(collect.stream().mapToDouble(Professions::getSalary).average().orElse(0.0));
            avgSalaryByTypes.add(type);
        });

        return avgSalaryByTypes;
    }

    @Override
    public List<ITEmployment> getItEmpl(String id, Integer year) {
        List<Information> all = informationRepository.findByYearAndProfessions_ProfessionType_id(year, id);
        List<ITEmployment> itEmployments = new ArrayList<>();
        all.forEach(a -> {
            ITEmployment itEmployment = new ITEmployment();
            List<Professions> collect = a.getProfessions().stream().filter(p -> p.getProfessionType().getId().equals(id)).collect(Collectors.toList());
            itEmployment.setName(a.getCity().getName());
            itEmployment.setEmployed(collect.stream().mapToDouble(Professions::getEmployeeCount).sum());
            itEmployment.setUnemployed(collect.stream().mapToDouble(Professions::getUnemployedCount).sum());
            itEmployments.add(itEmployment);
        });
        return itEmployments;
    }
}