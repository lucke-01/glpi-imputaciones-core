package es.alfatec.imputaciones.util.utilidades;

public class CadenaUtil {

    public static String removeLastChar(String cadena) {
        return removeLastChars(cadena, 1);
    }

    public static String removeLastChars(String cadena, int chars) {
        if (cadena == null || cadena.isEmpty()) {
            return cadena;
        }
        return cadena.substring(0, cadena.length() - chars);
    }
}
