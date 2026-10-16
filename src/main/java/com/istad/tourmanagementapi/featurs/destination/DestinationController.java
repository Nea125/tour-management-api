package com.istad.tourmanagementapi.featurs.destination;

import com.istad.tourmanagementapi.featurs.destination.dto.CreateDestinationRequest;
import com.istad.tourmanagementapi.featurs.destination.dto.DestinationResponse;
import com.istad.tourmanagementapi.featurs.destination.dto.UpdateDestinationRequest;
import com.istad.tourmanagementapi.featurs.media.dto.MediaResponse;
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
@RequestMapping("/api/v1/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    //    Used @RequestBody when the data comes from the HTTP request body, usually as JSON.
    //    Used @RequestParam when data comes as a request parameter, commonly from query parameters or form-data.
    //    JSON object → @RequestBody
    //    Query/form parameter → @RequestParam
    //    multipart/form-data text fields → @ModelAttribute
    //    Uploaded files → @RequestParam MultipartFile

     // CREATE DESTINATION
    public ApiResponse<DestinationResponse> create(
            @Valid @ModelAttribute CreateDestinationRequest request,
            @RequestParam(value = "images", required = false)
            List<MultipartFile> images
    ) {

        return ApiResponse.<DestinationResponse>builder()
                .status(1)
                .message("Destination created successfully")
                .data(destinationService.create(request, images))
                .build();
    }

    // FIND DESTINATION BY ID
    @GetMapping("/{id}")
    public ApiResponse<DestinationResponse> findById(@PathVariable Long id) {

        return ApiResponse.<DestinationResponse>builder()
                .status(1)
                .message("Destination retrieved successfully")
                .data(
                        destinationService.findById(id))
                .build();
    }

// SEARCH DESTINATION BY NAME
    @GetMapping("/search")
    public ApiResponse<PageResponse<DestinationResponse>> search(

            @RequestParam String name,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "10")
            int size
    ) {

        PageResponse<DestinationResponse> destinations = destinationService.search(name, page, size);
        String message = destinations.getItems().isEmpty()
                        ? "No destinations found"
                        : "Destinations retrieved successfully";

        return ApiResponse
                .<PageResponse<DestinationResponse>>builder()
                .status(1)
                .message(message)
                .data(destinations)
                .build();
    }

// GET ALL DESTINATION
    @GetMapping
    public ApiResponse<PageResponse<DestinationResponse>> findAll(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        PageResponse<DestinationResponse> destinations = destinationService.findAll(page, size);

        String message =
                destinations.getItems().isEmpty()
                        ? "No destinations found"
                        : "Destinations retrieved successfully";

        return ApiResponse
                .<PageResponse<DestinationResponse>>builder()
                .status(1)
                .message(message)
                .data(destinations)
                .build();
    }

// UPDATE DESTINATION
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<DestinationResponse> update(
            @PathVariable Long id,
            @ModelAttribute UpdateDestinationRequest request,
            @RequestParam(value = "imageIds", required = false)
            List<Integer> imageIds,
            @RequestParam(value = "images", required = false)
            List<MultipartFile> images
    ) {
        return ApiResponse.<DestinationResponse>builder()
                .status(1)
                .message("Destination updated successfully")
                .data(destinationService.update(id, request, imageIds, images))
                .build();
    }

// DELETE DESTINATION
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {

        destinationService.delete(id);
        return ApiResponse.<Void>builder()
                .status(1)
                .message("Destination deleted successfully")
                .build();
    }

// GET DESTINATION IMAGE
    @GetMapping("/{destinationId}/media/{imageId}")
    public ResponseEntity<Resource> getDestinationImage(
            @PathVariable Long destinationId,
            @PathVariable Integer imageId
    ) {

        return destinationService.getDestinationImage(destinationId, imageId);
    }

// UPDATE DESTINATION IMAGE
    @PutMapping(value = "/{destinationId}/images/{imageId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<MediaResponse> updateDestinationImage(
            @PathVariable Long destinationId,
            @PathVariable Integer imageId,
            @RequestPart("image") MultipartFile image
    ) {

        return ApiResponse.<MediaResponse>builder()
                .status(1)
                .message("Image updated successfully")
                .data(destinationService.updateDestinationImage(destinationId, imageId, image))
                .build();
    }
}
