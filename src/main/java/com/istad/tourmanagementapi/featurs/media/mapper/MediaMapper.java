package com.istad.tourmanagementapi.featurs.media.mapper;

import com.istad.tourmanagementapi.featurs.media.Media;
import com.istad.tourmanagementapi.featurs.media.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MediaMapper {

    private final MediaService mediaService;

    public String toUrl(Media media) {

        if (media == null) {
            return null;
        }

        return mediaService.getMediaUri(media);
    }
}