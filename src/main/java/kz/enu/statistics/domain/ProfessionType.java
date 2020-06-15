package kz.enu.statistics.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class ProfessionType {

    @Id
    private String id;
    private String type;
    private String description;
}
