package kz.enu.statistics.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Information {

    @Id
    private String id;
    private City city;
    private Integer year;
    private Integer population;
    private List<Employed> employed;
    private List<Unemployed> unemployed;
    private List<Professions> professions;
    private String description;
}
