package pl.mkolasinski.Quest1.Model.conferenceRoom;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class ConferenceRoom{

    @NotNull
    @Length(min = 2, max = 20, message = "Name of the conference room should be between 2 and 20 characters.")
    private String name;

    @NotNull
    @Length(min = 2, max = 20, message = "Id should be between 2 and 20 characters without white signs.")
    private String id = "No ID";

    @NotNull
    @Min(value = 0, message = "Floor should be equal or more than 0.")
    @Max(value = 10, message = "Floor should be equal or less than 10.")
    private int floor = -1;

    private boolean availability = true;

    @NotNull
    @Min(value = 0, message = "Number of seats and standing should be equal or more than 0.")
    private int numberOfSeatsAndStanding = -1;

    @NotNull
    @Min(value = 0, message = "Number of lying places should be equal or more than 0.")
    private int numberOfLyingPlaces = 0;

    @NotNull
    @Min(value = 0, message = "Number of hanging places should be equal or more than 0.")
    private int numberOfHangingPlaces = 0;

    @NotNull
    private String  projectorName = "No name";

    private boolean phone = false;

    @NotNull
    @Max(value = 99, message = "Internal phone number should be equal or less than 99")
    @Min(value = 0, message = "Internal phone number should be equal or more than 1")
    private int internalNumber;

    @NotNull
    @Pattern(regexp = "[+]\\d\\d\\s\\d{9}", message = "External phone number should be like \'+12 123456789\'")
    private String externalNumber = "+00 000000000";

    @NotNull
    private PhoneInterface phoneInterface = PhoneInterface.NO_INTERFACE;


}