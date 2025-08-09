package com.example.ondongnae.backend.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDetailResponse {

    private Header header;

    private List<MenuItem> menuTab;

    private Info infoTab;

    private MapPoint map;

    /* ---------------------- nested DTOs ---------------------- */

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Header {
        private List<String> images;

        private String name;

        private String nameKo;

        private Status status;

        private List<WeeklyHour> weeklyHours;

        private String shortIntro;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Status {
        private boolean isOpen;

        private String todayOpenTime;

        private String todayCloseTime;

        private boolean todayClosed;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WeeklyHour {
        private String day;

        private String open;

        private String close;

        private boolean closed;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MenuItem {
        private String name;

        private int priceKrw;

        private List<String> allergies;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Info {
        private String longIntro;

        private String phone;

        private String address;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MapPoint {
        private double lat;

        private double lng;
    }
}
