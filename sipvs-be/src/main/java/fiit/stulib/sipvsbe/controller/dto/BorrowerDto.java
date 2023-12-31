package fiit.stulib.sipvsbe.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class BorrowerDto {

    @NotNull
    @Size(min = 7, max = 7)
    private String cardNumber;
}
