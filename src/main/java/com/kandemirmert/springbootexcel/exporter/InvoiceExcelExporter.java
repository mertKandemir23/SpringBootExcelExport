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
    private XSSFWorkbook workbook; //Bir excel sheeti oluşturmak için workbook nesnesinin oluşturulması gerekir.

    private XSSFSheet sheet; // Excel sheet referansı

    private CreationHelper createHelper; // HSSF ve XSSF için ihtiyaç duyulan çeşitli nesnelerin  somut sınıflarını somutlaştıran bir nesne

    private CellStyle dateCellStyle; //Hücrelerle ilgili işlemler için kullanılıyor.

    private List<Invoice> invoiceList; // Model sınıfımızın türünden list

    public InvoiceExcelExporter(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
        workbook = new XSSFWorkbook();
        this.createHelper = workbook.getCreationHelper();
        dateCellStyle = workbook.createCellStyle(); //Bir hücre şekillendirme instance ı ve bu classın instanceına ataadık.yarattık.
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm")); //Tarihi düzenledik.
    }

    private void createACell(Row row, int columnCount, Object value, CellStyle cellStyle) { //Bir hücre yaratma metodu.
        sheet.autoSizeColumn(columnCount); //İçeriği sığdırmak için sütun genişliğini otomatik olarak ayarlar. Sadece bir kez kullanılmalıdır.

        Cell cell = row.createCell(columnCount); //parametrede gelen sayı kadar hücre yarat.

        if (value instanceof Long) { //object tipinden gelen nesnenin tip kontrolünü yapıyoruz.Ona göre nesneyi yazıyoruz.
            cell.setCellValue((Long) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) value);}
        else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(cellStyle); //Hücre stilini geçiyoruz.
    }

    private void writeHeaderLine() { //Başlık yazıyoruz.
        sheet = workbook.createSheet("Invoice"); //Tablo adı verdik.
        Row row2 = sheet.createRow(0); //0.Satırı oluşturduk.
        CellStyle cellStyle = workbook.createCellStyle(); //Yeni bir  hücre tipi oluşturduk zira
        XSSFFont font = workbook.createFont(); //Bir yazı tipi oluşturuyoruz.
        font.setBold(true); // Bold tipi olacağını söylüyoruz. //Kalın
        font.setFontHeight(25); //Yazı yüksekliğini belirliyoruz.
        cellStyle.setFont(font); //Hücre tipine gelecek yazının fontunu söyledik.
        cellStyle.setAlignment(HorizontalAlignment.CENTER); //Yazının hücreyi orlataycağını söyledik.

        Row row = sheet.createRow(1); //Yeni bir satır oluşturduk
        createACell(row2, 0, "Invoice Info", cellStyle); //0.satıra değer verdik oluşturdğumuz cellStyleda
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5)); //0.satır boyunca toplam 6 sütün oluşsun dedik.
        font.setFontHeight(12); //Font büyüklüğünü ayarladık
        createACell(row, 0, "Invoice ID", cellStyle); //Kolon numarası, içine yazılaacak bilgi,ve hücre tipi
        createACell(row, 1, "Invoice Name", cellStyle);
        createACell(row, 2, "Invoice Location", cellStyle);
        createACell(row, 3, "Invoice Amount", cellStyle);
        createACell(row, 4, "Invoice Due Date", cellStyle);
        createACell(row, 5, "Invoice CreateAt", cellStyle);

    }


    private void writeDataLines() { //Verileri yazıyoruz.
        int rowCount = 2;
        CellStyle cellStyle = workbook.createCellStyle(); //Yeni bir cellstyle
        XSSFFont font = workbook.createFont(); //Yeni bir font
        font.setFontHeight(10); //Font yüksekliği
        cellStyle.setFont(font); //Hücreye yazılacak yazının fontu
        cellStyle.setAlignment(HorizontalAlignment.CENTER); //Her hücreyi ortalaayacağız.
        dateCellStyle.setAlignment(HorizontalAlignment.CENTER); //Dateyi ayrı tanımlamıştık formatlamıştık bunu ayrı yapıyruz o yüzden
        for (Invoice invoice : invoiceList) { //InvoiceListi çağır ve her elemanı te tek gez.
            Row row = sheet.createRow(rowCount++); //2.rowdan başla ve sonra arttır.
            int columnCount = 0; //0.Sütün
            createACell(row, columnCount++, invoice.getId(), cellStyle); //2.rowa yaz. sonra sütüna yaz. sonra sütün sayısnı bir arttır yani yan sütuna gel.
            createACell(row, columnCount++, invoice.getName(), cellStyle);
            createACell(row, columnCount++, invoice.getLocation(), cellStyle);
            createACell(row, columnCount++, invoice.getAmount(), cellStyle);
            createACell(row, columnCount++, invoice.getDueDate(), dateCellStyle);

            createACell(row, columnCount++, invoice.getCreatedAt(), dateCellStyle);

        }
    }
    //Httpservlet geri parametre olarak alacağız.
    public void export(HttpServletResponse httpServletResponse) throws IOException {
        writeHeaderLine();
        writeDataLines();
        ServletOutputStream outputStream = httpServletResponse.getOutputStream(); //İstemciye byte halinde göndermek için yazıyoruz
        workbook.write(outputStream);
        workbook.close(); //Excel dosyaının yazımı bitince kapat.
        outputStream.close(); //Byte akışını kes
    }

}
