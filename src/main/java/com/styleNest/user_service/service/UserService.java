package com.styleNest.user_service.service;

import com.styleNest.user_service.dto.UserRegisterRequest;
import com.styleNest.user_service.dto.UserResponse;
import com.styleNest.user_service.entity.User;
import com.styleNest.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(UserRegisterRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new RuntimeException("Email already exists!");
        });
        userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new RuntimeException("Username already exists!");
        });

        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saveUser = userRepository.save(user);
        return modelMapper.map(saveUser, UserResponse.class);
    }
}
