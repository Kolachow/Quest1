package pl.mkolasinski.Quest1.Model.organization;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import pl.mkolasinski.Quest1.Model.conferenceRoom.ConferenceRoom;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Data
public class Organization {

    private List<ConferenceRoom> conferenceRooms = new LinkedList<>();

    @NotNull
    @Length(min = 2, max = 20, message = "Organization name should be between 2 and 20 characters.")
    private String name;

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
