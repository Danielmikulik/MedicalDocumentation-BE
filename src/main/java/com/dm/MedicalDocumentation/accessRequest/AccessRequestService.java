package com.dm.MedicalDocumentation.accessRequest;

import com.dm.MedicalDocumentation.response.AccessRequestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessRequestService {

    private AccessRequestRepository repository;
    public List<AccessRequestResponse> getPatientsAccessRequests(String userLogin) {
        List<AccessRequest> requests = repository.findByPatientUserUserLogin(userLogin);
        return null;
    }
}
