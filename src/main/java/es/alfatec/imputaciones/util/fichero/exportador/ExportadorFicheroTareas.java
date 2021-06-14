package es.alfatec.imputaciones.util.fichero.exportador;

import es.alfatec.imputaciones.util.modelo.TareaListado;

public abstract class ExportadorFicheroTareas {
    protected TareaListado tareaListado;
    protected String rutaFichero;

    public ExportadorFicheroTareas(TareaListado tareaListado, String rutaFichero) {
        this.tareaListado = tareaListado;
        this.rutaFichero = rutaFichero;
    }
    public abstract void exportar();

    public TareaListado getTareaListado() {
        return tareaListado;
    }
    public void setTareaListado(TareaListado tareaListado) {
        this.tareaListado = tareaListado;
    }
    public String getRutaFichero() {
        return rutaFichero;
    }
    public void setRutaFichero(String rutaFichero) {
        this.rutaFichero = rutaFichero;
    }
}
