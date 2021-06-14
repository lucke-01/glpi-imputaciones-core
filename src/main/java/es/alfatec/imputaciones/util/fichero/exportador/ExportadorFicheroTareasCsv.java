package es.alfatec.imputaciones.util.fichero.exportador;

import es.alfatec.imputaciones.util.modelo.TareaListado;
import lombok.Data;

public class ExportadorFicheroTareasCsv extends ExportadorFicheroTareas {

    public ExportadorFicheroTareasCsv(TareaListado tareaListado,String rutaFichero) {
        super(tareaListado, rutaFichero);
    }
    
    @Override
    public void exportar() {
        exportaCsv();
    }
    
    public void exportaCsv() { 
        throw new UnsupportedOperationException("sin implementar");
    }
}
