package es.alfatec.imputaciones.util.modelo;

public enum FormatoExportacion {
    EXCEL("xlsx"),CSV("csv");
    private final String extension;
    private FormatoExportacion(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
