package pl.mkolasinski.Quest1.Model.reservations;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class NewName {

    @NotNull
    @Length(min = 2, max = 20, message = "New name should be between 2 and 20 characters")
    private String name;
}
