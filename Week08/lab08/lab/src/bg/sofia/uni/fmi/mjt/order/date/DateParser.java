package bg.sofia.uni.fmi.mjt.order.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateParser {

    //Takes in a string in format "dd-MM-yy" and returns LocalDate
    public static LocalDate parse(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yy"));
    }

}
