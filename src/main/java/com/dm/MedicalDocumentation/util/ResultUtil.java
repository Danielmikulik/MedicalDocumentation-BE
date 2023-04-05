package com.dm.MedicalDocumentation.util;

import com.dm.MedicalDocumentation.medication.Medication;

import java.util.ArrayList;
import java.util.List;

public class ResultUtil {
    public static List<String> getMedicationsAsStringList(List<Medication> medications) {
        List<String> result = new ArrayList<>(medications.size());
        for (Medication medication : medications) {
            StringBuilder medicationString = new StringBuilder(medication.getMedicationName());
            medicationString.append(" ");
            medicationString.append(medication.getAmount());
            medicationString.append(" ");
            medicationString.append(medication.getUnit());
            result.add(medicationString.toString());
        }
        return result;
    }
}
