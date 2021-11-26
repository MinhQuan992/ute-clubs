package hcmute.manage.club.uteclubs.framework.dto.club;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubAddOrUpdateInfoParams {
    @NotBlank(message = "The club name is required")
    @Size(min = 3, max = 255, message = "The length of the club name must be between 3 and 255 characters")
    private String clubName;

    @NotBlank(message = "The affiliated unit is required")
    @Size(min = 3, max = 255, message = "The length of the affiliated unit name must be between 3 and 255 characters")
    private String affiliatedUnit;

    private String description;
}
