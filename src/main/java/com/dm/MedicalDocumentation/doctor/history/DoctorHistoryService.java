package com.dm.MedicalDocumentation.doctor.history;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.hospital.department.Department;
import com.dm.MedicalDocumentation.hospital.department.DepartmentID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorHistoryService {
    private final DoctorHistoryRepository repository;
    public void createDoctorHistoryRecord(Doctor doctor, boolean newDoctor) {
        LocalDate dateFrom;
        if (!newDoctor) {
            Optional<DoctorHistory> latestHistory = repository.findFirstByIdDoctorOrderByIdDateFromDesc(doctor);
            if (latestHistory.isPresent()) {
                latestHistory.get().setDateTo(LocalDate.now());
                repository.save(latestHistory.get());
            }
            dateFrom = LocalDate.now();
        } else {
            dateFrom = doctor.getUser().getCreatedAt().toLocalDate();
        }

        DoctorHistory doctorHistory = DoctorHistory.builder()
                .id(DoctorHistoryID.builder()
                        .doctor(doctor)
                        .dateFrom(dateFrom)
                        .build())
                .dateTo(null)
                .department(Department.builder()
                        .id(DepartmentID.builder()
                                .hospital(doctor.getDepartment().getId().getHospital())
                                .departmentType(doctor.getDepartment().getId().getDepartmentType())
                                .build())
                        .build())
                .build();
        repository.save(doctorHistory);
    }
}
