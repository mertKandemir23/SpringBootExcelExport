package com.kandemirmert.springbootexcel.service;

import com.kandemirmert.springbootexcel.dto.FaturaDto;
import com.kandemirmert.springbootexcel.model.Fatura;
import com.kandemirmert.springbootexcel.repository.FaturaRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FaturaService {
    private final FaturaRepository faturaRepository;
    private final ModelMapper modelMapper;

    public FaturaDto faturaKaydet(Fatura fatura){
        LocalDate localDate = LocalDate.now();//For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        String formattedString = localDate.format(formatter);

        fatura.setCreatedAt(formattedString);
        LocalDate localDate2 = LocalDate.now().plusMonths(1L);//For reference
        String formattedString2 = localDate2.format(formatter);

        fatura.setDueDate(formattedString2);
        faturaRepository.save(fatura);
        return modelMapper.map(fatura,FaturaDto.class);
    }

    public FaturaDto faturaGuncelle(String name,Fatura fatura){
        Fatura fatura1 = faturaRepository.findByName(name);
        fatura1.setAmount(fatura.getAmount());
        fatura1.setLocation(fatura.getLocation());
        fatura1.setName(fatura.getName());
        faturaRepository.save(fatura1);
        return modelMapper.map(fatura1,FaturaDto.class);

    }
   public List<FaturaDto> faturaDtoList() {
       List<Fatura> faturaList = faturaRepository.findAll();
       List<FaturaDto> faturaDtoList = new ArrayList<>();
       for (Fatura fatura : faturaList
       ) {
           faturaDtoList.add(modelMapper.map(fatura, FaturaDto.class));

       }
       return faturaDtoList;
   }

    public List<Fatura> faturaList(){
        return faturaRepository.findAll();
    }

    public void  faturayiSil(String name){
        faturaRepository.delete(faturaRepository.findByName(name));

    }

    public void tumFaturalariSil(){
        faturaRepository.deleteAll();
    }

    public FaturaDto faturaBul(String name){
        return modelMapper.map(faturaRepository.findByName(name),FaturaDto.class);

    }



}
