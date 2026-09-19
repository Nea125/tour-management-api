package com.istad.tourmanagementapi.featurs.media;

import com.istad.tourmanagementapi.featurs.media.dto.MediaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.core.io.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaServiceImpl implements MediaService {

    @Value("${media.location}")
    private String mediaLocation;

    @Value("${media.client-path}")
    private String mediaClientPath;
    @Value("${media.base-uri}")
    private String mediaBaseUri;
    private final MediaRepository mediaRepository;

    private static final String MB = "MB";

    @Override
    @Transactional
    public MediaResponse uploadMedia(MultipartFile file) {

        Media media = uploadMediaEntity(file);

        return buildMediaResponse(media);
    }

    @Override
    @Transactional
    public List<MediaResponse> uploadMedia(List<MultipartFile> files) {

        return files.stream()
                .map(this::uploadMediaEntity)
                .map(this::buildMediaResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<Media> uploadMediaEntities(List<MultipartFile> files) {

        return files.stream()
                .map(this::uploadMediaEntity)
                .toList();
    }

    private Media uploadMediaEntity(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "File cannot be empty"
            );
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid file"
            );
        }

        // Generate unique file name
        String name = UUID.randomUUID().toString();

        // Get file extension
        int lastIndexOf = originalFilename.lastIndexOf(".");

        String extension = originalFilename.substring(lastIndexOf + 1);

        // Create file path
        Path path = Paths.get(mediaLocation + name + "." + extension
        );

        log.info("Uploading media location: {}", path);

        // Copy file to file system
        try {

            Files.createDirectories(path.getParent());

            Files.copy(file.getInputStream(), path);

        } catch (IOException e) {

            log.error("Failed to upload media", e);

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to upload media"
            );
        }

        // Save media information to database
        Media media = new Media();

        media.setName(name);
        media.setExtension(extension);
        media.setFileSize(file.getSize());
        media.setMediaType(file.getContentType());
        media.setCreatedAt(LocalDateTime.now());

        mediaRepository.save(media);

        log.info(
                "Media uploaded successfully: {}",
                media.getFileSize()
        );

        return media;
    }
    @Override
    @Transactional(readOnly = true)
    public Resource getMediaResource(Integer mediaId) {

        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Media not found with id: " + mediaId
                        )
                );

        Path path = Paths.get(
                mediaLocation,
                media.getName() + "." + media.getExtension()
        );

        Resource resource = new FileSystemResource(path);

        if (!resource.exists() || !resource.isReadable()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Media file not found"
            );
        }

        return resource;
    }


    @Override
    public MediaResponse getMediaResponse(Media media) {
        return buildMediaResponse(media);
    }

    private MediaResponse buildMediaResponse(Media media) {

        return MediaResponse.builder()
                .id(media.getId())
                .name(media.getName())
                .extension(media.getExtension())
                .mediaType(media.getMediaType())
                .size(media.getFileSize())
                .measurement(MB)
                .createdAt(media.getCreatedAt())
                .uri(buildMediaUri(media))
                .build();
    }

    @Override
    @Transactional
    public MediaResponse updateMedia(
            Integer mediaId,
            MultipartFile image
    ) {

        if (image == null || image.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Image cannot be empty"
            );
        }

        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Media not found with id: " + mediaId
                        )
                );

        String originalFilename = image.getOriginalFilename();

        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid file"
            );
        }

        String oldFileName =
                media.getName() + "." + media.getExtension();

        Path oldPath = Paths.get(
                mediaLocation,
                oldFileName
        );

        String extension = originalFilename.substring(
                originalFilename.lastIndexOf(".") + 1
        );

        String newFileName =
                media.getName() + "." + extension;

        Path newPath = Paths.get(
                mediaLocation,
                newFileName
        );

        try {

            // Delete old physical file
            Files.deleteIfExists(oldPath);

            // Save new file using the SAME media ID/name
            Files.copy(
                    image.getInputStream(),
                    newPath
            );

        } catch (IOException e) {

            log.error("Failed to update media", e);

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to update media"
            );
        }

        // Update existing Media record
        media.setExtension(extension);
        media.setFileSize(image.getSize());
        media.setMediaType(image.getContentType());

        mediaRepository.save(media);

        return buildMediaResponse(media);
    }


    private String buildMediaUri(Media media) {

        return mediaBaseUri
                + mediaClientPath
                + "/"
                + media.getName()
                + "."
                + media.getExtension();
    }

}