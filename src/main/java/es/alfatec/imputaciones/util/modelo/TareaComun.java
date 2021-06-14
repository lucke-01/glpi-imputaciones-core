package es.alfatec.imputaciones.util.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TareaComun {
    protected LocalDate fechaTruncada;
    protected BigDecimal tiempoHoras;
    protected String errores = "";

    public TareaComun(LocalDate fechaTruncada, BigDecimal tiempoHoras) {
        this.fechaTruncada = fechaTruncada;
        this.tiempoHoras = tiempoHoras;
    }
    public TareaComun() {
        
    }
}
