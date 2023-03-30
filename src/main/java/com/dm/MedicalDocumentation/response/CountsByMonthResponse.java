package com.dm.MedicalDocumentation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountsByMonthResponse {
    private List<Long> counts;
    private List<String> intervals;
}
