package procesamientoCsv;

import es.alfatec.imputaciones.util.fichero.extractor.ExtractorFicheroTareas;
import es.alfatec.imputaciones.util.modelo.Imputable;
import es.alfatec.imputaciones.util.modelo.TareaImputada;
import es.alfatec.imputaciones.util.modelo.TareaListado;
import es.alfatec.imputaciones.util.utilidades.FechaUtil;
import es.alfatec.imputaciones.util.utilidades.FicheroUtil;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.ImputacionTesting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Order;

/**
 *
 * @author ricardo
 */
public class ExtraccionTareasTest {
    
    public ExtraccionTareasTest() {
    }
    //@Test
    @Order(1)
    public void testExtraccionCsv_ejemplo1() {
        final String contenidoFichero = FicheroUtil.fileToString(new File(ExtraccionTareasTest.class.getResource("/test/tareasCsv/glpi_ejemplo1.csv").getFile()));
        Imputable imputacion = new ImputacionTesting(FechaUtil.cadenaToLocalDate("01/04/2021"),FechaUtil.cadenaToLocalDate("30/04/2021"));
        final ExtractorFicheroTareas procesadorFicheroTareas = new ExtractorFicheroTareas(
                contenidoFichero,imputacion
        );
        
        List<TareaImputada> tareas = procesadorFicheroTareas.extraeFicheroTareas();
        
        assertEquals(tareas.size(),28);
        
        System.out.println(tareas.get(0));
    }
    
    @Test
    @Order(2)
    public void testExtraccionTareas_ejemplo1() {
        final String contenidoFichero = FicheroUtil.fileToString(new File(ExtraccionTareasTest.class.getResource("/test/tareasCsv/glpi_ejemplo1.csv").getFile()));
        List<LocalDate> vacaciones = new ArrayList<>();
        vacaciones.add(FechaUtil.cadenaToLocalDate("01/04/2021"));
        vacaciones.add(FechaUtil.cadenaToLocalDate("02/04/2021"));
        vacaciones.add(FechaUtil.cadenaToLocalDate("05/04/2021"));
        vacaciones.add(FechaUtil.cadenaToLocalDate("06/04/2021"));
        vacaciones.add(FechaUtil.cadenaToLocalDate("07/04/2021"));
        vacaciones.add(FechaUtil.cadenaToLocalDate("08/04/2021"));
        vacaciones.add(FechaUtil.cadenaToLocalDate("09/04/2021"));
        
        Imputable imputacion = new ImputacionTesting(FechaUtil.cadenaToLocalDate("01/04/2021"),FechaUtil.cadenaToLocalDate("30/04/2021"),vacaciones);
        
        final ExtractorFicheroTareas procesadorFicheroTareas = new ExtractorFicheroTareas(
                contenidoFichero,imputacion
        );
        
        TareaListado tareaListado =  procesadorFicheroTareas.extraeTareaListado();
        //System.out.println("TAREAS AGRUPADAS: ");
        //tareaListado.getListaTareasAgrupada().forEach(System.out::println);
        System.out.println("TAREA LISTADO");
        System.out.println(tareaListado);
        System.out.println("tareaListado.getHorasTotales()");
        System.out.println(tareaListado.getHorasTotales());
        System.out.println("tareaListado.getHorasImputadas()");
        System.out.println(tareaListado.getHorasImputadas());
    }
}
