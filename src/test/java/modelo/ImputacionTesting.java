package modelo;

import es.alfatec.imputaciones.util.modelo.FormatoExportacion;
import es.alfatec.imputaciones.util.modelo.Imputable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@Data
@Getter
@Setter
@ToString
public class ImputacionTesting implements Imputable{

    private boolean activo = true;

    private boolean formatoMinificado = false;
    
    private String nombre;

    private String ficheroImputaciones;

    private String ficheroSalida;
    
    private FormatoExportacion formatoSalida = FormatoExportacion.EXCEL;
    
    private LocalDate fechaInicio;
    
    private LocalDate fechaFin;
    
    private List<LocalDate> vacaciones = new ArrayList<>();
    private List<LocalDate> festivos = new ArrayList<>();

    public ImputacionTesting() {
        
    }
    public ImputacionTesting(LocalDate fechaInicio, LocalDate fechaFin,List<LocalDate> vacaciones, List<LocalDate> festivos) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.vacaciones = vacaciones;
        this.festivos = festivos;
    }
     public ImputacionTesting(LocalDate fechaInicio, LocalDate fechaFin,List<LocalDate> vacaciones) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.vacaciones = vacaciones;
    }
    public ImputacionTesting(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
}
