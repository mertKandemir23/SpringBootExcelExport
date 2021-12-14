package com.kandemirmert.springbootexcel.controller;

import com.kandemirmert.springbootexcel.dto.InvoiceDto;
import com.kandemirmert.springbootexcel.exporter.InvoiceExcelExporter;
import com.kandemirmert.springbootexcel.exporter.InvoicePdfExporter;
import com.kandemirmert.springbootexcel.model.Invoice;
import com.kandemirmert.springbootexcel.service.InvoiceService;
import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/invoice")
public class InvoiceController {
    private final InvoiceService invoiceService;




    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Invoice_info.xlsx"; //dosya adı belirttik.

        response.setHeader(headerKey, headerValue); //Response headerın içine değerleri koyduk.
        List<Invoice> invoiceList = invoiceService.invoiceList();
        InvoiceExcelExporter invoiceExcelExporter = new InvoiceExcelExporter(invoiceList); //exporter sınıfını initialize ettik
        invoiceExcelExporter.export(response); //parametre olarak aldığımız cevabı işleyip döndürdük

    }

    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");


        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=list_of_invoices"+".pdf";
        response.setHeader(headerKey, headerValue);

        List<Invoice> invoiceList = invoiceService.invoiceList();

        InvoicePdfExporter exporter = new InvoicePdfExporter(invoiceList);
        exporter.export(response);

    }

    @PostMapping("/save-invoice")
    public ResponseEntity<InvoiceDto> saveInvoice(@RequestBody Invoice invoice) {
        return ResponseEntity.ok(invoiceService.createAnInvoice(invoice));
    }

    @PutMapping("/{name}")
    public ResponseEntity<InvoiceDto> updateInvoiceByName(@PathVariable String name, @RequestBody Invoice invoice){
        return ResponseEntity.ok(invoiceService.faturaGuncelle(name,invoice));

    }

    @GetMapping("/{name}")
    public ResponseEntity<InvoiceDto> findInvoiceByName(@PathVariable String name){
        return ResponseEntity.ok(invoiceService.deleteByName(name));

    }
    @GetMapping("/all-invoices")
    public ResponseEntity<List<InvoiceDto>> allInvoices(){
        return ResponseEntity.ok(invoiceService.invoiceDtoList());
    }
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteAnInvoice(@PathVariable String name){
        invoiceService.deleteAnInvoice(name);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/clear-all-invoices")
    public ResponseEntity<Void> deleteAllInvoices(){
        invoiceService.deleteAllInvoices();
        return ResponseEntity.ok().build();

    }
}

