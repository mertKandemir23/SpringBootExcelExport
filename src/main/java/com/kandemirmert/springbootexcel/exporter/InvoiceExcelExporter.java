package com.kandemirmert.springbootexcel.exporter;

import com.kandemirmert.springbootexcel.model.Invoice;
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

public class InvoiceExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CreationHelper createHelper;
    private CellStyle dateCellStyle;

    private List<Invoice> invoiceList;

    public InvoiceExcelExporter(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
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
        sheet = workbook.createSheet("Invoice");
        Row row2 = sheet.createRow(0);
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(25);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        Row row = sheet.createRow(1);
        createACell(row2, 0, "Invoice Info", cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        font.setFontHeight(12);
        createACell(row, 0, "Invoice ID", cellStyle);
        createACell(row, 1, "Invoice Name", cellStyle);
        createACell(row, 2, "Invoice Location", cellStyle);
        createACell(row, 3, "Invoice Amount", cellStyle);
        createACell(row, 4, "Invoice Due Date", cellStyle);
        createACell(row, 5, "Invoice CreateAt", cellStyle);

    }

    private void writeDataLines() {
        int rowCount = 2;
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(10);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        dateCellStyle.setAlignment(HorizontalAlignment.CENTER);
        for (Invoice invoice : invoiceList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createACell(row, columnCount++, invoice.getId(), cellStyle);
            createACell(row, columnCount++, invoice.getName(), cellStyle);
            createACell(row, columnCount++, invoice.getLocation(), cellStyle);
            createACell(row, columnCount++, invoice.getAmount(), cellStyle);
            createACell(row, columnCount++, invoice.getDueDate(), dateCellStyle);

            createACell(row, columnCount++, invoice.getCreatedAt(), dateCellStyle);

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
