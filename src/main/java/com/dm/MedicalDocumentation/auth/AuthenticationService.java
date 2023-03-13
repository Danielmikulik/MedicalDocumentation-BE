package com.dm.MedicalDocumentation.auth;

import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.user.Role;
import com.dm.MedicalDocumentation.user.User;
import com.dm.MedicalDocumentation.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .userLogin(request.getUserLogin())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.fromString(request.getRole().toLowerCase()))
                .createdAt(LocalDateTime.now())
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUserLogin(),
                    request.getPassword()
            )
        );
        var user = repository.findByUserLogin(request.getUserLogin())
                .orElseThrow(() -> new IllegalArgumentException("No user with given login exists."));
        Map<String, Object> extraClaims = new HashMap<>(Map.of("Authorities", user.getAuthorities()));
        if (user.getDoctor() != null) {
            String department = user.getDoctor().getDepartment().getId().getDepartmentType().getDepartmentTypeName();
            extraClaims.put("department", department);
        }
        var jwtToken = jwtService.generateToken(extraClaims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
