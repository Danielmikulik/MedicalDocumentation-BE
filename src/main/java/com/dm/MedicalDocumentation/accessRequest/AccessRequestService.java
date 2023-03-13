package com.dm.MedicalDocumentation.accessRequest;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.doctor.DoctorRepository;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import com.dm.MedicalDocumentation.medicalExamination.MedicalExamination;
import com.dm.MedicalDocumentation.medicalExamination.MedicalExaminationRepository;
import com.dm.MedicalDocumentation.patient.Patient;
import com.dm.MedicalDocumentation.patient.PatientRepository;
import com.dm.MedicalDocumentation.person.Person;
import com.dm.MedicalDocumentation.request.AccessRequestRequest;
import com.dm.MedicalDocumentation.response.AccessRequestGroupResponse;
import com.dm.MedicalDocumentation.response.AccessRequestResponse;
import com.dm.MedicalDocumentation.response.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccessRequestService {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private MedicalExaminationRepository medExamRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private AccessRequestRepository repository;
    public List<AccessRequestGroupResponse> getAccessRequestsByDoctor(String userLogin) {
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given login found!"));
        List<Object[]> groups = repository.getAccessRequestCounts(doctor);
        List<AccessRequestGroupResponse> result = new ArrayList<>(groups.size());
        for (Object[] group : groups) {
            Patient patient = (Patient) group[1];
            result.add(AccessRequestGroupResponse.builder()
                    .count((long) group[0])
                    .patientName(patient.getPerson().getFullName())
                    .patientBirthNumber(patient.getPerson().getBirthNumber())
                    .department(((DepartmentType) group[2]).getDepartmentTypeName())
                    .build());
        }
        return result;
    }

    public Integer createAccessRequestsForPatientsMedExams(String userLogin, AccessRequestRequest request) {
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given login found!"));
        Patient patient = patientRepository.findByPersonBirthNumber(request.getPatient())
                .orElseThrow(() -> new IllegalArgumentException("No patient with given birth number found!"));
        List<MedicalExamination> exams = medExamRepository
                .findByPatientAndDepartmentTypeDepartmentTypeNameAndStartTimeGreaterThan(patient,
                        request.getDepartment(), request.getDateSince().atStartOfDay());

        List<AccessRequest> accessRequests = new ArrayList<>(exams.size());
        for (MedicalExamination exam : exams) {
            if (repository.findByMedicalExaminationAndPatientAndDoctor(exam, patient, doctor).isEmpty()) {
                accessRequests.add(AccessRequest.builder()
                        .medicalExamination(exam)
                        .patient(patient)
                        .doctor(doctor)
                        .approved(false)
                        .accessibleUntil(request.getDateUntil())
                        .build()
                );
            }
        }
        return repository.saveAll(accessRequests).size();
    }

    public CustomPage<AccessRequestResponse> getDoctorsPatientsAccessRequests(String userLogin, Pageable page, String patientName, String patientBirthNumber, String requestDoctor, String examDoctor, String department) {
        patientName = patientName == null ? "" : patientName;
        patientBirthNumber = patientBirthNumber == null ? "" : patientBirthNumber;
        requestDoctor = requestDoctor == null ? "" : requestDoctor;
        examDoctor = examDoctor == null ? "" : examDoctor;
        department = department == null ? "" : department;

        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given login found!"));

        Page<AccessRequest> requestsPage = repository.getGeneralPractitionersPatientsAccessRequests(doctor, patientName,
                patientBirthNumber, requestDoctor, examDoctor, department, page);

        List<AccessRequest> requests = requestsPage.getContent();
        List<AccessRequestResponse> result = new ArrayList<>(requests.size());
        for (AccessRequest request : requests) {
            result.add(AccessRequestResponse.builder()
                            .id(request.getRequestId())
                            .patientName(request.getPatient().getPerson().getFullName())
                            .patientBirthNumber(request.getPatient().getPerson().getBirthNumber())
                            .requestDoctor(request.getDoctor().getPerson().getFullName())
                            .requestDoctorId(request.getDoctor().getDoctorId())
                            .examDoctor(request.getMedicalExamination().getDoctor().getPerson().getFullName())
                            .examDoctorId(request.getMedicalExamination().getDoctor().getDoctorId())
                            .department(request.getMedicalExamination().getDepartmentType().getDepartmentTypeName())
                            .startTime(request.getMedicalExamination().getStartTime())
                            .accessibleUntil(request.getAccessibleUntil())
                    .build());
        }
        CustomPage<AccessRequestResponse> resultPage = new CustomPage<>(result, requestsPage.getTotalElements());
        return resultPage;
    }

    public List<String> getDoctorsPatientsWithAccessRequests(String userLogin) {
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given login found!"));
        List<Patient> patients = repository.getGeneralPractitionersPatientsWithDisapprovedAccessRequests(doctor);
        List<String> result = new ArrayList<>(patients.size());
        for (Patient patient : patients) {
            Person person = patient.getPerson();
            result.add(person.getBirthNumber() + " " + person.getFullName());
        }
        return result;
    }

    public String confirmAccessRequests(String userLogin, List<Long> ids) {
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given login found!"));
        int confirmCount = 0;
        List<AccessRequest> requests = new ArrayList<>(ids.size());
        for (long id : ids) {
            Optional<AccessRequest> request = repository.findById(id);
            if (request.isPresent() && doctor.getDoctorId().equals(request.get().getPatient().getGeneralPractitioner().getDoctorId())) {
                request.get().setApproved(true);
                requests.add(request.get());
                confirmCount++;
            }
        }
        repository.saveAll(requests);
        return String.valueOf(confirmCount);
    }
}
