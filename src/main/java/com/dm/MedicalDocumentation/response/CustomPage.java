package com.dm.MedicalDocumentation.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CustomPage<T> {
    private List<T> content;
    private long totalRows;
    private long totalPages;

}