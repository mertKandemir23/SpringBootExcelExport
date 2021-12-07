package com.kandemirmert.springbootexcel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class InvoiceDto {
    Long id;

    String name;
    String location;
    Double amount;

    String dueDate;

    String createdAt;
}
