package com.example.ondongnae.backend.market.service;

import com.example.ondongnae.backend.course.dto.OptionAndMarketRequestDto;
import com.example.ondongnae.backend.market.model.Market;
import com.example.ondongnae.backend.market.repository.MarketRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarketService {

    private final MarketRepository marketRepository;

    public MarketService(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    public List<OptionAndMarketRequestDto> getMarketNames(String lang) {

        String language = lang == null ? "en" : lang.strip().toLowerCase();

        List<Market> allMarket = marketRepository.findAll();
        List<OptionAndMarketRequestDto> marketNameAndIdList = new ArrayList<>();

        for (Market m : allMarket) {
            String name = switch (language) {
                case "en" -> m.getNameEn();
                case "zh" -> m.getNameZh();
                case "ja" -> m.getNameJa();
                default -> m.getNameEn();
            };
            marketNameAndIdList.add(OptionAndMarketRequestDto.builder().id(m.getId()).name(name).build());
        }
        return marketNameAndIdList;
    }
}
