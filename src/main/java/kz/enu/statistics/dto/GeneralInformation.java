package kz.enu.statistics.dto;

import lombok.Data;

import java.util.HashMap;

@Data
public class GeneralInformation {
    private String id;
    private HashMap<String, Integer> value;
}
