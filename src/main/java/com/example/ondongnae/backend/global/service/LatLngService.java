package com.example.ondongnae.backend.global.service;

import com.example.ondongnae.backend.global.dto.LatLngResponseDto;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LatLngService {

    @Value("${GOOGLE_MAP_API_KEY}")
    private String GOOGLE_MAP_API_KEY;

    public LatLngResponseDto getLatLngByAddress(String address) {
        try {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(GOOGLE_MAP_API_KEY).build();

            GeocodingResult[] results = GeocodingApi.geocode(context, address)
                    .region("kr").await();
            if (results.length > 0) {
                GeocodingResult geocodingResult = results[0];
                Double lat = geocodingResult.geometry.location.lat;
                Double lng = geocodingResult.geometry.location.lng;
                return LatLngResponseDto.builder().lat(lat).lng(lng).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
