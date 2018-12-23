package pl.mkolasinski.Quest1.Model.conferenceRoom;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class PhoneDescription {

    @Max(value = 99, message = "Internal phone number should be equal or less than 99")
    @Min(value = 0, message = "Internal phone number should be equal or more than 0")
    private int internalNumber;

    @Size(min = 12, max = 13, message = "External phone number should be like \'+12 123456789\'")
    private String externalNumber;

    private PhoneInterface phoneInterface;

}
