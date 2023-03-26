package com.dm.MedicalDocumentation.util;

import com.dm.MedicalDocumentation.response.CountsByMonthResponse;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GraphDataUtil {
    public static CountsByMonthResponse getCountsByMonth(LocalDateTime startDate, List<Object[]> data) {
        int startMonth = startDate.getMonthValue();
        List<Long> counts = new ArrayList<>(12);
        List<String> months = new ArrayList<>(12);
        for (int i = startMonth; i < startMonth + 12; i++) {
            int month = ((i - 1) % 12) + 1;
            String monthName = Month.of(month).getDisplayName(TextStyle.SHORT_STANDALONE, new Locale("sk"));
            months.add(monthName);

            long countInMonth = 0;
            //row[0] is count, row[1] is month
            for (Object[] row : data) {
                if ((int)row[1] == month) {
                    countInMonth = (long) row[0];
                    break;
                }
            }
            counts.add(countInMonth);
        }
        return CountsByMonthResponse.builder()
                .counts(counts)
                .months(months)
                .build();
    }
}
