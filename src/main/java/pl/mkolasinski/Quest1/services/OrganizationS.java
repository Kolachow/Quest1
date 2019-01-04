package pl.mkolasinski.Quest1.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mkolasinski.Quest1.model.conferenceRoom.ConferenceRoom;
import pl.mkolasinski.Quest1.model.reservations.NewName;
import pl.mkolasinski.Quest1.model.reservations.Reservation;

import javax.validation.Valid;

import java.util.LinkedList;
import java.util.List;

import static pl.mkolasinski.Quest1.services.GeneralS.*;

@RestController
@RequestMapping(value = "/")
public class OrganizationS {

    private GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
    private Gson gson = gsonBuilder.create();

    @RequestMapping(value = "/organizations", method = RequestMethod.POST)                           //add OrganizationS
    public ResponseEntity<?> addOrganization(@RequestBody @Valid pl.mkolasinski.Quest1.model.organization.Organization organization) {
        for (pl.mkolasinski.Quest1.model.organization.Organization orgFromList : organizationsList) {
            if (orgFromList.getName().equals(organization.getName().trim())) {
                return new ResponseEntity<>("OrganizationS name already exist", HttpStatus.BAD_REQUEST);
            }
        }

        if (organization.getName().trim().length() > 1 && organization.getName().trim().length() < 21) {
            organization.setName(organization.getName().trim());
            organizationsList.add(organization);
            return new ResponseEntity<>("OrganizationS created.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("OrganizationS name is NOT between 2 and 20 charakters.", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/organizations", method = RequestMethod.GET)                            //show all Organizations without Conference Rooms
    public ResponseEntity<?> showOrganizations() {
        List<String> rawOrganizationsList = new LinkedList<>();
        for (pl.mkolasinski.Quest1.model.organization.Organization orgFromList : organizationsList) {
            rawOrganizationsList.add(orgFromList.getName());
        }
        String rawOrganizationsListJson = gson.toJson(rawOrganizationsList);
        return new ResponseEntity<>(rawOrganizationsListJson, HttpStatus.OK);
    }

    @RequestMapping(value = "/organizations/{organizationName}", method = RequestMethod.GET)         //show Conference Rooms of one specified OrganizationS
    public ResponseEntity<?> showOrganizationRooms(@PathVariable String organizationName) {
        for (pl.mkolasinski.Quest1.model.organization.Organization orgFromList : organizationsList) {
            if (orgFromList.getName().equals(organizationName)) {
                String organizationJson = gson.toJson(orgFromList);
                return new ResponseEntity<>(organizationJson, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("There is no such organization.", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/organizations/{organizationName}", method = RequestMethod.PUT)         //modify OrganizationS
    public ResponseEntity<?> changeOrganization(@PathVariable String organizationName,
                                                @RequestBody @Valid NewName newName) {
        for (pl.mkolasinski.Quest1.model.organization.Organization orgFromList : organizationsList) {
            if (orgFromList.getName().equals(organizationName)) {
                orgFromList.setName(newName.getName());
                return new ResponseEntity<>("Name of organization has been changed", HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("This name does not exist.", HttpStatus.BAD_REQUEST);
    }
    @RequestMapping(value = "/organizations/{organizationName}", method = RequestMethod.DELETE)      //delete OrganizationS
    public ResponseEntity<?> deleteOrganization(@PathVariable String organizationName) {
        for (pl.mkolasinski.Quest1.model.organization.Organization orgFromList : organizationsList) {

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

                return new ResponseEntity<>("OrganizationS has been removed.", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("OrganizationS does not exist", HttpStatus.BAD_REQUEST);
    }
}
