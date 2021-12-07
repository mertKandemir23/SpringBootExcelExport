package com.kandemirmert.springbootexcel.service;

import com.kandemirmert.springbootexcel.dto.InvoiceDto;
import com.kandemirmert.springbootexcel.model.Invoice;
import com.kandemirmert.springbootexcel.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ModelMapper modelMapper;

    public InvoiceDto createAnInvoice(Invoice invoice){
        LocalDate localDate = LocalDate.now();//For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        String formattedString = localDate.format(formatter);

        invoice.setCreatedAt(formattedString);
        LocalDate localDate2 = LocalDate.now().plusMonths(1L);//For reference
        String formattedString2 = localDate2.format(formatter);

        invoice.setDueDate(formattedString2);
        invoiceRepository.save(invoice);
        return modelMapper.map(invoice, InvoiceDto.class);
    }

    public InvoiceDto faturaGuncelle(String name, Invoice invoice){
        Invoice invoice1 = invoiceRepository.findByName(name);
        invoice1.setAmount(invoice.getAmount());
        invoice1.setLocation(invoice.getLocation());
        invoice1.setName(invoice.getName());
        invoiceRepository.save(invoice1);
        return modelMapper.map(invoice1, InvoiceDto.class);

    }
   public List<InvoiceDto> invoiceDtoList() {
       List<Invoice> invoiceList = invoiceRepository.findAll();
       List<InvoiceDto> invoiceDtoList = new ArrayList<>();
       for (Invoice invoice : invoiceList
       ) {
           invoiceDtoList.add(modelMapper.map(invoice, InvoiceDto.class));

       }
       return invoiceDtoList;
   }

    public List<Invoice> invoiceList(){
        return invoiceRepository.findAll();
    }

    public void deleteAnInvoice(String name){
        invoiceRepository.delete(invoiceRepository.findByName(name));

    }

    public void deleteAllInvoices(){
        invoiceRepository.deleteAll();
    }

    public InvoiceDto deleteByName(String name){
        return modelMapper.map(invoiceRepository.findByName(name), InvoiceDto.class);

    }



}
