package kz.enu.statistics.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Professions {

    @Id
    private String id;
    private String name;
    private Integer age;
    private Integer salary;
    private ProfessionType professionType;
    private Integer employeeCount;
    private Integer employerCount;
    private Integer unemployedCount;
}
