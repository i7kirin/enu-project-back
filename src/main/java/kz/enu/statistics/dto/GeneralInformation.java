package kz.enu.statistics.dto;

import lombok.Data;

@Data
public class GeneralInformation {
    private String city;
    private Integer employedCount;
    private Integer unemployedCount;
    private Integer employerCount;
    private Integer avgSalary;
}
