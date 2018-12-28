package pl.mkolasinski.Quest1.Model.conferenceRoom;

import lombok.Data;

@Data
public class RoomModifications {

    private String name;

    private String id;

    private int floor = -1;

    private int numberOfSeatsAndStanding = -1;

    private int numberOfLyingPlaces = -1;

    private int numberOfHangingPlaces = -1;

    private boolean phone = true;

    private int internalNumber = -1;

    private String externalNumber = "+00 000000000";

    private PhoneInterface phoneInterface;


}
