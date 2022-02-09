package es.alfatec.imputaciones.util.fichero.extractor;

import es.alfatec.imputaciones.util.modelo.Imputable;
import es.alfatec.imputaciones.util.modelo.TareaComun;
import es.alfatec.imputaciones.util.modelo.TareaImputada;
import es.alfatec.imputaciones.util.modelo.TareaImputadaAgrupada;
import es.alfatec.imputaciones.util.modelo.TareaListado;
import es.alfatec.imputaciones.util.utilidades.CadenaUtil;
import es.alfatec.imputaciones.util.utilidades.FechaUtil;
import es.alfatec.imputaciones.util.utilidades.FicheroUtil;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

@Data
public class ExtractorFicheroTareas {
    
    private String contenidoFicheroCsv;
        
    private Imputable imputable;

    public ExtractorFicheroTareas(String contenidoFicheroCsv, Imputable imputable) {
        this.contenidoFicheroCsv = contenidoFicheroCsv;
        this.imputable = imputable;
    }
    
    public TareaListado extraeTareaListado() {
        TareaListado tareaListado = new TareaListado();
        
        tareaListado.setDiasFestivos(imputable.getFestivos());
        //ordenamos dias festivos
        Collections.sort(tareaListado.getDiasFestivos());
        
        tareaListado.setDiasVacaciones(imputable.getVacaciones());
        //ordenamos dias vacaciones
        Collections.sort(tareaListado.getDiasVacaciones());
        
        List<TareaImputada> tareasImputadas =  extraeFicheroTareas();
        //ordenamos tareas por fecha
        Collections.sort(tareasImputadas);
        tareaListado.setListaTareas(tareasImputadas);
        
        List<TareaImputadaAgrupada> tareasAgrupadasByFecha = agrupaTareasByFecha(tareasImputadas);
        
        //ordenamos tareas agrupadas por fecha
        Collections.sort(tareasAgrupadasByFecha);
        tareaListado.setListaTareasAgrupada(tareasAgrupadasByFecha);

        //establecemos posibles errores
        estableceErroresTareaListado(tareaListado);
        estableceCalculadosTareaListado(tareaListado);
        
        return tareaListado;
    }
    public void estableceCalculadosTareaListado(TareaListado tareaListado) {
        //fechaInicio y fechaFin
        tareaListado.setFechaInicio(imputable.getFechaInicio());
        tareaListado.setFechaFin(imputable.getFechaFin());
        //total horas (dias imputables)
        for (LocalDate inicio = imputable.getFechaInicio() ; inicio.compareTo(imputable.getFechaFin()) <= 0 ; inicio = inicio.plusDays(1)) {
            DayOfWeek diaSemana = inicio.getDayOfWeek();
            if (diaSemana != DayOfWeek.SATURDAY && diaSemana != DayOfWeek.SUNDAY) {
                if (!imputable.getFestivos().contains(inicio) && !imputable.getVacaciones().contains(inicio)) {
                    tareaListado.setHorasTotales(tareaListado.getHorasTotales().add(new BigDecimal(imputable.getNumeroHorasDia())));
                }
            }
        }
        //total horas imputadas
        for (int index = 0; index < tareaListado.getListaTareas().size(); index++) {
            TareaImputada tareaImputada = tareaListado.getListaTareas().get(index);
            tareaListado.setHorasImputadas(tareaListado.getHorasImputadas().add(tareaImputada.getTiempoHoras()));
        }
        //dias sin imputar
        List<LocalDate> diasImputados = tareaListado.getListaTareas().stream()
                .filter(t->t.getTiempoMinutos() > 0)
                .map(TareaImputada::getFechaTruncada)
                .collect(Collectors.toList());
        Set<LocalDate> diasSinImputar = new HashSet<>();
        for (int index = 0; index < tareaListado.getListaTareas().size(); index++) {
            for (LocalDate inicio = imputable.getFechaInicio() ; inicio.compareTo(imputable.getFechaFin()) <= 0 ; inicio = inicio.plusDays(1)) {
                DayOfWeek diaSemana = inicio.getDayOfWeek();
                //si es un dia imputable y no esta imputado se agrega a dias sin imputar
                if (diaSemana != DayOfWeek.SATURDAY && diaSemana != DayOfWeek.SUNDAY) {
                    if (!imputable.getFestivos().contains(inicio) && !imputable.getVacaciones().contains(inicio)) {
                        if (!diasImputados.contains(inicio)) {
                            diasSinImputar.add(inicio);
                        }
                    }
                }
            }
        }
        tareaListado.setDiasSinImputar(new ArrayList<>(diasSinImputar));
        //ordenado dias sin imputar
        Collections.sort(tareaListado.getDiasSinImputar());
    }
    public void estableceErroresTareaListado(TareaListado tareaListado) {
        //establecemos posibles errores en tareasImputadas
        for (int index = 0; index < tareaListado.getListaTareas().size(); index++) {
            TareaImputada tareaImputada = tareaListado.getListaTareas().get(index);
            
            estableceErroresTareaComun(tareaImputada);
        }
        //establecemos posibles errores en tareasAgrupadas
        for (int index = 0; index < tareaListado.getListaTareasAgrupada().size(); index++) {
            TareaImputadaAgrupada tareaImputadaAgrupada = tareaListado.getListaTareasAgrupada().get(index);
            
            estableceErroresTareaComun(tareaImputadaAgrupada);
        }
    }
    public void estableceErroresTareaComun(TareaComun tareaComun) {
        //solo comprobamos menos de 8 horas en las tareas agrupadas
        if (tareaComun instanceof TareaImputadaAgrupada) {
            if (tareaComun.getTiempoHoras().doubleValue() < imputable.getNumeroHorasDia()) {
                tareaComun.setErrores(tareaComun.getErrores()+"Menos de 8 horas imputadas,");
            }
        }
        //imputacion de tareas con solo 0 horas
        if (tareaComun instanceof TareaImputada) {
            if (BigDecimal.ZERO.equals(tareaComun.getTiempoHoras())) {
                tareaComun.setErrores(tareaComun.getErrores()+"Taraea con Imputacion de CERO horas,");
            }
        }
        if (tareaComun.getTiempoHoras().doubleValue() > imputable.getNumeroHorasDia()) {
            tareaComun.setErrores(tareaComun.getErrores()+"Mas de 8 horas imputadas,");
        }
        DayOfWeek diaSemana = tareaComun.getFechaTruncada().getDayOfWeek();
        //imputacion en dias ilegales
        if (tareaComun.getTiempoHoras().doubleValue() > 0) {
            //findes
            if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
                tareaComun.setErrores(tareaComun.getErrores()+"FIN DE SEMANA,");
            }
            //festivos
            if (imputable.getFestivos().contains(tareaComun.getFechaTruncada())) {
                tareaComun.setErrores(tareaComun.getErrores()+"Festivo,");
            }
            //vacaciones
            if (imputable.getVacaciones().contains(tareaComun.getFechaTruncada())) {
                tareaComun.setErrores(tareaComun.getErrores()+"Vacaciones,");
            }
        }
        //limpieza ultima coma
        tareaComun.setErrores(this.normalizaCadenaErrores(tareaComun.getErrores()));
    }
    public String normalizaCadenaErrores(String cadenaErrores) {
        String cadenaErroresNormalizada = CadenaUtil.removeLastChar(cadenaErrores);
        if (cadenaErroresNormalizada != null && !cadenaErroresNormalizada.isEmpty()) {
            cadenaErroresNormalizada += ".";
        }
        return cadenaErroresNormalizada;
    }
    public List<TareaImputadaAgrupada> agrupaTareasByFecha(List<TareaImputada> tareasImputadas) {
        final List<TareaImputadaAgrupada> listaTareasAgrupada = new ArrayList<>();
        
        //agrupar tareas en mapa
        Map<LocalDate, BigDecimal> horasByFechaTruncada = tareasImputadas.stream()
            .collect(Collectors.groupingBy(
                    TareaImputada::getFechaTruncada,
                    //Collectors.summarizingDouble(TareaImputadaAgrupada::getTiempoHoras)
                    Collectors.reducing(BigDecimal.ZERO, TareaImputada::getTiempoHoras, BigDecimal::add))
        );
        for (Entry<LocalDate, BigDecimal> entry : horasByFechaTruncada.entrySet()) {
            TareaImputadaAgrupada tareaAgrupada = new TareaImputadaAgrupada(entry.getKey(),entry.getValue());
            listaTareasAgrupada.add(tareaAgrupada);
        }
        
        return listaTareasAgrupada;
    }
    
    public List<TareaImputada> extraeFicheroTareas() {
        List<TareaImputada> listaTareas = new ArrayList<>();
        String contenidoCsvSinHeader = FicheroUtil.deleteFirtLine(contenidoFicheroCsv);
        try {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withDelimiter(';')
                    .withRecordSeparator('\n')
                    .withSkipHeaderRecord()
                    .withTrim()
                    .withIgnoreEmptyLines()
                    .parse(new StringReader(contenidoCsvSinHeader));
            
            listaTareas = coleccionaFicheroTareas(records);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return listaTareas;
    }
    public List<TareaImputada> coleccionaFicheroTareas(Iterable<CSVRecord> listaRecord) {
        List<TareaImputada> listaTareaImputada = new ArrayList<>();
        for (CSVRecord record : listaRecord) {
            TareaImputada tarea = construyeTareaImputadaFromRecord(record);
            listaTareaImputada.add(tarea);
        }
        return listaTareaImputada;
    } 
    public TareaImputada construyeTareaImputadaFromRecord(CSVRecord record) {
        
        TareaImputada tareaImputada = new TareaImputada(
            record.get(HeaderTareas.ENTIDAD.ordinal()),record.get(HeaderTareas.TIPO_REGISTRO.ordinal()),
            Integer.valueOf(record.get(HeaderTareas.ID_REGISTRO.ordinal())),record.get(HeaderTareas.TITULO_REGISTRO.ordinal()),
            Integer.valueOf(record.get(HeaderTareas.ID_TAREA.ordinal())),record.get(HeaderTareas.DESCRIPCION_TAREA.ordinal()),
            FechaUtil.cadenaToLocalDateTime(record.get(HeaderTareas.FECHA.ordinal())),Integer.valueOf(record.get(HeaderTareas.TIEMPO_MINUTOS.ordinal())),
            record.get(HeaderTareas.ESTADO.ordinal()),record.get(HeaderTareas.INICIO_PLAN.ordinal()),
            record.get(HeaderTareas.FIN_PLAN.ordinal())
        );
        tareaImputada.setTiempoHoras(new BigDecimal(tareaImputada.getTiempoMinutos()).divide(new BigDecimal(60)));
        tareaImputada.setFechaTruncada(tareaImputada.getFecha().toLocalDate());
        
        return tareaImputada;
    }
    
}
enum HeaderTareas {
    ENTIDAD, TIPO_REGISTRO, ID_REGISTRO, TITULO_REGISTRO,
    ID_TAREA,DESCRIPCION_TAREA,FECHA,TIEMPO_MINUTOS,
    ESTADO,INICIO_PLAN,FIN_PLAN;
}