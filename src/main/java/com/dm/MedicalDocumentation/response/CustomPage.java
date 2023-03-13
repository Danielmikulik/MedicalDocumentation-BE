package com.dm.MedicalDocumentation.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class CustomPage<T> {
    private List<T> content;
    private long totalRows;

    public CustomPage(List<T> content, long totalRows) {
        this.content = content;
        this.totalRows = totalRows;
    }
}