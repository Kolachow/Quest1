package pl.mkolasinski.Quest1.Model.organization;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import pl.mkolasinski.Quest1.Model.conferenceRoom.ConferenceRoom;

import javax.validation.constraints.Size;
import java.util.LinkedList;
import java.util.List;

@Data
public class Organization {

    private List<ConferenceRoom> conferenceRooms = new LinkedList<>();

    @UniqueElements(message = "This organization name already exists.")
    @Size(min = 2, max = 20, message = "Name should be more than 1 and less than 21 characters without white signs.")
    private String name;


}
