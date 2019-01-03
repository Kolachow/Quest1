package pl.mkolasinski.Quest1.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
@RequestMapping(value = "/")
public class OrganizationController {

    private List<Organization> organizationsList = new LinkedList<>();
    private List<String> roomsNames = new LinkedList<>();
    private List<String> roomsIds = new LinkedList<>();
    private List<Reservation> reservationsList = new LinkedList<>();

    private GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
    private Gson gson = gsonBuilder.create();

    //organizations CRUD
    @RequestMapping(value = "/organization", method = RequestMethod.POST)                                       //add Organization
    public ResponseEntity<?> addOrganization(@RequestBody @Valid Organization organization) {
        for (Organization orgFromList : organizationsList) {
            if (orgFromList.getName().equals(organization.getName().trim())) {
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

    @RequestMapping(value = "/organizations", method = RequestMethod.GET)                                       //show all Organizations without Conference Rooms
    public ResponseEntity<?> showOrganizations() {
        List<String> rawOrganizationsList = new LinkedList<>();
        for (Organization orgFromList : organizationsList) {
            rawOrganizationsList.add(orgFromList.getName());
        }
        String rawOrganizationsListJson = gson.toJson(rawOrganizationsList);
        return new ResponseEntity<>(rawOrganizationsListJson, HttpStatus.OK);
    }

    @RequestMapping(value = "/organizationrooms", method = RequestMethod.GET)                                   //show Conference Rooms of one specified Organization
    public ResponseEntity<?> showOrganizationRooms(@RequestParam(value = "id") String organizationName) {
        for (Organization orgFromList : organizationsList) {
            if (orgFromList.getName().equals(organizationName)) {
                String organizationJson = gson.toJson(orgFromList);
                return new ResponseEntity<>(organizationJson, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("There is no such organization.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/organization", method = RequestMethod.PUT)                                        //modify Organization
    public ResponseEntity<?> changeOrganization(@RequestParam(value = "id") String organizationName,
                                                @RequestBody @Valid NewName newName) {
        for (Organization orgFromList : organizationsList) {
            if (orgFromList.getName().equals(organizationName)) {
                orgFromList.setName(newName.getName());
                return new ResponseEntity<>("Name of organization has been changed", HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("This name does not exist.", HttpStatus.BAD_REQUEST);
    }
    @RequestMapping(value = "/organization", method = RequestMethod.DELETE)                                     //delete Organization
    public ResponseEntity<?> deleteOrganization(@RequestParam(value = "id") String organizationName) {
        for (Organization orgFromList : organizationsList) {

            if (orgFromList.getName().equals(organizationName)) {
                for (ConferenceRoom roomFromList : orgFromList.getConferenceRooms()) {
                    for (Reservation res : reservationsList) {
                        if (res.getRoomName().equals(roomFromList.getName())) {
                            roomsNames.remove(roomFromList.getName());
                            roomsIds.remove(roomFromList.getId());
                            reservationsList.remove(res);
                        }
                    }
                }

                organizationsList.remove(orgFromList);

                return new ResponseEntity<>("Organization has been removed.", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Organization does not exist", HttpStatus.BAD_REQUEST);
    }

    //Conference room CRUD
    @RequestMapping(value = "/room", method = RequestMethod.POST)                                               //add Conference Room
    public ResponseEntity<?> addRoom(@RequestParam(value = "id") String orgName, @RequestBody @Valid ConferenceRoom conferenceRoom) {
        for (Organization orgFromList : organizationsList) {
            if (orgFromList.getName().toLowerCase().equals(orgName.toLowerCase())) {
                if (roomsNames.contains(conferenceRoom.getName())) {
                    return new ResponseEntity<>("Room name already exist.", HttpStatus.BAD_REQUEST);
                } else if (roomsIds.contains(conferenceRoom.getId())) {
                    return new ResponseEntity<>("Room ID already exist.", HttpStatus.BAD_REQUEST);
                } else {
                    boolean added = orgFromList.addRoom(conferenceRoom);
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

    @RequestMapping(value = "/room", method = RequestMethod.GET)                                                //show specified Conference Room
    public ResponseEntity<?> showRoom(@RequestParam(value = "id") String roomName) {
        for (Organization orgFromList : organizationsList) {
            for (ConferenceRoom roomFromList : orgFromList.getConferenceRooms()) {
                if (roomFromList.getName().equals(roomName)) {
                    String conferenceRoomJson = gson.toJson(roomFromList);
                    return new ResponseEntity<>(conferenceRoomJson, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("This conference room does not exist.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/room", method = RequestMethod.DELETE)                                             //delete Conference Room
    public ResponseEntity<?> deleteRoom(@RequestParam(value = "id") String roomName) {
        for (Organization orgFromList : organizationsList) {
            for (ConferenceRoom roomFromList : orgFromList.getConferenceRooms()) {

                if (roomFromList.getName().equals(roomName)) {
                    for(Reservation res : reservationsList) {
                        if (res.getRoomName().equals(roomFromList.getName())) {
                            reservationsList.remove(res);
                        }
                    }

                    roomsNames.remove(roomFromList.getName());
                    roomsIds.remove(roomFromList.getId());
                    orgFromList.getConferenceRooms().remove(roomFromList);

                    return new ResponseEntity<>("Room has been removed.", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Conference room does not exist", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/room", method = RequestMethod.PUT)                                                //modify Conference Room
    public ResponseEntity<?> changeRoom(@RequestParam(value = "id") String roomName, @RequestBody @Valid ConferenceRoom modifiedConferenceRoom) {
        for (Organization orgFromList : organizationsList) {
            for (ConferenceRoom roomFromList : orgFromList.getConferenceRooms()) {
                if (roomsNames.contains(modifiedConferenceRoom.getName())) {
                    return new ResponseEntity<>("This room name already exist in the system", HttpStatus.BAD_REQUEST);
                } else if (roomsIds.contains(modifiedConferenceRoom.getId())) {
                    return new ResponseEntity<>("This room ID already exist in the system", HttpStatus.BAD_REQUEST);
                } else if (roomFromList.getName().equals(roomName)) {
                    roomsNames.remove(roomFromList.getName());
                    roomsIds.remove(roomFromList.getId());
                    orgFromList.getConferenceRooms().remove(roomFromList);


                    roomsNames.add(modifiedConferenceRoom.getName());
                    roomsIds.add(modifiedConferenceRoom.getId());
                    orgFromList.getConferenceRooms().add(modifiedConferenceRoom);
                    return new ResponseEntity<>("Conference room has been changed", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("This name does not exist in the system.", HttpStatus.BAD_REQUEST);
    }

    //reservation CRUD
    @RequestMapping(value = "/reservation", method = RequestMethod.POST)                                        //book / add reservation
    public ResponseEntity<?> book(@RequestBody @Valid Reservation reservation) {
        for (Reservation resFromList : reservationsList) {
            if (resFromList.getClientId().equals(reservation.getClientId())) {
                return new ResponseEntity<>("This client already exist and has a reservation.", HttpStatus.BAD_REQUEST);
            } else if (resFromList.getRoomName().equals(reservation.getRoomName())) {
                return new ResponseEntity<>("This room is already booked", HttpStatus.BAD_REQUEST);
            }
        }

        for (Organization orgFromList : organizationsList) {
            for (ConferenceRoom roomFromList : orgFromList.getConferenceRooms()) {
                if (roomFromList.getName().equals(reservation.getRoomName())) {
                    roomFromList.setAvailability(false);
                    reservationsList.add(reservation);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }
            }
        }
        return new ResponseEntity<>("This room does not exist in the system", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/reservations", method = RequestMethod.GET)                                        //show all reservations
    public ResponseEntity<?> showReservations() {
        String reservationsListJson = gson.toJson(reservationsList);
        return new ResponseEntity<>(reservationsListJson, HttpStatus.OK);
    }

    @RequestMapping(value = "/reservation", method = RequestMethod.DELETE)                                      //delete reservation
    public ResponseEntity<?> deleteReservation(@RequestParam(value = "id") String roomName) {
        for (Reservation resFromList : reservationsList) {
            if (resFromList.getRoomName().equals(roomName)) {
                reservationsList.remove(resFromList);

                for (Organization orgFromList : organizationsList) {
                    for (ConferenceRoom roomFromList : orgFromList.getConferenceRooms()) {
                        if (roomFromList.getName().equals(roomName)) {
                            roomFromList.setAvailability(true);
                            return new ResponseEntity<>("Reservation has been removed.", HttpStatus.OK);
                        }
                    }
                }
            }
        }
        return new ResponseEntity<>("This reservation is not exist.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/reservation", method = RequestMethod.PUT)                                         //modify reservation
    public ResponseEntity<?> modifyReservation(@RequestBody @Valid Reservation reservation) {

        for (Reservation resFromList : reservationsList) {
            if (resFromList.equals(reservation)) {
                return new ResponseEntity<>("Nothing to change.", HttpStatus.BAD_REQUEST);
            }
        }

        for (Reservation resFromList : reservationsList) {                            //searching reservation list

            if (resFromList.getRoomName().equals(reservation.getRoomName())) {        //searching reservation to change

                for (Organization orgFromList : organizationsList) {                  //searching conference room with name to change
                    for (ConferenceRoom roomFromList : orgFromList.getConferenceRooms()) {

                        if (roomFromList.getName().equals(reservation.getRoomName())) {

                            reservationsList.remove(resFromList);                     //remove old reservation
                            roomFromList.setAvailability(true);                        //set availability of the room to free

                            return book(reservation);
                        }
                    }
                }
            }
        }
        return new ResponseEntity<>("There is no such room.", HttpStatus.BAD_REQUEST);
    }


    //General
    @RequestMapping(value = "/allrooms", method = RequestMethod.GET)                                            //SHow all Organizations and their rooms
    public ResponseEntity<?> showAllRooms() {
        String organizationsListJson = gson.toJson(organizationsList);
        return new ResponseEntity<>(organizationsListJson, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)                                                    //Home page - welcome
    public ResponseEntity<?> welcome() {
        return new ResponseEntity<>("Welcome!", HttpStatus.OK);
    }
}
