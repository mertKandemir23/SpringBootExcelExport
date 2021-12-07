package com.kandemirmert.springbootexcel.repository;

import com.kandemirmert.springbootexcel.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
Invoice findByName(String faturaAdi);

}
