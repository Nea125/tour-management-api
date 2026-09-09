package com.istad.tourmanagementapi.featurs.utils;

import com.istad.tourmanagementapi.featurs.utils.PageResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PageMapper {
    // Can't create auto-generate mapping because Page is not an entity
    default PageResponse mapToPageResponse(Page<?> page) {
        PageResponse response = new PageResponse();

        response.setPageNumber(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());

        return response;
    }
}