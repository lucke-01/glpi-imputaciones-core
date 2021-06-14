package es.alfatec.imputaciones.util.fichero.exportador;

import es.alfatec.imputaciones.util.modelo.FormatoExportacion;
import es.alfatec.imputaciones.util.modelo.TareaListado;

public class ExportadorFicheroFactory {
    public static ExportadorFicheroTareas getExportadorFicheroTareas(TareaListado tareaListado,String rutaFichero,FormatoExportacion formatoExportacion) {
        ExportadorFicheroTareas exportador = null;
        
        if (null == formatoExportacion) {
            throw new IllegalArgumentException("Formato exportacion indicado invalido");
        }
        
        switch (formatoExportacion) {
            case EXCEL:
                exportador = new ExportadorFicheroTareasExcel(tareaListado, rutaFichero);
                break;
            case CSV:
                exportador = new ExportadorFicheroTareasCsv(tareaListado, rutaFichero);
                break;
            default:
                throw new IllegalArgumentException("Formato exportacion indicado invalido");
        }
        
        return exportador;
    }
}
