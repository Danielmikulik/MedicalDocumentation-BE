package com.dm.MedicalDocumentation.attachment;

import com.dm.MedicalDocumentation.medicalExamination.MedicalExamination;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.sql.Clob;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Attachment {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long attachmentId;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "medical_examination", nullable = false)
    private MedicalExamination medicalExamination;
    @Lob
    private String report;
    @Lob
    private byte[] file;
}
