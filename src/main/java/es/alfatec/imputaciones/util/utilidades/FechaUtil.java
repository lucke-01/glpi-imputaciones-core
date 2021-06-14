package es.alfatec.imputaciones.util.utilidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FechaUtil {
    public static String DEFAULT_CAD_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static DateTimeFormatter DEFAULT_DATETIME_FORMAT = DateTimeFormatter.ofPattern(DEFAULT_CAD_DATETIME_FORMAT);
    
    public static String DEFAULT_CAD_LOCALDATE_FORMAT = "dd/MM/yyyy";
    
    public static LocalDateTime cadenaToLocalDateTime(String cadena) {
        return cadenaToLocalDateTime(cadena,DEFAULT_CAD_DATETIME_FORMAT);
    }
    public static LocalDateTime cadenaToLocalDateTime(String cadena, String format) {
        LocalDateTime dateTime = LocalDateTime.parse(cadena, DateTimeFormatter.ofPattern(format));
        return dateTime;
    }
    public static LocalDate cadenaToLocalDate(String cadena) {
        return cadenaToLocalDate(cadena, DEFAULT_CAD_LOCALDATE_FORMAT);
    }
    public static LocalDate cadenaToLocalDate(String cadena, String format) {
        LocalDate localDate = LocalDate.parse(cadena, DateTimeFormatter.ofPattern(format));
        return localDate;
    }
}
