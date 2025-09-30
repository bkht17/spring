package com.example.demo.service.serviceimpl;

import com.example.demo.config.jwt.JwtUtil;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.AdminEntity;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails.getUsername());

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                .orElse("ADMIN");

        return new AuthResponse(token, userDetails.getUsername(), role);
    }

    @Override
    public void registerAdmin(AuthRequest authRequest) {
        if (adminRepository.existsByUsername(authRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        AdminEntity admin = new AdminEntity();
        admin.setUsername(authRequest.getUsername());
        admin.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        admin.setEmail(authRequest.getUsername() + "@admin.com");

        adminRepository.save(admin);
    }
}