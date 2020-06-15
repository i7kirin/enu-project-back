package kz.enu.statistics.dto;

import lombok.Data;

import java.util.HashMap;

@Data
public class AverageSalary {
    private HashMap<String, String> id;
    private HashMap<String, Integer> value;
}
