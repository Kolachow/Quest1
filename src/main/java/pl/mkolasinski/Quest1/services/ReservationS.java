package pl.mkolasinski.Quest1.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mkolasinski.Quest1.model.conferenceRoom.ConferenceRoom;
import pl.mkolasinski.Quest1.model.organization.Organization;

import javax.validation.Valid;

import static pl.mkolasinski.Quest1.services.GeneralS.organizationsList;
import static pl.mkolasinski.Quest1.services.GeneralS.reservationsList;

@RestController
@RequestMapping(value = "/")
public class ReservationS {

    private GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
    private Gson gson = gsonBuilder.create();

    @RequestMapping(value = "/reservations", method = RequestMethod.POST)                //book / add ReservationS
    public ResponseEntity<?> book(@RequestBody @Valid pl.mkolasinski.Quest1.model.reservations.Reservation reservation) {
        for (pl.mkolasinski.Quest1.model.reservations.Reservation resFromList : reservationsList) {
            if (resFromList.getClientId().equals(reservation.getClientId())) {
                return new ResponseEntity<>("This client already exist and has a ReservationS.", HttpStatus.BAD_REQUEST);
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

    @RequestMapping(value = "/reservations/{clientId}", method = RequestMethod.GET)      //show all reservations
    public ResponseEntity<?> showReservations(@PathVariable String clientId) {
        for (pl.mkolasinski.Quest1.model.reservations.Reservation resFromList : reservationsList) {
            if (resFromList.getClientId().equals(clientId)) {
                String reservationsListJson = gson.toJson(resFromList);
                return new ResponseEntity<>(reservationsListJson, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("No such ReservationS.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/reservations/{clientId}", method = RequestMethod.DELETE)   //delete ReservationS
    public ResponseEntity<?> deleteReservation(@PathVariable String clientId) {
        for (pl.mkolasinski.Quest1.model.reservations.Reservation resFromList : reservationsList) {
            if (resFromList.getClientId().equals(clientId)) {
                for (Organization orgFromList : organizationsList) {
                    for (ConferenceRoom roomFromList : orgFromList.getConferenceRooms()) {
                        if (roomFromList.getName().equals(resFromList.getRoomName())) {
                            roomFromList.setAvailability(true);
                            reservationsList.remove(resFromList);
                            return new ResponseEntity<>("ReservationS has been removed.", HttpStatus.OK);
                        }
                    }
                }
            }
        }
        return new ResponseEntity<>("This ReservationS is not exist.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/reservations/{clientId}", method = RequestMethod.PUT)      //modify ReservationS
    public ResponseEntity<?> modifyReservation(@PathVariable String clientId, @RequestBody @Valid pl.mkolasinski.Quest1.model.reservations.Reservation reservation) {

        for (pl.mkolasinski.Quest1.model.reservations.Reservation resFromList : reservationsList) {
            if (resFromList.equals(reservation)) {
                return new ResponseEntity<>("Nothing to change.", HttpStatus.BAD_REQUEST);
            }
        }

        for (pl.mkolasinski.Quest1.model.reservations.Reservation resFromList : reservationsList) {                            //searching ReservationS list

            if (resFromList.getClientId().equals(clientId)) {        //searching ReservationS to change

                for (Organization orgFromList : organizationsList) {                  //searching conference room with name to change
                    for (ConferenceRoom roomFromList : orgFromList.getConferenceRooms()) {

                        if (roomFromList.getName().equals(resFromList.getRoomName())) {

                            reservationsList.remove(resFromList);                     //remove old ReservationS
                            roomFromList.setAvailability(true);                        //set availability of the room to free

                            return book(reservation);
                        }
                    }
                }
            }
        }
        return new ResponseEntity<>("There is no such room.", HttpStatus.BAD_REQUEST);
    }

}
