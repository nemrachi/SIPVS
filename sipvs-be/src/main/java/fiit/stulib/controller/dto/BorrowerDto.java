package fiit.stulib.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class BorrowerDto {

  @Size(max = 50)
  private String name;

  @Size(max = 50)
  private String surname;

  @Size(max = 15)
  @NotNull
  private String cardNumber;
}
