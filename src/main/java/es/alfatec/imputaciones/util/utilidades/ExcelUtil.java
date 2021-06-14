/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.alfatec.imputaciones.util.utilidades;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.bouncycastle.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ricardo
 */
public class ExcelUtil {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    public static void exportarWorkbook(Workbook workbook, String ruta) {
        //guardar en fichero workbook
        try (FileOutputStream fileOut = new FileOutputStream(ruta)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //cerrar workbook
            try {
                workbook.close();
            } catch (IOException ex) {
                log.error("", ex);
            }
        }

    }

    public static Cell cellStyleDate(Workbook wb, Cell cell, String formatoFecha) {
        CellStyle cellStyle = wb.createCellStyle();
        CreationHelper createHelper = wb.getCreationHelper();
        cellStyle.setDataFormat(
                createHelper.createDataFormat().getFormat(formatoFecha));
        cell.setCellStyle(cellStyle);
        return cell;
    }
    public static Cell cellFontBold(Workbook wb, Cell cell) {
        CellStyle style =  getCellStyleFontBold(wb);
        cell.setCellStyle(style);
        return cell;
    }
    public static CellStyle getCellStyleCenterText(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        
        return cellStyle;
    }
    public static CellStyle getCellStyleFontBold(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }
    public static CellStyle getCellStyleColorRed(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        Font font = wb.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillBackgroundColor(IndexedColors.RED.getIndex());
        cellStyle.setFillPattern(FillPatternType.BIG_SPOTS);  
        cellStyle.setFont(font);
        return cellStyle;
    }
    public static CellStyle getCellStyleColorVerde(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        Font font = wb.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
        cellStyle.setFillPattern(FillPatternType.BIG_SPOTS);  
        cellStyle.setFont(font);
        return cellStyle;
    }
    public static void autoSizeAllColumn(Sheet sheet, int numeroMaximoColumnas,int[] noResizeColumns) {
        for (int index = 0; index < numeroMaximoColumnas; index++) {
            if (!Arrays.contains(noResizeColumns, index)) {
                sheet.autoSizeColumn(index);
            }
        }
    }

    public static Row getOrCreateRow(Sheet sheet,int indice) {
        Row row = sheet.getRow(indice);
        if (row == null) {
            row = sheet.createRow(indice);
        }
        return row;
    }
    
    public static void addComment(Workbook workbook, Cell cell, String commentText) {
        CreationHelper factory = workbook.getCreationHelper();

        ClientAnchor anchor = factory.createClientAnchor();
        //i found it useful to show the comment box at the bottom right corner
        anchor.setCol1(cell.getColumnIndex() + 1); //the box of the comment starts at this given column...
        anchor.setCol2(cell.getColumnIndex() + 3); //...and ends at that given column
        anchor.setRow1(cell.getRowIndex() + 1); //one row below the cell...
        anchor.setRow2(cell.getRowIndex() + 5); //...and 4 rows high
        
        Drawing drawing = cell.getSheet().createDrawingPatriarch();
        Comment comment = drawing.createCellComment(anchor);
        //set the comment text and author
        comment.setString(factory.createRichTextString(commentText));
        //comment.setAuthor(author);

        cell.setCellComment(comment);
    }
    public static Cell createEmptyCellStyle(Workbook wb, Cell cell) {
        CellStyle cellStyle = wb.createCellStyle();
        cell.setCellStyle(cellStyle);
        return cell;
    }
    public static Cell addCenterAlign(Cell cell) {
        CellStyle cellStyle = cell.getCellStyle();
        
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
 
        return cell;
    }
}
