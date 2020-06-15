package kz.enu.statistics.api;

import kz.enu.statistics.domain.*;
import kz.enu.statistics.dto.AverageSalary;
import kz.enu.statistics.dto.AvgSalaryByType;
import kz.enu.statistics.dto.GeneralInformation;
import kz.enu.statistics.dto.ITEmployment;
import kz.enu.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @PostMapping(value = "/city", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<City> createCity(@RequestBody City city){
        return ResponseEntity.status(HttpStatus.CREATED).body(statisticsService.create(city));
    }

    @PostMapping(value = "/information", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Information> createInformation(@RequestBody Information information){
        return ResponseEntity.status(HttpStatus.OK).body(statisticsService.createInformation(information));
    }

    @GetMapping("/city/all")
    public ResponseEntity<List<City>> getAllCities(){
        return ResponseEntity.status(HttpStatus.OK).body(statisticsService.getAllCities());
    }

    @GetMapping(value = "/information/{cityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Information>> getAllInformationByCity(@PathVariable String cityId){
        return statisticsService.getAllInformationByCity(cityId).map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PutMapping("/professions/{cityId}/{year}")
    public ResponseEntity<List<Professions>> createProfession(@PathVariable String cityId, @PathVariable Integer year, @RequestBody Professions professions){
        return ResponseEntity.status(HttpStatus.CREATED).body(statisticsService.createProfession(cityId, year, professions));
    }

    @RequestMapping(value = "/get-avg-salary/{city}", method = RequestMethod.GET)
    public List<AverageSalary> getAvgSalaryByCityAndYear(@PathVariable String city){
        return statisticsService.getAvgSalaryByCityAndYear(city);
    }

    @GetMapping("/general-info/{year}")
    public List<AverageSalary> getGeneralInformation(@PathVariable Integer year){
        return statisticsService.getGeneralInformation(year);
    }

    @GetMapping("/employment/{city}/{year}")
    public List<GeneralInformation> getEmployment(@PathVariable String city, @PathVariable Integer year){
        return statisticsService.getEmployment(city, year);
    }

    @GetMapping("/young-employment/{year}")
    public List<GeneralInformation> getYoungEmployment(@PathVariable Integer year){
        return statisticsService.getYoungEmployment(year);
    }

    @PostMapping("/profession-type")
    public ResponseEntity<ProfessionType> createProfessionType(@RequestBody ProfessionType professionType){
        return ResponseEntity.status(HttpStatus.CREATED).body(statisticsService.createProfessionType(professionType));
    }

    @GetMapping("/profession-type")
    public ResponseEntity<List<ProfessionType>> createProfessionType(){
        return ResponseEntity.status(HttpStatus.CREATED).body(statisticsService.getProfessionType());
    }

    @GetMapping("/salary/by-profession-type/{id}/{year}")
    public List<AvgSalaryByType> getSalaryByProfession(@PathVariable String id, @PathVariable Integer year){
        return statisticsService.getSalaryByProfession(year, id);
    }

    @GetMapping("/it/employment/{id}/{year}")
    public List<ITEmployment> getItEmpl(@PathVariable String id, @PathVariable Integer year){
        return statisticsService.getItEmpl(id, year);
    }
}
