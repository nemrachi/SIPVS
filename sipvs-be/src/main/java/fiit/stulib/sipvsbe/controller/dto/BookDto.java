package fiit.stulib.sipvsbe.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class BookDto {

    @NotNull
    @Size(max = 13)
    private String isbn;

}
