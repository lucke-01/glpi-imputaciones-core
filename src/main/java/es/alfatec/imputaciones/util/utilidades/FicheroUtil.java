package es.alfatec.imputaciones.util.utilidades;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FicheroUtil {
    public static String fileToString(File file) {
        String contentFile;
        try {
            Stream<String> lines = Files.lines(file.toPath(), StandardCharsets.UTF_8);
            contentFile = lines.collect(Collectors.joining("\n"));
            contentFile = deleteBoom(contentFile);
            //contentFile = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return contentFile;
    }
    /**
     * Boom es el primer caracter de ficheros utf-8 con boom que indica informacion acerca de la codificacion
     * (es una cosa que no debiera existir)
     * @return 
     */
    public static String deleteBoom(String contenidoFichero) {
        return  contenidoFichero.replaceAll("\\A\uFEFF", "").replaceAll("\\A\uEFBBBF", "");
    }
    public static String deleteFirtLine(String contenidoFichero) {
        return contenidoFichero.substring(contenidoFichero.indexOf('\n')+1);
    }
}
