package es.alfatec.imputaciones.util.modelo;

import es.alfatec.imputaciones.util.core.Constantes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TareaListado {
    //obligatorio para de ese modo poder sacar mas informacion
    private LocalDate fechaInicio;
    //obligatorio para de ese modo poder sacar mas informacion
    private LocalDate fechaFin;
    
    private List<TareaImputada> listaTareas = new ArrayList<>();
    //TODO: si no se han imputado todas las horas rojo en excel
    private List<TareaImputadaAgrupada> listaTareasAgrupada = new ArrayList<>();
    /**
     * si tenemos establecido: fechaInicio y fechaFin (de momento obligatorios)
     * este campo dira los dias que faltan por imputar
     */
    private List<LocalDate> diasSinImputar = new ArrayList<>();
    /**
     * Total Numero de horas que deben sumar las imputaciones
     */
    private BigDecimal horasTotales = BigDecimal.ZERO;
    /**
     * Total Numero de horas imputadas
     */    
    private BigDecimal horasImputadas = BigDecimal.ZERO;
    
    private List<LocalDate> diasFestivos = new ArrayList<>();
    
    private List<LocalDate> diasVacaciones = new ArrayList<>();
    
    public String getEstadoTareas() {
        String estado = "";
        if (horasTotales.doubleValue() > horasImputadas.doubleValue()) {
            estado += "Faltan Horas por imputar;";
        }
        if (horasTotales.doubleValue() < horasImputadas.doubleValue()) {
            estado += "Demasiadas horas imputadas;";
        }
        if (!diasSinImputar.isEmpty()) {
            estado += "Faltan Dias por imputar;";
        }
        
        estado = estado.isEmpty() ? Constantes.ESTADO_TAREAS_OK : estado;
        return estado;
    }
    public boolean tieneEstadoCorrecto() {
        String estadoTareas = this.getEstadoTareas();
        return Constantes.ESTADO_TAREAS_OK.equals(estadoTareas);
    }
}
