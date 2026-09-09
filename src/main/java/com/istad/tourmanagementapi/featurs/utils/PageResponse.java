package com.istad.tourmanagementapi.featurs.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data // use this data annotation to create
/*
 * @Getter
 * @Setter
 * @ToString
 * @EqualsAndHashCode
 * @RequiredArgsConstructor
 */
@Builder // Use this annotation to make possible use builder in service
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private int size;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
}