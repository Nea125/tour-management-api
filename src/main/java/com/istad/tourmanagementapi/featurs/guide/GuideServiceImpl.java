package com.istad.tourmanagementapi.featurs.guide;

import com.istad.tourmanagementapi.featurs.guide.dto.GuideRequest;
import com.istad.tourmanagementapi.featurs.guide.dto.GuideResponse;
import com.istad.tourmanagementapi.featurs.guide.entity.Guide;
import com.istad.tourmanagementapi.featurs.user.UserRepository;
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
public class GuideServiceImpl implements GuideService {

    private final GuideRepository guideRepository;
    private final GuideMapper guideMapper;
    private final UserRepository userRepository;

    @Override
    public GuideResponse create(GuideRequest request) {
        Guide guide = guideMapper.toEntity(request);
        guide.setUser(getUserById(request.userId()));
        return guideMapper.toResponse(guideRepository.save(guide));
    }

    @Override
    public GuideResponse findById(Long id) {
        return guideMapper.toResponse(getById(id));
    }

    @Override
    public Page<GuideResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return guideRepository.findAll(pageable).map(guideMapper::toResponse);
    }

    @Override
    public GuideResponse update(Long id, GuideRequest request) {
        Guide guide = getById(id);
        guideMapper.updateEntity(request, guide);
        if (request.userId() != null) {
            guide.setUser(getUserById(request.userId()));
        }
        return guideMapper.toResponse(guideRepository.save(guide));
    }

    @Override
    public void delete(Long id) {
        Guide guide = getById(id);
        guideRepository.delete(guide);
    }

    private Guide getById(Long id) {
        return guideRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Guide not found with id: " + id));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found with id: " + userId));
    }
}
