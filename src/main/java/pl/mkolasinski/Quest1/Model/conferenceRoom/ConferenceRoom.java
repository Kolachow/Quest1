package pl.mkolasinski.Quest1.Model.conferenceRoom;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class ConferenceRoom extends Equipment {

    @UniqueElements(message = "This name of the conference room already exists.")
    @Size(min = 2, max = 20, message = "Name should be more than 1 and less than 21 characters without white signs.")
    private String name;

    @UniqueElements(message = "This id of the conference room already exists.")
    @Size(min = 2, max = 20, message = "Id should be more than 1 and less than 21 characters without white signs.")
    private String id;

    @Min(value = 0, message = "Floor should be equal or more than 0.")
    @Max(value = 10, message = "Floor should be equal or less than 10.")
    private int floor;

    private boolean availability;

    @Min(value = 0, message = "Number of seats and standing should be equal or more than 0.")
    private int numberOfSeatsAndStanding;

    @Min(value = 0, message = "Number of lying places should be equal or more than 0.")
    private int numberOfLyingPlaces;

    @Min(value = 0, message = "Number of hanging places should be equal or more than 0.")
    private int numberOfHangingPlaces;


}