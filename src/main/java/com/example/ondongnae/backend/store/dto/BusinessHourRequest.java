package com.example.ondongnae.backend.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessHourRequest {

    @NotNull
    @Size(min = 1, max = 7)
    private List<Item> items;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Item {

        // MON,TUE..
        @NotBlank
        private String day;

        // "HH:mm" 24시간 형식. closed=true면 null 허용
        @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "시간 형식은 HH:mm 입니다.")
        private String open;

        @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "시간 형식은 HH:mm 입니다.")
        private String close;

        @NotNull
        private Boolean closed;

    }
}
