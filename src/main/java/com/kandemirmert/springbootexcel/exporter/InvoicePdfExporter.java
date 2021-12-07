package com.kandemirmert.springbootexcel.exporter;

import com.kandemirmert.springbootexcel.model.Invoice;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class InvoicePdfExporter {
    private List<Invoice> invoiceList;


    private void writeTableHeader(PdfPTable pdfTable) {
        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(Color.GRAY);
        cell.setPadding(6);

        Font font = FontFactory.getFont(FontFactory.TIMES_ITALIC);
        font.setColor(Color.BLUE);

        cell.setPhrase(new Phrase("FATURA ID", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell);

        cell.setPhrase(new Phrase("FATURA ADI", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell);

        cell.setPhrase(new Phrase("FATURA KONUMU", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell);

        cell.setPhrase(new Phrase("FATURA BEDELI", font));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell);

        cell.setPhrase(new Phrase("FATURA OLUSTURULMA TARIHI", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell);

        cell.setPhrase(new Phrase("SON ODEME TARIHI", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell);

    }

    private void writeTableData(PdfPTable table) {
        for (Invoice invoice : invoiceList) {
            table.addCell(String.valueOf(invoice.getId()));

            table.addCell(invoice.getName());
            table.addCell(invoice.getLocation());
            table.addCell(String.valueOf(invoice.getAmount()));
            table.addCell(invoice.getCreatedAt());
            table.addCell(invoice.getDueDate());

        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);

        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("Tüm Faturalar", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2f, 2.8f, 2.2f, 2f, 4f, 4f});
        table.setSpacingBefore(12);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }

}
