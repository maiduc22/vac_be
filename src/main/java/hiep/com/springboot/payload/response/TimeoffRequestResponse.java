package hiep.com.springboot.payload.response;

import hiep.com.springboot.models.entities.TimeOffRequestEntity;
import lombok.Data;

/**
 * @author: Admin
 * @date: 7/31/2023
 **/

@Data
public class TimeoffRequestResponse extends TimeOffRequestEntity {
    private String employeeName;
}
