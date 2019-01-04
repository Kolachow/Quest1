package pl.mkolasinski.Quest1.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mkolasinski.Quest1.model.conferenceRoom.ConferenceRoom;
import pl.mkolasinski.Quest1.model.organization.Organization;
import pl.mkolasinski.Quest1.model.reservations.Reservation;

import javax.validation.Valid;

import static pl.mkolasinski.Quest1.services.GeneralS.*;

@RestController
@RequestMapping(value = "/")
public class ConferenceRoomS {

    private GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
    private Gson gson = gsonBuilder.create();

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
        return new ResponseEntity<>("OrganizationS name does not exist.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/organizations/{organizationName}/{roomName}", method = RequestMethod.GET)                                     //show specified Conference Room
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

    @RequestMapping(value = "/organizations/{organizationName}/{roomName}", method = RequestMethod.DELETE)                                  //delete Conference Room
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

    @RequestMapping(value = "/organizations/{organizationName}/{roomName}", method = RequestMethod.PUT)                                     //modify Conference Room
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

}
