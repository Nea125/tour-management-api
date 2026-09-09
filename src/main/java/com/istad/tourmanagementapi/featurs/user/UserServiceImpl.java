package com.istad.tourmanagementapi.featurs.user;

import com.istad.tourmanagementapi.featurs.user.dto.UserRequest;
import com.istad.tourmanagementapi.featurs.user.dto.UserResponse;
import com.istad.tourmanagementapi.featurs.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User already exists with email: " + request.email());
        }
        User user = userMapper.toEntity(request);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse findById(Long id) {
        return userMapper.toResponse(getById(id));
    }

    @Override
    public Page<UserResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Override
    public UserResponse update(Long id, UserRequest request) {
        User user = getById(id);
        userMapper.updateEntity(request, user);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        User user = getById(id);
        userRepository.delete(user);
    }

    private User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found with id: " + id));
    }
}
