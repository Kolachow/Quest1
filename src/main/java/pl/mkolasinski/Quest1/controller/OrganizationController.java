package pl.mkolasinski.Quest1.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mkolasinski.Quest1.model.conferenceRoom.ConferenceRoom;
import pl.mkolasinski.Quest1.model.organization.Organization;
import pl.mkolasinski.Quest1.model.reservations.NewName;
import pl.mkolasinski.Quest1.model.reservations.Reservation;

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
    @RequestMapping(value = "/organizations", method = RequestMethod.POST)                                       //add Organization
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

    @RequestMapping(value = "/organizations/{organizationName}", method = RequestMethod.GET)                                   //show Conference Rooms of one specified Organization
    public ResponseEntity<?> showOrganizationRooms(@PathVariable String organizationName) {
        for (Organization orgFromList : organizationsList) {
            if (orgFromList.getName().equals(organizationName)) {
                String organizationJson = gson.toJson(orgFromList);
                return new ResponseEntity<>(organizationJson, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("There is no such organization.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/organizations/{organizationName}", method = RequestMethod.PUT)                                        //modify Organization
    public ResponseEntity<?> changeOrganization(@PathVariable String organizationName,
                                                @RequestBody @Valid NewName newName) {
        for (Organization orgFromList : organizationsList) {
            if (orgFromList.getName().equals(organizationName)) {
                orgFromList.setName(newName.getName());
                return new ResponseEntity<>("Name of organization has been changed", HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("This name does not exist.", HttpStatus.BAD_REQUEST);
    }
    @RequestMapping(value = "/organizations/{organizationName}", method = RequestMethod.DELETE)                                     //delete Organization
    public ResponseEntity<?> deleteOrganization(@PathVariable String organizationName) {
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
    @RequestMapping(value = "/organizations/{organizationName}", method = RequestMethod.POST)                                               //add Conference Room
    public ResponseEntity<?> addRoom(@PathVariable String organizationName, @RequestBody @Valid ConferenceRoom conferenceRoom) {
        for (Organization orgFromList : organizationsList) {
            if (orgFromList.getName().toLowerCase().equals(organizationName.toLowerCase())) {
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

    @RequestMapping(value = "/organizations/{organizationName}/{roomName}", method = RequestMethod.GET)                                                //show specified Conference Room
    public ResponseEntity<?> showRoom(@PathVariable String organizationName ,@PathVariable String roomName) {
        for (Organization orgFromList : organizationsList) {
            for (ConferenceRoom roomFromList : orgFromList.getConferenceRooms()) {
                if (roomFromList.getName().equals(roomName) && orgFromList.getName().equals(organizationName)) {
                    String conferenceRoomJson = gson.toJson(roomFromList);
                    return new ResponseEntity<>(conferenceRoomJson, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("This conference room does not exist.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/organizations/{organizationName}/{roomName}", method = RequestMethod.DELETE)                                             //delete Conference Room
    public ResponseEntity<?> deleteRoom(@PathVariable String organizationName, @PathVariable String roomName) {
        for (Organization orgFromList : organizationsList) {
            for (ConferenceRoom roomFromList : orgFromList.getConferenceRooms()) {

                if (roomFromList.getName().equals(roomName) && orgFromList.getName().equals(organizationName)) {
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

    @RequestMapping(value = "/organizations/{organizationName}/{roomName}", method = RequestMethod.PUT)                                                //modify Conference Room
    public ResponseEntity<?> changeRoom(@PathVariable String organizationName, @PathVariable String roomName, @RequestBody @Valid ConferenceRoom modifiedConferenceRoom) {
        for (Organization orgFromList : organizationsList) {
            for (ConferenceRoom roomFromList : orgFromList.getConferenceRooms()) {
                if (roomFromList.getName().equals(roomName) && orgFromList.getName().equals(organizationName)) {
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
    @RequestMapping(value = "/reservations", method = RequestMethod.POST)                                        //book / add reservation
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

    @RequestMapping(value = "/reservations/{clientId}", method = RequestMethod.GET)                                        //show all reservations
    public ResponseEntity<?> showReservations(@PathVariable String clientId) {
        for (Reservation resFromList : reservationsList) {
            if (resFromList.getClientId().equals(clientId)) {
                String reservationsListJson = gson.toJson(resFromList);
                return new ResponseEntity<>(reservationsListJson, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("No such reservation.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/reservations/{clientId}", method = RequestMethod.DELETE)                                      //delete reservation
    public ResponseEntity<?> deleteReservation(@PathVariable String clientId) {
        for (Reservation resFromList : reservationsList) {
            if (resFromList.getClientId().equals(clientId)) {
                for (Organization orgFromList : organizationsList) {
                    for (ConferenceRoom roomFromList : orgFromList.getConferenceRooms()) {
                        if (roomFromList.getName().equals(resFromList.getRoomName())) {
                            roomFromList.setAvailability(true);
                            reservationsList.remove(resFromList);
                            return new ResponseEntity<>("Reservation has been removed.", HttpStatus.OK);
                        }
                    }
                }
            }
        }
        return new ResponseEntity<>("This reservation is not exist.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/reservations/{clientId}", method = RequestMethod.PUT)                                         //modify reservation
    public ResponseEntity<?> modifyReservation(@PathVariable String clientId, @RequestBody @Valid Reservation reservation) {

        for (Reservation resFromList : reservationsList) {
            if (resFromList.equals(reservation)) {
                return new ResponseEntity<>("Nothing to change.", HttpStatus.BAD_REQUEST);
            }
        }

        for (Reservation resFromList : reservationsList) {                            //searching reservation list

            if (resFromList.getClientId().equals(clientId)) {        //searching reservation to change

                for (Organization orgFromList : organizationsList) {                  //searching conference room with name to change
                    for (ConferenceRoom roomFromList : orgFromList.getConferenceRooms()) {

                        if (roomFromList.getName().equals(resFromList.getRoomName())) {

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
