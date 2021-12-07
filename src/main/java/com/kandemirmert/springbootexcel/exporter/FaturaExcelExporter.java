package com.kandemirmert.springbootexcel.exporter;

import com.kandemirmert.springbootexcel.model.Fatura;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class FaturaExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CreationHelper createHelper;
    private CellStyle dateCellStyle;

    private List<Fatura> faturaList;

    public FaturaExcelExporter(List<Fatura> faturaList) {
        this.faturaList = faturaList;
        workbook = new XSSFWorkbook();
        this.createHelper = workbook.getCreationHelper();
        dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
    }

    private void createACell(Row row, int columnCount, Object value, CellStyle cellStyle) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof LocalDateTime) {


            cell.setCellValue((LocalDateTime) value);

        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(cellStyle);
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Fatura");
        Row row2 = sheet.createRow(0);
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(25);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        Row row = sheet.createRow(1);
        createACell(row2, 0, "Fatura Bilgisi", cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        font.setFontHeight(12);
        createACell(row, 0, "Fatura ID", cellStyle);
        createACell(row, 1, "Fatura Name", cellStyle);
        createACell(row, 2, "Fatura Location", cellStyle);
        createACell(row, 3, "Fatura Amount", cellStyle);
        createACell(row, 4, "Fatura UpdateAt", cellStyle);
        createACell(row, 5, "Fatura CreateAt", cellStyle);

    }

    private void writeDataLines() {
        int rowCount = 2;
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(10);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        dateCellStyle.setAlignment(HorizontalAlignment.CENTER);
        for (Fatura fatura : faturaList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createACell(row, columnCount++, fatura.getId(), cellStyle);
            createACell(row, columnCount++, fatura.getName(), cellStyle);
            createACell(row, columnCount++, fatura.getLocation(), cellStyle);
            createACell(row, columnCount++, fatura.getAmount(), cellStyle);
            createACell(row, columnCount++, fatura.getDueDate(), dateCellStyle);

            createACell(row, columnCount++, fatura.getCreatedAt(), dateCellStyle);

        }
    }

    public void export(HttpServletResponse httpServletResponse) throws IOException {
        writeHeaderLine();
        writeDataLines();
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}
