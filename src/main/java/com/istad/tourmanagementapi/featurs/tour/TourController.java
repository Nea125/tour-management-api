package com.istad.tourmanagementapi.featurs.tour;
import com.istad.tourmanagementapi.featurs.media.dto.MediaResponse;
import com.istad.tourmanagementapi.featurs.tour.dto.CreateTourRequest;
import com.istad.tourmanagementapi.featurs.tour.dto.PopularTourResponse;
import com.istad.tourmanagementapi.featurs.tour.dto.TourResponse;
import com.istad.tourmanagementapi.featurs.tour.dto.UpdateTourRequest;
import com.istad.tourmanagementapi.featurs.utils.ApiResponse;
import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tours")
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    //CREATE TOUR
    public ApiResponse<TourResponse> create(
            @Valid @ModelAttribute CreateTourRequest request,
            @RequestPart("images") List<MultipartFile> images
    ) {
        return ApiResponse.<TourResponse>builder()
                .status(1)
                .message("Tour created successfully")
                .data(tourService.create(request, images)
                ).build();
    }

    // FIND TOUR BY ID
    @GetMapping("/{id}")
    public ApiResponse<TourResponse> findById(@PathVariable Long id) {
        return ApiResponse.<TourResponse>builder()
                .status(1)
                .message("Tour retrieved successfully")
                .data(tourService.findById(id))
                .build();
    }
// FIND ALL TOUR
    @GetMapping
    public ApiResponse<PageResponse<TourResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<TourResponse> tours = tourService.findAll(page, size);
        String message = tours.getItems().isEmpty()
                ? "No tours found"
                : "Tours retrieved successfully";
        return ApiResponse.<PageResponse<TourResponse>>builder()
                .status(1)
                .message(message)
                .data(tours)
                .build();
    }

    // SEARCH TOUR BY TITLE
    @GetMapping("/search")
    public ApiResponse<PageResponse<TourResponse>> search(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<TourResponse> tours = tourService.search(title, page, size);
        String message = tours.getItems().isEmpty()
                ? "No tours found"
                : "Tours retrieved successfully";

        return ApiResponse.<PageResponse<TourResponse>>builder()
                .status(1)
                .message(message)
                .data(tours)
                .build();
    }
// UPDATE TOUR
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<TourResponse> update(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateTourRequest request,
            @RequestParam(value = "imageIds", required = false)
            List<Integer> imageIds,
            @RequestParam(value = "images", required = false)
            List<MultipartFile> images
    ) {
        return ApiResponse.<TourResponse>builder()
                .status(1)
                .message("Tour updated successfully")
                .data(tourService.update(id, request, imageIds, images))
                .build();
    }

    // DELETE TOUR
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id
    ) {
        tourService.delete(id);
        return ApiResponse.<Void>builder()
                .status(1)
                .message("Tour deleted successfully")
                .build();
    }

    // FIND TOUR BY DESTINATION ID
    @GetMapping("/destination/{destinationId}")
    public ApiResponse<PageResponse<TourResponse>> findByDestinationId(
            @PathVariable Long destinationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<TourResponse> tours =
                tourService.findByDestinationId(destinationId, page, size);
        String message = tours.getItems().isEmpty()
                ? "No tours found for this destination"
                : "Tours retrieved successfully";

        return ApiResponse.<PageResponse<TourResponse>>builder()
                .status(1)
                .message(message)
                .data(tours)
                .build();
    }

    // GET TOUR IMAGE BY ID
    @GetMapping("/{tourId}/media/{imageId}")
    public ResponseEntity<Resource> getTourImage(
            @PathVariable Long tourId,
            @PathVariable Integer imageId
    ) {
        return tourService.getTourImage(tourId, imageId);
    }

    // UPDATE TOUR IMAGE BY ID
    @PutMapping(value = "/{tourId}/images/{imageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MediaResponse> updateTourImage(
            @PathVariable Long tourId,
            @PathVariable Integer imageId,
            @RequestPart("image") MultipartFile image
    ) {
        return ApiResponse.<MediaResponse>builder()
                .status(1)
                .message("Tour image updated successfully")
                .data(tourService.updateTourImage(tourId, imageId, image)
                )
                .build();
    }

    // GET POPULAR TOUR BASE ON RATTING AND REVIEW
    @GetMapping("/popular")
    public ApiResponse<List<PopularTourResponse>> findPopular() {

        List<PopularTourResponse> tours = tourService.findPopularTours();

        return ApiResponse.<List<PopularTourResponse>>builder()
                .status(1)
                .message(tours.isEmpty()
                        ? "No popular tours found"
                        : "Popular tours retrieved successfully")
                .data(tours)
                .build();
    }
}