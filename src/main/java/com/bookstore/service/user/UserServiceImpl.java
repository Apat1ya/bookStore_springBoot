package com.bookstore.service.user;

import com.bookstore.dto.user.UserRegistrationRequestDto;
import com.bookstore.dto.user.UserResponseDto;
import com.bookstore.exception.RegistrationException;
import com.bookstore.mapper.user.UserMapper;
import com.bookstore.model.Role;
import com.bookstore.model.RoleName;
import com.bookstore.model.User;
import com.bookstore.repository.role.RoleRepository;
import com.bookstore.repository.user.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("User with email "
            + requestDto.getEmail() + "already exist");
        }
        User savedUser = userMapper.toModel(requestDto);
        savedUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role userRole = roleRepository.findByRole(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        savedUser.setRoles(Set.of(userRole));
        userRepository.save(savedUser);
        return userMapper.modelToResponse(savedUser);
    }
}
