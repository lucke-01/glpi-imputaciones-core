package es.alfatec.imputaciones.util.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.ToString;
import org.apache.commons.lang3.builder.CompareToBuilder;

@ToString
public class TareaImputadaAgrupada extends TareaComun implements Comparable<TareaImputadaAgrupada> {

    public TareaImputadaAgrupada() {
        
    }
    public TareaImputadaAgrupada(LocalDate fechaTruncada, BigDecimal tiempoHoras) {
        this.fechaTruncada = fechaTruncada;
        this.tiempoHoras = tiempoHoras;
    }
    
    /**
     * Orden por fechaTruncada ASC
     * @param another
     * @return 
     */
    @Override
    public int compareTo(TareaImputadaAgrupada another) {
        return new CompareToBuilder()
                .append(this.fechaTruncada, another.fechaTruncada)
                .toComparison();
    }
}
