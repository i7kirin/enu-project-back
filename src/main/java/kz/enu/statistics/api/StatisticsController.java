package kz.enu.statistics.api;

import kz.enu.statistics.domain.*;
import kz.enu.statistics.dto.GeneralInformation;
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

    @PutMapping("/employed/{cityId}/{year}")
    public ResponseEntity<List<Employed>> createEmployed(@PathVariable String cityId, @PathVariable Integer year, @RequestBody Employed employed){
        return ResponseEntity.status(HttpStatus.CREATED).body(statisticsService.createEmployed(cityId, year, employed));
    }

    @PutMapping("/unemployed/{cityId}/{year}")
    public ResponseEntity<List<Unemployed>> createUnemployed(@PathVariable String cityId, @PathVariable Integer year, @RequestBody Unemployed unemployed){
        return ResponseEntity.status(HttpStatus.CREATED).body(statisticsService.createUnemployed(cityId, year, unemployed));
    }

    @GetMapping("/general-info")
    public ResponseEntity<List<GeneralInformation>> createUnemployed(){
        return ResponseEntity.ok(statisticsService.getGeneralInfo());
    }
}
