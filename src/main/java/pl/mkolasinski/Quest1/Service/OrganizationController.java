package pl.mkolasinski.Quest1.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mkolasinski.Quest1.Model.conferenceRoom.ConferenceRoom;
import pl.mkolasinski.Quest1.Model.organization.Organization;
import pl.mkolasinski.Quest1.Model.reservations.Reservation;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/organizations")
public class OrganizationController {

    private List<Organization> organizationsList = new LinkedList<>();
    private List<String> roomsNames = new LinkedList<>();
    private List<String> roomsIds = new LinkedList<>();
    private List<Reservation> reservationsList = new LinkedList<>();

//organizations CRUD
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addOrganization(@RequestBody Organization organization) {
        for (Organization o: organizationsList) {
            if (o.getName().equals(organization.getName().trim())) {
                return new ResponseEntity<>("Organization name already exist" ,HttpStatus.BAD_REQUEST);
            }
        }

        if (organization.getName().trim().length() > 1 && organization.getName().trim().length() < 21) {
            organization.setName(organization.getName().trim());
            organizationsList.add(organization);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Organization name is NOT between 2 and 20 charakters." ,HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/showorganizations", method = RequestMethod.GET)
    public ResponseEntity<List<String>> showOrganizations() {
        List<String> rawOrganizationsList = new LinkedList<>();
        for (Organization o : organizationsList) {
            rawOrganizationsList.add(o.getName());
        }
        return new ResponseEntity<>(rawOrganizationsList, HttpStatus.OK);
    }

    @RequestMapping(value = "/showall", method = RequestMethod.GET)
    public ResponseEntity<List<Organization>> showAll() {
        return new ResponseEntity<>(organizationsList, HttpStatus.OK);
    }

    @RequestMapping(value = "/addroom", method = RequestMethod.POST)
    public ResponseEntity<?> addRoom(@RequestParam(value = "id") String id, @RequestBody @Valid ConferenceRoom conferenceRoom) {
        for (Organization o : organizationsList) {
            if (o.getName().toLowerCase().equals(id.toLowerCase())) {
                if (roomsNames.contains(conferenceRoom.getName())) {
                    return new ResponseEntity<>("Room name already exist." ,HttpStatus.BAD_REQUEST);
                } else if (roomsIds.contains(conferenceRoom.getId())) {
                    return new ResponseEntity<>("Room ID already exist." ,HttpStatus.BAD_REQUEST);
                } else {
                    boolean added = o.addRoom(conferenceRoom);
                    if (added) {
                        roomsNames.add(conferenceRoom.getName());
                        roomsIds.add(conferenceRoom.getId());
                        return new ResponseEntity<>(HttpStatus.CREATED);
                    } else {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }
        return new ResponseEntity<>("Organization name does not exist.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public ResponseEntity<?> book(@RequestBody @Valid Reservation reservation) {
        for(Reservation r : reservationsList) {
            if (r.getClientId().equals(reservation.getClientId())) {
                return new ResponseEntity<>("This client already exist and has a reservation.", HttpStatus.BAD_REQUEST);
            } else if (r.getRoomName().equals(reservation.getRoomName())) {
                return new ResponseEntity<>("This room is already booked", HttpStatus.BAD_REQUEST);
            }
        }
        reservationsList.add(reservation);
        for (Organization o : organizationsList) {
            for (ConferenceRoom c : o.getConferenceRooms()) {
                if(c.getName().equals(reservation.getRoomName())) {
                    c.setAvailability(false);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }
            }
        }
        return new ResponseEntity<>("Reservation error.", HttpStatus.BAD_REQUEST);
    }
}
