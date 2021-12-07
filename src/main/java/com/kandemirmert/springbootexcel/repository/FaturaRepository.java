package com.kandemirmert.springbootexcel.repository;

import com.kandemirmert.springbootexcel.model.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaturaRepository extends JpaRepository<Fatura,Long> {
Fatura findByName(String faturaAdi);

}
