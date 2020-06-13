package kz.enu.statistics.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Unemployed {

    @Id
    private String id;
    private Integer age;
    private Integer count;
}
