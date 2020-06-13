package kz.enu.statistics.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class City {

    @Id
    private String id;
    private String name;
    private String description;
    private String latitude;
    private String longitude;
}
