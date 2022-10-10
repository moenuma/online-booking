package com.moenuma.online.booking.controller;

import com.moenuma.online.booking.model.ERole;
import com.moenuma.online.booking.model.ImmutableUser;
import com.moenuma.online.booking.model.Role;
import com.moenuma.online.booking.model.ImmutableRole;
import com.moenuma.online.booking.model.User;
import com.moenuma.online.booking.model.request.LoginRequest;
import com.moenuma.online.booking.model.request.SignupRequest;
import com.moenuma.online.booking.repository.RoleRepository;
import com.moenuma.online.booking.repository.UserRepository;
import com.moenuma.online.booking.model.response.JwtResponse;
import com.moenuma.online.booking.security.jwt.JwtUtils;
import com.moenuma.online.booking.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@Valid @RequestBody final LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@Valid @RequestBody final SignupRequest signupRequest) {

        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }

        Set<ERole> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Role userRole = roleRepository.findByName(role)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            });
        }

        User user = ImmutableUser.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(signupRequest.getPassword())
                .addAllRoles(roles)
                .build();
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/role")
    public ResponseEntity registerRole() {
        final Role roleUser = ImmutableRole.builder().name(ERole.ROLE_USER).build();
        final Role roleAdmin = ImmutableRole.builder().name(ERole.ROLE_ADMIN).build();
        final Role roleMod = ImmutableRole.builder().name(ERole.ROLE_MOD).build();
        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);
        roleRepository.save(roleMod);
        return ResponseEntity.ok("ok");
    }

}
