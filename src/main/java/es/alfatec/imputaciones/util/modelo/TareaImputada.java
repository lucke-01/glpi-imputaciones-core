package es.alfatec.imputaciones.util.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Getter
@Setter
@ToString
public class TareaImputada extends TareaComun implements Comparable<TareaImputada> {

    //propiedades fichero
    private String entidad;
    private String tipoRegistro;
    private Integer idRegistro;
    private String tituloRegistro;
    private Integer idTarea;
    private String descripcionTarea;
    private LocalDateTime fecha;
    private Integer tiempoMinutos;
    private String estado;
    private String inicioPlan;
    private String finPlan;
    //calculados
    /*private LocalDate fechaTruncada;
    private BigDecimal tiempoHoras;
    private String errores;*/

    public TareaImputada() {

    }

    public TareaImputada(String entidad, String tipoRegistro, Integer idRegistro, String tituloRegistro, Integer idTarea, String descripcionTarea, LocalDateTime fecha, Integer tiempoMinutos, String estado, String inicioPlan, String finPlan) {
        this.entidad = entidad;
        this.tipoRegistro = tipoRegistro;
        this.idRegistro = idRegistro;
        this.tituloRegistro = tituloRegistro;
        this.idTarea = idTarea;
        this.descripcionTarea = descripcionTarea;
        this.fecha = fecha;
        this.tiempoMinutos = tiempoMinutos;
        this.estado = estado;
        this.inicioPlan = inicioPlan;
        this.finPlan = finPlan;
    }
    /**
     * Orden por fecha ASC
     * @param another
     * @return 
     */
    @Override
    public int compareTo(TareaImputada another) {
        return new CompareToBuilder()
                .append(this.fecha, another.fecha)
                .toComparison();
    }
}
