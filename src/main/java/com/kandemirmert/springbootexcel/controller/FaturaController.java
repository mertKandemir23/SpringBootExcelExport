package com.kandemirmert.springbootexcel.controller;

import com.kandemirmert.springbootexcel.dto.FaturaDto;
import com.kandemirmert.springbootexcel.exporter.FaturaExcelExporter;
import com.kandemirmert.springbootexcel.exporter.FaturaPdfExporter;
import com.kandemirmert.springbootexcel.model.Fatura;
import com.kandemirmert.springbootexcel.service.FaturaService;
import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/fatura")
public class FaturaController {
    private final FaturaService faturaService;




    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Fatura_bilgisi.xlsx";

        response.setHeader(headerKey, headerValue);
        List<Fatura> faturaList = faturaService.faturaList();
        FaturaExcelExporter faturaExcelExporter = new FaturaExcelExporter(faturaList);
        faturaExcelExporter.export(response);

    }

    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");


        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=fatura_listesi"+".pdf";
        response.setHeader(headerKey, headerValue);

        List<Fatura> faturaList = faturaService.faturaList();

        FaturaPdfExporter exporter = new FaturaPdfExporter(faturaList);
        exporter.export(response);

    }

    @PostMapping("/faturakaydet")
    public ResponseEntity<FaturaDto> faturaKaydet(@RequestBody Fatura fatura) {
        return ResponseEntity.ok(faturaService.faturaKaydet(fatura));
    }

    @PutMapping("/{name}")
    public ResponseEntity<FaturaDto> faturaGuncelle(@PathVariable String name,@RequestBody Fatura fatura){
        return ResponseEntity.ok(faturaService.faturaGuncelle(name,fatura));

    }

    @GetMapping("/{name}")
    public ResponseEntity<FaturaDto> faturaGuncelle(@PathVariable String name){
        return ResponseEntity.ok(faturaService.faturaBul(name));

    }
    @GetMapping("/tumfaturalar")
    public ResponseEntity<List<FaturaDto>> tumFaturalar(){
        return ResponseEntity.ok(faturaService.faturaDtoList());
    }
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> faturaSil(@PathVariable String name){
        faturaService.faturayiSil(name);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/hepsini-temizle")
    public ResponseEntity<Void> faturaSil(){
        faturaService.tumFaturalariSil();
        return ResponseEntity.ok().build();

    }
}

