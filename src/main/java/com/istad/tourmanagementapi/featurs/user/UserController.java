package com.istad.tourmanagementapi.featurs.user;

import com.istad.tourmanagementapi.featurs.user.dto.UserRequest;
import com.istad.tourmanagementapi.featurs.user.dto.UserResponse;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PageMapper pageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> create(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("User created successfully")
                .data(userService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> findById(@PathVariable Long id) {
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("User retrieved successfully")
                .data(userService.findById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<UserResponse> result = userService.findAll(page, size);
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Users retrieved successfully")
                .data(result.getContent())
                .pagination(pageMapper.mapToPageResponse(result))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> update(@PathVariable Long id,
                                            @RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("User updated successfully")
                .data(userService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("User deleted successfully")
                .build();
    }
}
