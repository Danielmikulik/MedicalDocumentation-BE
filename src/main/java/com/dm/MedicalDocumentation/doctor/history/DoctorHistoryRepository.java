package com.dm.MedicalDocumentation.doctor.history;

import com.dm.MedicalDocumentation.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorHistoryRepository extends JpaRepository<DoctorHistory, DoctorHistoryID> {
    Optional<DoctorHistory> findFirstByIdDoctorOrderByIdDateFromDesc(Doctor doctor);
}
