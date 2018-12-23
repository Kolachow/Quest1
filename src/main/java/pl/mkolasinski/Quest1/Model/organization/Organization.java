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

    private String name;

    public boolean addRoom(ConferenceRoom conferenceRoom) {
        for (ConferenceRoom c : conferenceRooms) {
            if (c.getName().equals(conferenceRoom.getName().trim()) || c.getId().equals(conferenceRoom.getName().trim())) {
                return false;
            }
        }

        conferenceRoom.setName(conferenceRoom.getName().trim());
        conferenceRoom.setId(conferenceRoom.getId().trim());
        conferenceRooms.add(conferenceRoom);
        return true;
    }

}
