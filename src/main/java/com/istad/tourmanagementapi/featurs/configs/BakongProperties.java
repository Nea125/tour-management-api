package com.istad.tourmanagementapi.featurs.configs;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "bakong")
public class BakongProperties {

    private String baseUrl;

    private String token;

    private String bakongAccountId;

    private String merchantId;

    private String acquiringBank;

    private String merchantName;

    private String merchantCity;

    private String currency;
}