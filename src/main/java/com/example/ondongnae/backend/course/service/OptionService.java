package com.example.ondongnae.backend.course.service;

import com.example.ondongnae.backend.course.dto.OptionAndMarketRequestDto;
import com.example.ondongnae.backend.course.model.Option;
import com.example.ondongnae.backend.course.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;

    public List<OptionAndMarketRequestDto> getOptionNames(String lang) {

        String language = lang == null ? "en" : lang.strip().toLowerCase();

        List<Option> allOptions = optionRepository.findAll();
        List<OptionAndMarketRequestDto> optionNameAndIdList = new ArrayList<>();

        for (Option o : allOptions) {
            String name = switch (language) {
                case "en" -> o.getNameEn();
                case "zh" ->  o.getNameZh();
                case "ja" -> o.getNameJa();
                default -> o.getNameEn();
            };

            optionNameAndIdList.add(OptionAndMarketRequestDto.builder().name(name).id(o.getId()).build());
        }

        return optionNameAndIdList;
    }

}
