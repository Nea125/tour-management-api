package com.istad.tourmanagementapi.featurs.configs;

import com.istad.tourmanagementapi.featurs.utils.ResourcePrefix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceHandlersConfig implements WebMvcConfigurer {
    @Value("${media.location}")
    private String mediaLocation;
    @Value("${media.client-path}")
    private String clientPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(clientPath + "/**")
                .addResourceLocations(ResourcePrefix.FILE_SYSTEM + mediaLocation);
    }
}