package de.rjst.rjstintegration.database;

import de.rjst.rjstintegration.FlowType;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String name;
    private Integer status;
    private FlowType flowType;
}
