package pl.mkolasinski.Quest1.model.organization;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import pl.mkolasinski.Quest1.model.conferenceRoom.ConferenceRoom;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Data
public class Organization {

    @NotNull
    @Length(min = 2, max = 20, message = "OrganizationS name should be between 2 and 20 characters.")
    private String name;

    private List<ConferenceRoom> conferenceRooms = new LinkedList<>();

    public boolean addRoom(ConferenceRoom conferenceRoom) {
        for (ConferenceRoom c : conferenceRooms) {
            if (c.getName().equals(conferenceRoom.getName().trim()) || c.getId().equals(conferenceRoom.getId().trim())) {
                return false;
            }
        }

        conferenceRoom.setName(conferenceRoom.getName().trim());
        conferenceRoom.setId(conferenceRoom.getId().trim());
        conferenceRooms.add(conferenceRoom);
        return true;
    }
}
