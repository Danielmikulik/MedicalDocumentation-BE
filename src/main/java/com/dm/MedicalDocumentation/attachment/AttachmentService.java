package com.dm.MedicalDocumentation.attachment;

import com.dm.MedicalDocumentation.medicalExamination.MedicalExaminationService;
import com.dm.MedicalDocumentation.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final MedicalExaminationService medService;
    private final AttachmentRepository repository;
    public List<Attachment> getAttachmentForMedExam(long examId, String userLogin, Role role) {
        if (medService.hasUserAccess(examId, userLogin, role)) {
            return repository.findByMedicalExaminationMedicalExaminationId(examId);
        }
        return null;
    }
}
