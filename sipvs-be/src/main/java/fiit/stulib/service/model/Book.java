package fiit.stulib.service.model;

import lombok.Data;

@Data
public class Book {
  private String title;
  private String author;
  private String publishedIn;
  private String isbn;
  private String publisher;
}
