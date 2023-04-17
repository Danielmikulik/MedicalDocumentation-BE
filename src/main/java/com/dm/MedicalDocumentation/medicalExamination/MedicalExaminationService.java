package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.accessRequest.AccessRequest;
import com.dm.MedicalDocumentation.accessRequest.AccessRequestRepository;
import com.dm.MedicalDocumentation.attachment.Attachment;
import com.dm.MedicalDocumentation.attachment.AttachmentRepository;
import com.dm.MedicalDocumentation.disease.Disease;
import com.dm.MedicalDocumentation.disease.DiseaseRepository;
import com.dm.MedicalDocumentation.disease.type.DiseaseType;
import com.dm.MedicalDocumentation.disease.type.DiseaseTypeRepository;
import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.doctor.DoctorRepository;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentTypeRepository;
import com.dm.MedicalDocumentation.medicalExamination.type.ExaminationType;
import com.dm.MedicalDocumentation.medicalExamination.type.ExaminationTypeRepository;
import com.dm.MedicalDocumentation.patient.Patient;
import com.dm.MedicalDocumentation.patient.PatientRepository;
import com.dm.MedicalDocumentation.request.MedicalExamRequest;
import com.dm.MedicalDocumentation.response.CountsByMonthResponse;
import com.dm.MedicalDocumentation.response.CustomPage;
import com.dm.MedicalDocumentation.response.MedicalExamResponse;
import com.dm.MedicalDocumentation.user.Role;
import com.dm.MedicalDocumentation.util.GraphDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalExaminationService {

    private final MedicalExaminationRepository repository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AccessRequestRepository accessRequestRepository;
    private final DiseaseRepository diseaseRepository;
    private final DiseaseTypeRepository diseaseTypeRepository;
    private final ExaminationTypeRepository examinationTypeRepository;
    private final AttachmentRepository attachmentRepository;
    private final DepartmentTypeRepository departmentTypeRepository;

    public CustomPage<MedicalExamResponse> getPatientsExams(String userLogin, String type, String examDoctor, String department,
                                                            Pageable page) {
        type = type == null ? "" : type;
        examDoctor = examDoctor == null ? "" : examDoctor;
        department = department == null ? "" : department;
        Patient patient = patientRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("No patient with given login found."));
        Page<MedicalExamination> exams = repository.findPatientsExams(patient, type, examDoctor, department, page);
        List<MedicalExamResponse> results = new ArrayList<>(exams.getContent().size());
        for (MedicalExamination exam : exams.getContent()) {
            String disease = exam.getDisease() != null
                    ? exam.getDisease().getDiseaseType().getDiseaseTypeName()
                    : null;
            results.add(MedicalExamResponse.builder()
                    .id(exam.getMedicalExaminationId())
                    .type(exam.getType().getExaminationTypeName())
                    .disease(disease)
                    .doctor(exam.getDoctor().getPerson().getFullName())
                    .doctorId(exam.getDoctor().getDoctorId())
                    .department(exam.getDepartmentType().getDepartmentTypeName())
                    .startTime(exam.getStartTime())
                    .endTime(exam.getEndTime())
                    .build()
            );
        }
        return new CustomPage<>(results, exams.getTotalElements(), exams.getTotalPages());
    }

    public CustomPage<MedicalExamResponse> getDoctorsExams(String userLogin, String patientBirthNumber, boolean isGeneralPractitioner,
                                                           String type, String examDoctor, String department,
                                                           Pageable page) {
        type = type == null ? "" : type;
        examDoctor = examDoctor == null ? "" : examDoctor;
        department = department == null ? "" : department;
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("No doctor with given login found."));
        Patient patient = null;
        if (patientBirthNumber != null) {
            patient = patientRepository.findByPersonBirthNumber(patientBirthNumber)
                    .orElseThrow(() -> new IllegalArgumentException("No patient with given birth number found."));
        }
        List<Patient> patients = isGeneralPractitioner
                ? patientRepository.findGeneralPractitionersPatients(doctor)
                : patientRepository.findDoctorsPatients(doctor);
        Page<MedicalExamination> exams = patient == null
                ? repository.findAllExamsWithinDepartmentAndWithAccess(doctor,
                    doctor.getDepartment().getId().getDepartmentType(), patients, type, examDoctor, department, page)
                : repository.findPatientsExamsWithinDepartmentAndWithAccess(doctor,
                    doctor.getDepartment().getId().getDepartmentType(), patient, type, examDoctor, department, page);
        List<MedicalExamResponse> results = new ArrayList<>(exams.getContent().size());
        for (MedicalExamination exam : exams.getContent()) {
            String examDisease = exam.getDisease() != null
                    ? exam.getDisease().getDiseaseType().getDiseaseTypeName()
                    : null;
            results.add(MedicalExamResponse.builder()
                    .id(exam.getMedicalExaminationId())
                    .type(exam.getType().getExaminationTypeName())
                    .disease(examDisease)
                    .patient(exam.getPatient().getPerson().getFullName())
                    .doctor(exam.getDoctor().getPerson().getFullName())
                    .doctorId(exam.getDoctor().getDoctorId())
                    .department(exam.getDepartmentType().getDepartmentTypeName())
                    .startTime(exam.getStartTime())
                    .endTime(exam.getEndTime())
                    .build()
            );
        }
        return new CustomPage<>(results, exams.getTotalElements(), exams.getTotalPages());
    }

    public boolean hasUserAccess(long examId, String userLogin, Role role) {
        MedicalExamination medicalExamination = repository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("No exam with id " + examId + "found!"));
        switch (role) {
            case DOCTOR -> {
                return hasDoctorAccess(medicalExamination, userLogin);
            }
            case PATIENT -> {
                return hasPatientAccess(medicalExamination, userLogin);
            }
        }
        return false;
    }

    private boolean hasPatientAccess(MedicalExamination medicalExamination, String userLogin) {
        return medicalExamination.getPatient().getUser().getUserLogin().equals(userLogin);
    }

    private boolean hasDoctorAccess(MedicalExamination medicalExamination, String userLogin) {
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given login found!"));
        if (medicalExamination.getDoctor().equals(doctor)
                || medicalExamination.getPatient().getGeneralPractitioner().equals(doctor)
                || doctor.getDepartment().getId().getDepartmentType().equals(medicalExamination.getDepartmentType())) {
            return true;
        }

        List<AccessRequest> requests = accessRequestRepository
                .findByMedicalExaminationMedicalExaminationId(medicalExamination.getMedicalExaminationId());
        for (AccessRequest request : requests) {
            if (request.getApproved()
                    && request.getDoctor().getDoctorId().equals(doctor.getDoctorId())
                    && request.getAccessibleUntil().isAfter(LocalDate.now())) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public String createMedicalExam(String userLogin, MedicalExamRequest request) {
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("No doctor with given login found!"));
        Patient patient = patientRepository.findByPersonBirthNumber(request.getPatient())
                .orElseThrow(() -> new IllegalArgumentException("No patient with given birth number found."));

        Disease disease = null;
        if (!request.getDiseaseType().isBlank()) {
            DiseaseType diseaseType = diseaseTypeRepository.findByDiseaseTypeName(request.getDiseaseType())
                    .orElseThrow(() -> new IllegalArgumentException("Disease type " + request.getDiseaseType() + "does not exist."));
            Optional<Disease> foundDisease = diseaseRepository.findByPatientAndDiseaseTypeAndCured(patient,
                    diseaseType, null);
            if (foundDisease.isEmpty()) {
                disease = Disease.builder()
                        .diseaseType(diseaseType)
                        .patient(patient)
                        .diagnosed(request.getEndTime())
                        .cured(null)
                        .build();
                disease = diseaseRepository.save(disease);
            } else {
                disease = foundDisease.get();
            }
        }

        ExaminationType examinationType = examinationTypeRepository.findByExaminationTypeName(request.getExaminationType())
                .orElseThrow(() -> new IllegalArgumentException("No examinationType " + request.getExaminationType() + " found."));

        MedicalExamination medicalExamination = MedicalExamination.builder()
                .type(examinationType)
                .departmentType(doctor.getDepartment().getId().getDepartmentType())
                .disease(disease)
                .patient(patient)
                .doctor(doctor)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
        MedicalExamination exam = repository.save(medicalExamination);

        if ((request.getFile() != null && !request.getFile().isEmpty())
                || (request.getReport() != null && !request.getReport().isBlank())) {
            createAttachment(request.getReport(), request.getFile(), exam);
        }

        return "created";
    }

    public void createAttachment(String report, MultipartFile file, MedicalExamination exam) {
        byte[] image = null;
        if (file != null && !file.isEmpty()) {
            try {
                image = file.getBytes();
            } catch (IOException e) {
                throw new RuntimeException("Couldn't convert file to byte[].", e);
            }
        }
        Attachment attachment = Attachment.builder()
                .report(report)
                .file(image)
                .medicalExamination(exam)
                .build();
        attachmentRepository.save(attachment);
    }

    public CountsByMonthResponse getExamCountsForInterval(String userLogin, LocalDate dateSince, LocalDate dateUntil, String interval, boolean isDoctor) {
        boolean monthInterval = interval.equals("month");
        LocalDateTime endDate;
        LocalDateTime startDate;
        if (monthInterval) {
            startDate = dateSince.atStartOfDay().withDayOfMonth(1);
            if (dateUntil.getMonth() == Month.FEBRUARY && !Year.isLeap(dateUntil.getYear())) {
                endDate = dateUntil.atStartOfDay().withDayOfMonth(28).with(LocalTime.MAX);
            } else {
                endDate = dateUntil.atStartOfDay().withDayOfMonth(dateUntil.getMonth().maxLength()).with(LocalTime.MAX);
            }
        } else {
            DayOfWeek startDay = dateSince.getDayOfWeek();
            startDate = dateSince.minusDays(startDay.getValue() - 1).atStartOfDay();
            DayOfWeek endDay = dateUntil.getDayOfWeek();
            endDate = dateUntil.plusDays(7 - endDay.getValue()).atTime(LocalTime.MAX);
        }

        List<Object[]> data;
        if (isDoctor) {
            Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                    .orElseThrow(() -> new UsernameNotFoundException("No doctor with given login found!"));
            data = monthInterval
                    ? repository.getDoctorExamCountByMonth(doctor, startDate, endDate)
                    : repository.getDoctorExamCountByWeek(doctor, startDate, endDate);
        } else {
            Patient patient = patientRepository.findByUserUserLogin(userLogin)
                    .orElseThrow(() -> new UsernameNotFoundException("No patient with given login found!"));
            data = monthInterval
                    ? repository.getPatientsExamCountByMonth(patient, startDate, endDate)
                    : repository.getPatientsExamCountByWeek(patient, startDate, endDate);
        }
        return GraphDataUtil.getCountsByInterval(startDate, endDate, data, monthInterval);
    }

    public Long getPatientsTotalExamCount(String userLogin, String departmentTypeName) {
        Patient patient = patientRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("No patient with given login found!"));
        if (!departmentTypeName.isBlank()) {
            DepartmentType departmentType = departmentTypeRepository.findByDepartmentTypeName(departmentTypeName)
                    .orElseThrow(() -> new IllegalArgumentException("Department type " + departmentTypeName + "does not exist."));
            return repository.countByPatientAndDepartmentType(patient, departmentType);
        }
        return repository.countByPatient(patient);
    }

    public Long getDoctorsTotalExamCount(String userLogin, String diseaseTypeName) {
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("No doctor with given login found!"));
        if (!diseaseTypeName.isBlank()) {
            DiseaseType diseaseType = diseaseTypeRepository.findByDiseaseTypeName(diseaseTypeName)
                    .orElseThrow(() -> new IllegalArgumentException("Disease type " + diseaseTypeName + "does not exist."));
            return repository.countByDoctorAndDiseaseDiseaseType(doctor, diseaseType);
        }
        return repository.countByDoctor(doctor);
    }
}
