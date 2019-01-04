package pl.mkolasinski.Quest1.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mkolasinski.Quest1.model.organization.Organization;
import pl.mkolasinski.Quest1.model.reservations.Reservation;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/")
public class GeneralS {

    protected static List<Organization> organizationsList = new LinkedList<>();
    protected static List<String> roomsNames = new LinkedList<>();
    protected static List<String> roomsIds = new LinkedList<>();
    protected static List<Reservation> reservationsList = new LinkedList<>();

    private GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
    private Gson gson = gsonBuilder.create();

    //GeneralS
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
