package kz.enu.statistics.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Employed {

    @Id
    private String id;
    private Integer age;
    private Integer count;
}
