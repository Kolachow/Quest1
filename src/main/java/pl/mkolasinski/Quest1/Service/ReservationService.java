package pl.mkolasinski.Quest1.Service;

import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Size;

public class ReservationService {

    @UniqueElements(message = "This client ID already exist.")
    @Size(min = 2, max = 20, message = "Client ID should be between 2 and 20 characters.")
    private String reservationIsId;
}
