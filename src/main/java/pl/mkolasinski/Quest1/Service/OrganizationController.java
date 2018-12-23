package pl.mkolasinski.Quest1.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mkolasinski.Quest1.Model.conferenceRoom.ConferenceRoom;
import pl.mkolasinski.Quest1.Model.organization.Organization;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/organizations")
public class OrganizationController {

    private List<Organization> organizationsList = new LinkedList<>();

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> addOrganization(@RequestBody Organization organization) {
        for (Organization o: organizationsList) {
            if (o.getName().equals(organization.getName().trim())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        if (organization.getName().trim().length() > 1 && organization.getName().trim().length() < 21) {
            organization.setName(organization.getName().trim());
            organizationsList.add(organization);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/showall", method = RequestMethod.GET)
    public ResponseEntity<List<Organization>> showOrganizations() {
        return new ResponseEntity<>(organizationsList, HttpStatus.OK);
    }

    @RequestMapping(value = "/addroom", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> addRoom(@RequestParam(value = "id") String id, @RequestBody ConferenceRoom conferenceRoom) {
        for (Organization o : organizationsList) {
            if (o.getName().equals(id)) {
                o.addRoom(conferenceRoom);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
