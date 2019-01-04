package pl.mkolasinski.Quest1.model.reservations;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class Reservation {

    @NotNull
    @Length(min = 2, max = 20, message = "Client ID should be between 2 and 20 characters")
    private String clientId;

    @NotNull
    private String roomName;

    @NotNull
    private LocalDateTime startReservation;      //format 2007-12-03T10:15:30

    @NotNull
    private LocalDateTime endOfReservation;
}
