package com.dm.MedicalDocumentation.doctor;

import com.dm.MedicalDocumentation.GlobalConstants;
import com.dm.MedicalDocumentation.doctor.history.DoctorHistoryService;
import com.dm.MedicalDocumentation.hospital.Hospital;
import com.dm.MedicalDocumentation.hospital.HospitalRepository;
import com.dm.MedicalDocumentation.hospital.department.Department;
import com.dm.MedicalDocumentation.hospital.department.DepartmentID;
import com.dm.MedicalDocumentation.hospital.department.DepartmentRepository;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentTypeRepository;
import com.dm.MedicalDocumentation.patient.PatientRepository;
import com.dm.MedicalDocumentation.person.Person;
import com.dm.MedicalDocumentation.person.PersonRepository;
import com.dm.MedicalDocumentation.response.userInfo.PublicDoctorInfoResponse;
import com.dm.MedicalDocumentation.user.User;
import com.dm.MedicalDocumentation.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository repository;
    private final HospitalRepository hospitalRepository;
    private final DepartmentTypeRepository departmentTypeRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorHistoryService doctorHistoryService;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    public PublicDoctorInfoResponse getDoctorInfo(long doctorId) {
        Doctor doctor = repository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given id found!"));
        return PublicDoctorInfoResponse.builder()
                .hospital(doctor.getDepartment().getId().getHospital().getHospitalName())
                .department(doctor.getDepartment().getId().getDepartmentType().getDepartmentTypeName())
                .email(doctor.getUser().getEmail())
                .build();
    }

    public List<String> getAllDoctors() {
        List<Doctor> doctors = repository.findAll();
        List<String> result = new ArrayList<>(doctors.size());
        for (Doctor doctor : doctors) {
            result.add(doctor.getPerson().getBirthNumber() + " " + doctor.getPerson().getFullName());
        }
        return result;
    }

    public List<String> getAllGeneralPractitioners() {
        DepartmentType departmentType = departmentTypeRepository.findByDepartmentTypeName(GlobalConstants.GENERAL_PRACTITIONERS_CLINIC)
                .orElseThrow(() -> new IllegalArgumentException("No department type Ambulancia všeobecného lekára found!"));
        List<Doctor> doctors = repository.findByDepartmentIdDepartmentType(departmentType);
        List<String> result = new ArrayList<>(doctors.size());
        for (Doctor doctor : doctors) {
            result.add(doctor.getPerson().getBirthNumber() + " " + doctor.getPerson().getFullName());
        }
        return result;
    }

    public boolean departmentEquals(DoctorChangeRequest request) {
        Optional<Doctor> doctor = repository.findByPersonBirthNumber(request.getDoctor());
        return doctor.isPresent() 
                && doctor.get().getDepartment().getId().getHospital().getHospitalName().equals(request.getHospital())
                && doctor.get().getDepartment().getId().getDepartmentType().getDepartmentTypeName().equals(request.getDepartmentType());
    }

    @Transactional
    public void changeDepartment(DoctorChangeRequest request) {
        Doctor doctor = repository.findByPersonBirthNumber(request.getDoctor())
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given birthNumber found!"));
        Hospital hospital = hospitalRepository.findByHospitalName(request.getHospital())
                .orElseThrow(() -> new IllegalArgumentException("No hospital with given name found!"));
        DepartmentType departmentType = departmentTypeRepository.findByDepartmentTypeName(request.getDepartmentType())
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given birthNumber found!"));

        Optional<Department> department = departmentRepository.findByIdHospitalHospitalNameAndIdDepartmentTypeDepartmentTypeName(
                request.getHospital(), request.getDepartmentType()
        );
        if (department.isEmpty()) {
            throw new IllegalArgumentException("Department type does not exist in given hospital");
        }

        doctor.setDepartment(Department.builder()
                        .id(DepartmentID.builder()
                                .hospital(hospital)
                                .departmentType(departmentType)
                                .build())
                .build());

        repository.save(doctor);

        doctorHistoryService.createDoctorHistoryRecord(doctor, false);
    }

    public boolean recordExists(DoctorRequest request) {
        Optional<Doctor> doctor = repository.findByPersonBirthNumber(request.getPerson());
        Optional<User> user = userRepository.findByUserLogin(request.getUserLogin());
        Doctor foundDoctor = null;
        if (user.isPresent()) {
            foundDoctor = user.get().getDoctor();
        }
        return doctor.isPresent() || foundDoctor != null;
    }

    @Transactional
    public void createDoctor(DoctorRequest request) {
        Person person = personRepository.findByBirthNumber(request.getPerson())
                .orElseThrow(() -> new IllegalArgumentException("No person with birthNumber " + request.getPerson() + " exists."));
        Hospital hospital = hospitalRepository.findByHospitalName(request.getHospital())
                .orElseThrow(() -> new IllegalArgumentException("No hospital with given name found!"));
        DepartmentType departmentType = departmentTypeRepository.findByDepartmentTypeName(request.getDepartmentType())
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given birthNumber found!"));
        User user = userRepository.findByUserLogin(request.getUserLogin())
                .orElseThrow(() -> new IllegalArgumentException("No user with login " + request.getUserLogin() + " exists."));

        Optional<Department> department = departmentRepository.findByIdHospitalHospitalNameAndIdDepartmentTypeDepartmentTypeName(
                request.getHospital(), request.getDepartmentType()
        );
        if (department.isEmpty()) {
            throw new IllegalArgumentException("Department type does not exist in given hospital");
        }

        Doctor doctor = Doctor.builder()
                .user(user)
                .person(person)
                .department(Department.builder()
                        .id(DepartmentID.builder()
                                .hospital(hospital)
                                .departmentType(departmentType)
                                .build())
                        .build())
                .build();

        repository.save(doctor);

        doctorHistoryService.createDoctorHistoryRecord(doctor, true);
    }

    public Integer getDoctorsPatientCount(String userLogin) {
        Doctor doctor = repository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given userLogin found!"));
        return doctor.getDepartment().getId().getDepartmentType().getDepartmentTypeName()
                .equals(GlobalConstants.GENERAL_PRACTITIONERS_CLINIC)
                ? patientRepository.generalPractitionersPatientsCount(doctor)
                : patientRepository.doctorsPatientsCount(doctor);
    }
}
