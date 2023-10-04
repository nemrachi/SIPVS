package fiit.stulib.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class BookDto {

  @Size(max = 50)
  @NotNull
  private String title;

  @Size(max = 100)
  @NotNull
  private String author;

  @Size(max = 20)
  private String publishedIn;

  @Size(max = 20)
  @NotNull
  private String isbn;

  @Size(max = 75)
  private String publisher;
}
