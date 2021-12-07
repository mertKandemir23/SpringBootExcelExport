package com.kandemirmert.springbootexcel.exporter;

import com.kandemirmert.springbootexcel.model.Fatura;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class FaturaPdfExporter {
    private List<Fatura> faturaList;


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
        for (Fatura fatura : faturaList) {
            table.addCell(String.valueOf(fatura.getId()));

            table.addCell(fatura.getName());
            table.addCell(fatura.getLocation());
            table.addCell(String.valueOf(fatura.getAmount()));
            table.addCell(fatura.getCreatedAt());
            table.addCell(fatura.getDueDate());

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
