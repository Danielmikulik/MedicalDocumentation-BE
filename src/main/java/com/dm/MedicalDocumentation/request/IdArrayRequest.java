package com.dm.MedicalDocumentation.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class IdArrayRequest {
    private List<Long> ids;
}
