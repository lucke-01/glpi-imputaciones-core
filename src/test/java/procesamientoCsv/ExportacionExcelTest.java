package procesamientoCsv;

import es.alfatec.imputaciones.util.modelo.FormatoExportacion;
import es.alfatec.imputaciones.util.fichero.extractor.ExtractorFicheroTareas;
import es.alfatec.imputaciones.util.fichero.exportador.ExportadorFicheroFactory;
import es.alfatec.imputaciones.util.fichero.exportador.ExportadorFicheroTareas;
import es.alfatec.imputaciones.util.modelo.Imputable;
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
import org.junit.jupiter.api.Disabled;
public class ExportacionExcelTest {
    
    public ExportacionExcelTest() {
    }
    @Disabled
    @Test
    public void testExportacionExcel_ejemplo1() {
        final String contenidoFichero = FicheroUtil.fileToString(new File(ExportacionExcelTest.class.getResource("/test/tareasCsv/glpi_ejemplo1.csv").getFile()));
        List<LocalDate> vacaciones = new ArrayList<>();
        vacaciones.add(FechaUtil.cadenaToLocalDate("01/04/2021"));
        vacaciones.add(FechaUtil.cadenaToLocalDate("02/04/2021"));
        vacaciones.add(FechaUtil.cadenaToLocalDate("05/04/2021"));
        vacaciones.add(FechaUtil.cadenaToLocalDate("06/04/2021"));
        vacaciones.add(FechaUtil.cadenaToLocalDate("07/04/2021"));
        vacaciones.add(FechaUtil.cadenaToLocalDate("08/04/2021"));
        vacaciones.add(FechaUtil.cadenaToLocalDate("09/04/2021"));
        
        Imputable imputacion = new ImputacionTesting(FechaUtil.cadenaToLocalDate("01/04/2021"),FechaUtil.cadenaToLocalDate("30/04/2021"),vacaciones );
        
        final ExtractorFicheroTareas procesadorFicheroTareas = new ExtractorFicheroTareas(contenidoFichero,imputacion);
        
        TareaListado tareaListado =  procesadorFicheroTareas.extraeTareaListado();
        
        ExportadorFicheroTareas exportador = ExportadorFicheroFactory
                .getExportadorFicheroTareas(tareaListado, "./src/test/ficherosResultado/ficheroPrueba.xlsx", FormatoExportacion.EXCEL);
        exportador.exportar();
    }
    @Test
    public void testExportacionExcel_glpi_junio2021_faltanDias_ejemplo2() {
        final String contenidoFichero = FicheroUtil.fileToString(new File(ExportacionExcelTest.class.getResource("/test/tareasCsv/glpi_junio2021_faltanDias_ejemplo2.csv").getFile()));
        List<LocalDate> vacaciones = new ArrayList<>();
        vacaciones.add(FechaUtil.cadenaToLocalDate("28/06/2021"));
        vacaciones.add(FechaUtil.cadenaToLocalDate("29/06/2021"));
        vacaciones.add(FechaUtil.cadenaToLocalDate("30/06/2021"));
        List<LocalDate> festivos = new ArrayList<>();
        festivos.add(FechaUtil.cadenaToLocalDate("09/06/2021"));
        Imputable imputacion = new ImputacionTesting(FechaUtil.cadenaToLocalDate("01/06/2021"),FechaUtil.cadenaToLocalDate("30/06/2021"),vacaciones,festivos );
        final ExtractorFicheroTareas extractorFicheroTareas = new ExtractorFicheroTareas(
                contenidoFichero,imputacion
        );
        
        TareaListado tareaListado =  extractorFicheroTareas.extraeTareaListado();
        
        ExportadorFicheroTareas exportador = ExportadorFicheroFactory
                .getExportadorFicheroTareas(tareaListado, "./src/test/ficherosResultado/prueba_glpi_junio2021_faltanDias_ejemplo2.xlsx", FormatoExportacion.EXCEL);
        exportador.exportar();
    }
}
