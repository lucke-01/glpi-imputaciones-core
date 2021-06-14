package es.alfatec.imputaciones.util.fichero.exportador;

import es.alfatec.imputaciones.util.core.Constantes;
import es.alfatec.imputaciones.util.modelo.TareaImputada;
import es.alfatec.imputaciones.util.modelo.TareaImputadaAgrupada;
import es.alfatec.imputaciones.util.modelo.TareaListado;
import es.alfatec.imputaciones.util.utilidades.ExcelUtil;
import es.alfatec.imputaciones.util.utilidades.FechaUtil;
import java.time.LocalDate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportadorFicheroTareasExcel extends ExportadorFicheroTareas {
    private final static int INICIO_ROW_IMPUTACIONES = 6;
    private final static int INICIO_COLUMN_IMPUTACIONES_AGRUPADAS = 13;
    
    private int ultimaFilaResumen = 0;
    
    public ExportadorFicheroTareasExcel(TareaListado tareaListado,String rutaFichero) {
        super(tareaListado, rutaFichero);
    }
    
    @Override
    public void exportar() {
        exportaExcel();
    }
    public void exportaExcel() {
        //Workbook workbook = new XSSFWorkbook();
        Workbook wb = new XSSFWorkbook();
        
        Sheet sheet = wb.createSheet("Imputaciones");
        CreationHelper createHelper = wb.getCreationHelper();
        
        pintaHeaderExcel(wb, sheet);
        pintaImputacionesExcel(wb, sheet);
        pintaAgrupacionExcel(wb, sheet);
        pintaResumenExcel(wb, sheet);
        
        //resize todas columnas menos las grandes
        ExcelUtil.autoSizeAllColumn(sheet,100,new int[]{3,11,16});
        //tamanio de columnas personalizado
        sheet.setColumnWidth(3, 11 * 256);
        sheet.setColumnWidth(16, 12 * 256);
        
        //exportar fichero
        ExcelUtil.exportarWorkbook(wb, this.rutaFichero);
    }
    public void pintaHeaderExcel(Workbook wb,Sheet sheet) {
        Row headerRow1 = sheet.createRow(0);
        ExcelUtil.cellFontBold(wb,headerRow1.createCell(0)).setCellValue("Imputaciones");
        
        Row headerRow2 = sheet.createRow(1);
        ExcelUtil.cellFontBold(wb,headerRow2.createCell(0)).setCellValue("Fecha inicio: ");
        ExcelUtil.addCenterAlign(ExcelUtil.cellStyleDate(wb, headerRow2.createCell(1), FechaUtil.DEFAULT_CAD_LOCALDATE_FORMAT)).setCellValue(tareaListado.getFechaInicio());
        ExcelUtil.cellFontBold(wb,headerRow2.createCell(2)).setCellValue("Fecha Fin: ");
        ExcelUtil.addCenterAlign(ExcelUtil.cellStyleDate(wb, headerRow2.createCell(3), FechaUtil.DEFAULT_CAD_LOCALDATE_FORMAT)).setCellValue(tareaListado.getFechaFin());
        
        Row headerRow3 = sheet.createRow(2);
        /*Cell celdaHorasImputar = headerRow3.createCell(0);
        celdaHorasImputar.setCellValue("Horas Imputar: ");
        celdaHorasImputar.setCellStyle(ExcelUtil.getCellStyleFontBold(wb));*/
        ExcelUtil.cellFontBold(wb, headerRow3.createCell(0)).setCellValue("Horas Imputar: ");
        ExcelUtil.addCenterAlign(ExcelUtil.createEmptyCellStyle(wb,headerRow3.createCell(1))).setCellValue(tareaListado.getHorasTotales().doubleValue());
        //celdaHorasTotales.setCellStyle(wb.createCellStyle());
        //celdaHorasTotales.setCellValue(tareaListado.getHorasTotales().doubleValue());
        ExcelUtil.cellFontBold(wb,headerRow3.createCell(2)).setCellValue("Horas Imputadas: ");
        Cell celdaHorasImputadas = headerRow3.createCell(3);
        celdaHorasImputadas.setCellValue(tareaListado.getHorasImputadas().doubleValue());
        //comprobar horas imputadas con horasTotales
        celdaHorasImputadas.setCellStyle(wb.createCellStyle());
        CellStyle estiloRojo = ExcelUtil.getCellStyleColorRed(wb);
        if (tareaListado.getHorasImputadas().doubleValue() < tareaListado.getHorasTotales().doubleValue()) {
            celdaHorasImputadas.setCellStyle(estiloRojo);
            ExcelUtil.addComment(wb, celdaHorasImputadas, "Necesita imputar mas horas.");
        } else if (tareaListado.getHorasImputadas().doubleValue() > tareaListado.getHorasTotales().doubleValue()) {
            celdaHorasImputadas.setCellStyle(estiloRojo);
            ExcelUtil.addComment(wb, celdaHorasImputadas, "Hay mas horas imputadas que el total de horas.");
        } else {
            CellStyle estiloVerde = ExcelUtil.getCellStyleColorVerde(wb);
            celdaHorasImputadas.setCellStyle(estiloVerde);
        }
        ExcelUtil.addCenterAlign(celdaHorasImputadas);
        
        Row headerRow4 = sheet.createRow(3);
        ExcelUtil.cellFontBold(wb,headerRow4.createCell(0)).setCellValue("ESTADO IMPUTACIONES: ");
        Cell celdaEstadoTareas = headerRow4.createCell(1);
        if (!this.tareaListado.tieneEstadoCorrecto()) {
            CellStyle estiloRojoTareas = ExcelUtil.getCellStyleColorRed(wb);
            celdaEstadoTareas.setCellStyle(estiloRojoTareas);
            ExcelUtil.addComment(wb, celdaEstadoTareas, this.tareaListado.getEstadoTareas());
            celdaEstadoTareas.setCellValue(Constantes.ESTADO_TAREAS_MAL);
        } else {
            CellStyle estiloVerdeTareas = ExcelUtil.getCellStyleColorVerde(wb);
            celdaEstadoTareas.setCellStyle(estiloVerdeTareas);
            celdaEstadoTareas.setCellValue(Constantes.ESTADO_TAREAS_OK);
        }
        ExcelUtil.addCenterAlign(celdaEstadoTareas);
    }
    public void pintaImputacionesExcel(Workbook wb,Sheet sheet) {
        Row TituloImputacionesRow = ExcelUtil.getOrCreateRow(sheet, INICIO_ROW_IMPUTACIONES-1);
        ExcelUtil.cellFontBold(wb,TituloImputacionesRow.createCell(0)).setCellValue("Lista Imputaciones");
        Row headerImputacionesRow = ExcelUtil.getOrCreateRow(sheet, INICIO_ROW_IMPUTACIONES);
        ExcelUtil.cellFontBold(wb,headerImputacionesRow.createCell(0)).setCellValue("entidad");
        ExcelUtil.cellFontBold(wb,headerImputacionesRow.createCell(1)).setCellValue("tipoRegistro");
        ExcelUtil.cellFontBold(wb,headerImputacionesRow.createCell(2)).setCellValue("idRegistro");
        ExcelUtil.cellFontBold(wb,headerImputacionesRow.createCell(3)).setCellValue("tituloRegistro");
        ExcelUtil.cellFontBold(wb,headerImputacionesRow.createCell(4)).setCellValue("idTarea");
        ExcelUtil.cellFontBold(wb,headerImputacionesRow.createCell(5)).setCellValue("descripcionTarea");
        ExcelUtil.cellFontBold(wb,headerImputacionesRow.createCell(6)).setCellValue("fecha Completa");
        ExcelUtil.cellFontBold(wb,headerImputacionesRow.createCell(7)).setCellValue("tiempoMinutos");
        ExcelUtil.cellFontBold(wb,headerImputacionesRow.createCell(8)).setCellValue("estado");
        //headerImputacionesRow.createCell(9).setCellValue("inicioPlan");
        //headerImputacionesRow.createCell(10).setCellValue("finPlan");
        ExcelUtil.cellFontBold(wb,headerImputacionesRow.createCell(9)).setCellValue("Tiempo Horas");
        ExcelUtil.cellFontBold(wb,headerImputacionesRow.createCell(10)).setCellValue("Fecha Dia");
        ExcelUtil.cellFontBold(wb,headerImputacionesRow.createCell(11)).setCellValue("Errores");

        for (int indice = 0; indice < tareaListado.getListaTareas().size(); indice++) {
            int indiceFila = (INICIO_ROW_IMPUTACIONES+1)+indice;
            TareaImputada tarea = tareaListado.getListaTareas().get(indice);
            
            Row filaImputacion = ExcelUtil.getOrCreateRow(sheet, indiceFila);
            filaImputacion.createCell(0).setCellValue(tarea.getEntidad());
            filaImputacion.createCell(1).setCellValue(tarea.getTipoRegistro());
            ExcelUtil.addCenterAlign(ExcelUtil.createEmptyCellStyle(wb,filaImputacion.createCell(2))).setCellValue(tarea.getIdRegistro());
            filaImputacion.createCell(3).setCellValue(tarea.getTituloRegistro());
            ExcelUtil.addCenterAlign(ExcelUtil.createEmptyCellStyle(wb,filaImputacion.createCell(4))).setCellValue(tarea.getIdTarea());
            filaImputacion.createCell(5).setCellValue(tarea.getDescripcionTarea());
            ExcelUtil.addCenterAlign(ExcelUtil.cellStyleDate(wb, filaImputacion.createCell(6), FechaUtil.DEFAULT_CAD_LOCALDATE_FORMAT)).setCellValue(tarea.getFecha());            
            ExcelUtil.addCenterAlign(ExcelUtil.createEmptyCellStyle(wb,filaImputacion.createCell(7))).setCellValue(tarea.getTiempoMinutos());
            filaImputacion.createCell(8).setCellValue(tarea.getEstado());
            //filaImputacion.createCell(9).setCellValue(tarea.getInicioPlan());
            //filaImputacion.createCell(10).setCellValue(tarea.getFinPlan());
            //filaImputacion.createCell(9).setCellValue(tarea.getTiempoHoras().toString());
            ExcelUtil.addCenterAlign(ExcelUtil.createEmptyCellStyle(wb,filaImputacion.createCell(9))).setCellValue(tarea.getTiempoHoras().doubleValue());
            ExcelUtil.addCenterAlign(ExcelUtil.cellStyleDate(wb, filaImputacion.createCell(10), FechaUtil.DEFAULT_CAD_LOCALDATE_FORMAT)).setCellValue(tarea.getFechaTruncada());            
            filaImputacion.createCell(11).setCellValue(tarea.getErrores());
        }
    }
    public void pintaAgrupacionExcel(Workbook wb,Sheet sheet) {
        Row TituloImputacionesGRow = ExcelUtil.getOrCreateRow(sheet, INICIO_ROW_IMPUTACIONES-1);
        ExcelUtil.cellFontBold(wb,TituloImputacionesGRow.createCell(1+INICIO_COLUMN_IMPUTACIONES_AGRUPADAS)).setCellValue("Agrupacion");
        
        Row headerImputacionesGRow = ExcelUtil.getOrCreateRow(sheet, INICIO_ROW_IMPUTACIONES);
        
        ExcelUtil.cellFontBold(wb,headerImputacionesGRow.createCell(1+INICIO_COLUMN_IMPUTACIONES_AGRUPADAS)).setCellValue("Fecha Dia");
        ExcelUtil.cellFontBold(wb,headerImputacionesGRow.createCell(2+INICIO_COLUMN_IMPUTACIONES_AGRUPADAS)).setCellValue("Tiempo Horas");
        ExcelUtil.cellFontBold(wb,headerImputacionesGRow.createCell(3+INICIO_COLUMN_IMPUTACIONES_AGRUPADAS)).setCellValue("Errores");
        
        for (int indice = 0; indice < tareaListado.getListaTareasAgrupada().size(); indice++) {
            int indiceFila = (INICIO_ROW_IMPUTACIONES+1)+indice;
            TareaImputadaAgrupada tarea = tareaListado.getListaTareasAgrupada().get(indice);
            
            //Row filaImputacion = sheet.getRow(indiceFila);
            Row filaImputacion = ExcelUtil.getOrCreateRow(sheet, indiceFila);
            ExcelUtil.addCenterAlign(ExcelUtil.cellStyleDate(wb, filaImputacion.createCell(1+INICIO_COLUMN_IMPUTACIONES_AGRUPADAS), FechaUtil.DEFAULT_CAD_LOCALDATE_FORMAT)).setCellValue(tarea.getFechaTruncada());
            
            Cell celdaHoras = filaImputacion.createCell(2+INICIO_COLUMN_IMPUTACIONES_AGRUPADAS);
            //celdaHoras.setCellValue(tarea.getTiempoHoras().toString());
            celdaHoras.setCellValue(tarea.getTiempoHoras().doubleValue());
            celdaHoras.setCellStyle(ExcelUtil.getCellStyleCenterText(wb));
            
            filaImputacion.createCell(3+INICIO_COLUMN_IMPUTACIONES_AGRUPADAS).setCellValue(tarea.getErrores());
            this.ultimaFilaResumen = indiceFila;
        }
    }
    public void pintaResumenExcel(Workbook wb,Sheet sheet) {
        //TODO: resumen con festivos, vacaciones, dias Sin imputar
        int filasResumenActual = ultimaFilaResumen+2;
        int inicioColumnaResumenActual = 1+INICIO_COLUMN_IMPUTACIONES_AGRUPADAS;
        
        Row resumenTituloRow = ExcelUtil.getOrCreateRow(sheet, filasResumenActual);
        ExcelUtil.cellFontBold(wb,resumenTituloRow.createCell(inicioColumnaResumenActual)).setCellValue("Resumen");
        filasResumenActual++;
        
        
        Row encabezadoTablaResumen = ExcelUtil.getOrCreateRow(sheet, filasResumenActual);
        //filasResumenActual++;
        //dias Restantes
        ExcelUtil.cellFontBold(wb,encabezadoTablaResumen.createCell(inicioColumnaResumenActual)).setCellValue("Dias Sin Imputar");
        for (int indice = 0,indiceFilaActual = encabezadoTablaResumen.getRowNum()+1; indice < tareaListado.getDiasSinImputar().size(); indice++) {
            LocalDate diaSinImputar = tareaListado.getDiasSinImputar().get(indice);
            
            Row filaDiaSinImputar = ExcelUtil.getOrCreateRow(sheet,indiceFilaActual);
            ExcelUtil.addCenterAlign(ExcelUtil.cellStyleDate(wb, filaDiaSinImputar.createCell(inicioColumnaResumenActual), FechaUtil.DEFAULT_CAD_LOCALDATE_FORMAT)).setCellValue(diaSinImputar);            
            indiceFilaActual++;
        }
        //festivos
        ExcelUtil.cellFontBold(wb,encabezadoTablaResumen.createCell(inicioColumnaResumenActual+1)).setCellValue("Festivos");
        for (int indice = 0,indiceFilaActual = encabezadoTablaResumen.getRowNum()+1; indice < tareaListado.getDiasFestivos().size(); indice++) {
            LocalDate dia = tareaListado.getDiasFestivos().get(indice);
            
            Row filaDia = ExcelUtil.getOrCreateRow(sheet,indiceFilaActual);
            ExcelUtil.addCenterAlign(ExcelUtil.cellStyleDate(wb, filaDia.createCell(inicioColumnaResumenActual+1), FechaUtil.DEFAULT_CAD_LOCALDATE_FORMAT)).setCellValue(dia);            
            indiceFilaActual++;
        }
        ExcelUtil.cellFontBold(wb,encabezadoTablaResumen.createCell(inicioColumnaResumenActual+2)).setCellValue("Vacaciones");
        for (int indice = 0,indiceFilaActual = encabezadoTablaResumen.getRowNum()+1; indice < tareaListado.getDiasVacaciones().size(); indice++) {
            LocalDate dia = tareaListado.getDiasVacaciones().get(indice);
            
            Row filaDia = ExcelUtil.getOrCreateRow(sheet,indiceFilaActual);
            ExcelUtil.addCenterAlign(ExcelUtil.cellStyleDate(wb, filaDia.createCell(inicioColumnaResumenActual+2), FechaUtil.DEFAULT_CAD_LOCALDATE_FORMAT)).setCellValue(dia);            
            indiceFilaActual++;
        }
    }
}
