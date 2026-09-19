package com.istad.tourmanagementapi.featurs.media;

import com.istad.tourmanagementapi.featurs.media.dto.MediaResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {

    List<Media> uploadMediaEntities(List<MultipartFile> files);
    Resource getMediaResource(Integer mediaId);
    MediaResponse updateMedia(Integer mediaId, MultipartFile image);
    Media uploadMediaEntity(MultipartFile file);
    String getMediaUri(Media media);
}