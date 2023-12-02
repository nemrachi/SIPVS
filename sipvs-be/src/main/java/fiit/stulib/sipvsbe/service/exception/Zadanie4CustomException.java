package fiit.stulib.sipvsbe.service.exception;

public class Zadanie4CustomException extends Exception {

    public Zadanie4CustomException(String s) {
        super(s);
    }

    public Zadanie4CustomException(String s, Exception e) {
        super(s, e);
    }
}
