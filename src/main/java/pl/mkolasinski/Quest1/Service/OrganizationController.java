package pl.mkolasinski.Quest1.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mkolasinski.Quest1.Model.conferenceRoom.ConferenceRoom;
import pl.mkolasinski.Quest1.Model.organization.Organization;
import pl.mkolasinski.Quest1.Model.reservations.NewName;
import pl.mkolasinski.Quest1.Model.reservations.Reservation;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

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
        for (Organization o : organizationsList) {
            if (o.getName().equals(organization.getName().trim())) {
                return new ResponseEntity<>("Organization name already exist", HttpStatus.BAD_REQUEST);
            }
        }

        if (organization.getName().trim().length() > 1 && organization.getName().trim().length() < 21) {
            organization.setName(organization.getName().trim());
            organizationsList.add(organization);
            return new ResponseEntity<>("Organization created.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Organization name is NOT between 2 and 20 charakters.", HttpStatus.BAD_REQUEST);
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

    @RequestMapping(value = "/showorgrooms", method = RequestMethod.GET)
    public ResponseEntity<?> showOrganizationRooms(@RequestParam(value = "id") String organizationName) {
        Organization organization = new Organization();
        for (Organization o : organizationsList) {
            if (o.getName().equals(organizationName)) {
                organization = o;
                break;
            }
        }
        return new ResponseEntity<>(organization, HttpStatus.OK);
    }

    @RequestMapping(value = "/changeorganization", method = RequestMethod.PUT)
    public ResponseEntity<?> changeOrganization(@RequestParam(value = "id") String organizationName,
                                                @RequestBody @Valid NewName newName) {
        for (Organization o : organizationsList) {
            if (o.getName().equals(organizationName)) {
                o.setName(newName.getName());
                return new ResponseEntity<>("Name of organization has been changed", HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("This name does not exist.", HttpStatus.BAD_REQUEST);
    }
    @RequestMapping(value = "/deleteorganization", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteOrganization(@RequestParam(value = "id") String organizationName) {
        for (Organization o : organizationsList) {

            if (o.getName().equals(organizationName)) {
                for (ConferenceRoom conRoom : o.getConferenceRooms()) {
                    for (Reservation res : reservationsList) {
                        if (res.getRoomName().equals(conRoom.getName())) {
                            reservationsList.remove(res);
                        }
                    }
                }

                organizationsList.remove(o);
                return new ResponseEntity<>("Organization has been removed.", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Organization does not exist", HttpStatus.BAD_REQUEST);
    }

//Conference room CRUD
    @RequestMapping(value = "/addroom", method = RequestMethod.POST)
    public ResponseEntity<?> addRoom(@RequestParam(value = "id") String orgName, @RequestBody @Valid ConferenceRoom conferenceRoom) {
        for (Organization o : organizationsList) {
            if (o.getName().toLowerCase().equals(orgName.toLowerCase())) {
                if (roomsNames.contains(conferenceRoom.getName())) {
                    return new ResponseEntity<>("Room name already exist.", HttpStatus.BAD_REQUEST);
                } else if (roomsIds.contains(conferenceRoom.getId())) {
                    return new ResponseEntity<>("Room ID already exist.", HttpStatus.BAD_REQUEST);
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

    @RequestMapping(value = "/showroom", method = RequestMethod.GET)
    public ResponseEntity<?> showRoom(@RequestParam(value = "id") String roomName) {
        for (Organization o : organizationsList) {
            for (ConferenceRoom c : o.getConferenceRooms()) {
                if (c.getName().equals(roomName)) {
                    return new ResponseEntity<>(c, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("This conference room does not exist.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/deleteroom", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRoom(@RequestParam(value = "id") String roomName) {
        for (Organization o : organizationsList) {
            for (ConferenceRoom c : o.getConferenceRooms()) {

                if (c.getName().equals(roomName)) {
                    for(Reservation res : reservationsList) {
                        if (res.getRoomName().equals(c.getName())) {
                            reservationsList.remove(res);
                        }
                    }

                    roomsNames.remove(c.getName());
                    roomsIds.remove(c.getId());
                    o.getConferenceRooms().remove(c);

                    return new ResponseEntity<>("Room has been removed.", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Conference room does not exist", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/changeroom", method = RequestMethod.PUT)
    public ResponseEntity<?> changeRoom(@RequestParam(value = "id") String roomName, @RequestBody ConferenceRoom modifiedConferenceRoom) {
        for (Organization o : organizationsList) {
            for (ConferenceRoom c : o.getConferenceRooms()) {
                if (roomsNames.contains(modifiedConferenceRoom.getName())) {
                    return new ResponseEntity<>("This room name already exist in the system", HttpStatus.BAD_REQUEST);
                } else if (roomsIds.contains(modifiedConferenceRoom.getId())) {
                    return new ResponseEntity<>("This room ID already exist in the system", HttpStatus.BAD_REQUEST);
                } else if (c.getName().equals(roomName)) {
                    roomsNames.remove(c.getName());
                    roomsIds.remove(c.getId());
                    o.getConferenceRooms().remove(c);


                    roomsNames.add(modifiedConferenceRoom.getName());
                    roomsIds.add(modifiedConferenceRoom.getId());
                    o.getConferenceRooms().add(modifiedConferenceRoom);
                    return new ResponseEntity<>("Conference room has been changed", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("This name does not exist in the system.", HttpStatus.BAD_REQUEST);
    }

//reservation CRUD
    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public ResponseEntity<?> book(@RequestBody @Valid Reservation reservation) {
        for (Reservation r : reservationsList) {
            if (r.getClientId().equals(reservation.getClientId())) {
                return new ResponseEntity<>("This client already exist and has a reservation.", HttpStatus.BAD_REQUEST);
            } else if (r.getRoomName().equals(reservation.getRoomName())) {
                return new ResponseEntity<>("This room is already booked", HttpStatus.BAD_REQUEST);
            }
        }

        for (Organization o : organizationsList) {
            for (ConferenceRoom c : o.getConferenceRooms()) {
                if (c.getName().equals(reservation.getRoomName())) {
                    c.setAvailability(false);
                    reservationsList.add(reservation);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }
            }
        }
        return new ResponseEntity<>("This room does not exist in the system", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/showreservations", method = RequestMethod.GET)
    public ResponseEntity<?> showReservations() {
        return new ResponseEntity<>(reservationsList, HttpStatus.OK);
    }

    @RequestMapping(value = "/deletereservation", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteReservation(@RequestParam(value = "id") String roomName) {
        for (Reservation r : reservationsList) {
            if (r.getRoomName().equals(roomName)) {
                reservationsList.remove(r);

                for (Organization o : organizationsList) {
                    for (ConferenceRoom c : o.getConferenceRooms()) {
                        if (c.getName().equals(roomName)) {
                            c.setAvailability(true);
                            return new ResponseEntity<>("Reservation has been removed.", HttpStatus.OK);
                        }
                    }
                }
            }
        }
        return new ResponseEntity<>("This reservation is not exist.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/modifyreservation", method = RequestMethod.PUT)
    public ResponseEntity<?> modifyReservation(@RequestBody Reservation reservation) {

        for (Reservation res : reservationsList) {
            if (res.equals(reservation)) {
                return new ResponseEntity<>("Nothing to change.", HttpStatus.BAD_REQUEST);
            }
        }

        for (Reservation r : reservationsList) {                            //searching reservation list

            if (r.getRoomName().equals(reservation.getRoomName())) {                         //searching reservation to change

                for (Organization o : organizationsList) {                  //searching conference room with name to change
                    for (ConferenceRoom c : o.getConferenceRooms()) {

                        if (c.getName().equals(reservation.getRoomName())) {

                            reservationsList.remove(r);                     //remove old reservation
                            c.setAvailability(true);                        //set availability of the room to free

                            book(reservation);
                            return new ResponseEntity<>("Reservation has been changed.", HttpStatus.OK);
                        }
                    }
                }
            }
        }
        return new ResponseEntity<>("There is no such room.", HttpStatus.BAD_REQUEST);
    }


//General
    @RequestMapping(value = "/showallrooms", method = RequestMethod.GET)
    public ResponseEntity<List<Organization>> showAllRooms() {
        return new ResponseEntity<>(organizationsList, HttpStatus.OK);
    }

    @RequestMapping(value = "/showallreservations", method = RequestMethod.GET)
    public ResponseEntity<List<Reservation>> showAllReservations() {
        return new ResponseEntity<>(reservationsList, HttpStatus.OK);
    }
}
