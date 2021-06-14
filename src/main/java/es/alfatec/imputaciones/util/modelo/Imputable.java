package es.alfatec.imputaciones.util.modelo;

import java.time.LocalDate;
import java.util.List;

import es.alfatec.imputaciones.util.core.Constantes;

public interface Imputable {
    boolean isActivo();

    boolean isFormatoMinificado();
    
    default int getNumeroHorasDia() {
    	return Constantes.DEFAULT_HORAS_DIA;
    }
    
    String getNombre();
    
    String getFicheroImputaciones();

    String getFicheroSalida();
    
    FormatoExportacion getFormatoSalida();
    
    LocalDate getFechaInicio();
    
    LocalDate getFechaFin();
    
    List<LocalDate> getVacaciones();
    
    List<LocalDate> getFestivos();   
}
