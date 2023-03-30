package com.dm.MedicalDocumentation.util;

import com.dm.MedicalDocumentation.response.CountsByMonthResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.chrono.ChronoLocalDate;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class GraphDataUtil {
    public static CountsByMonthResponse getCountsByInterval(LocalDateTime startDate, LocalDateTime endDate, List<Object[]> data,
                                                            boolean isMonthInterval) {
        return isMonthInterval
                ? getCountsByMonths(startDate, endDate, data)
                : getCountsByWeeks(startDate, endDate, data);
    }

    private static CountsByMonthResponse getCountsByWeeks(LocalDateTime startDate, LocalDateTime endDate, List<Object[]> data) {
        List<Long> counts = new LinkedList<>();
        List<String> weeks = new LinkedList<>();

        WeekFields weekFields = WeekFields.ISO;
        LocalDate date = startDate.toLocalDate();

        while (date.isBefore(ChronoLocalDate.from(endDate))) {
            int week = date.get(weekFields.weekOfWeekBasedYear());
            int year = date.getYear();

            weeks.add(week + " " + year);

            long countInWeek = 0;
            //row[0] is count, row[1] is week, row[2] is year
            for (Object[] row : data) {
                if ((int)row[1] == week && (int)row[2] == year) {
                    countInWeek = (long) row[0];
                    break;
                }
            }
            counts.add(countInWeek);

            date = date.plusWeeks(1);
        }
        return CountsByMonthResponse.builder()
                .counts(counts)
                .intervals(weeks)
                .build();
    }

    public static CountsByMonthResponse getCountsByMonths(LocalDateTime startDate, LocalDateTime endDate, List<Object[]> data) {
        int startMonth = startDate.getMonthValue();
        int year = startDate.getYear();
        Period diff = Period.between(startDate.toLocalDate(), endDate.toLocalDate());
        int monthsBetween = diff.getYears() * 12 + diff.getMonths() + 1;
        List<Long> counts = new ArrayList<>(12);
        List<String> months = new ArrayList<>(12);

        for (int i = startMonth; i < startMonth + monthsBetween; i++) {
            int month = ((i - 1) % 12) + 1;
            if (month == 1 && i != startMonth) {
                year++;
            }
            String monthName = Month.of(month).getDisplayName(TextStyle.SHORT_STANDALONE, new Locale("sk")) + " " + year;
            months.add(monthName);

            long countInMonth = 0;
            //row[0] is count, row[1] is month, row[2] is year
            for (Object[] row : data) {
                if ((int)row[1] == month && (int)row[2] == year) {
                    countInMonth = (long) row[0];
                    break;
                }
            }
            counts.add(countInMonth);
        }
        return CountsByMonthResponse.builder()
                .counts(counts)
                .intervals(months)
                .build();
    }
}
