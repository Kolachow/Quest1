package pl.mkolasinski.Quest1.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mkolasinski.Quest1.Model.organization.Organization;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/reservation")
public class ReservationService {

    private String reservationIsId;
//
//    private List<String> reservationsIdsList = new LinkedList<>();      //lista ID rezerwujących
//    private List<Organization> organizationsList = new LinkedList<>();        //lista organizacji
//
//
//    @RequestMapping(value = "/createorganization", method = RequestMethod.POST)
//    public ResponseEntity<HttpStatus> createOrganization(@RequestBody Organization organization) {
//        for (Organization o: organizationsList) {
//            if (o.getName().equals(organization.getName().trim())) {
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//        }
//
//        if (organization.getName().trim().length() > 1 && organization.getName().trim().length() < 21) {
//            organization.setName(organization.getName().trim());
//            organizationsList.add(organization);
//            return new ResponseEntity<>(HttpStatus.CREATED);
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @ResponseStatus(value = HttpStatus.OK)
//    @RequestMapping(value = "showorganizations", method = RequestMethod.GET)
//    public List<Organization> showOrganizations() {
//        return organizationsList;
//    }
//
//    @RequestMapping(value = "/createorganization/{id}", method = RequestMethod.POST)
//    public ResponseEntity<HttpStatus> addRoomToOrganization(@PathVariable(value = "id") String id, @RequestBody ConferenceRoom conferenceRoom) {
//        for (Organization o : organizationsList) {
//            if (o.getName().equals(id)) {
//
//            }
//        }
//    }
}
