package com.example.fishlog.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.fishlog.service.CatchService;
import com.example.fishlog.service.FishingSpotService;

/**
 * 地図表示(釣果マップ)に関するエンドポイントをまとめたコントローラー。
 * 釣果の一覧・登録は CatchController、釣り場の一覧・登録は FishingSpotController に分けている。
 */
@Controller
public class MapController {

    private final CatchService catchService;
    private final FishingSpotService fishingSpotService;

    public MapController(CatchService catchService, FishingSpotService fishingSpotService) {
        this.catchService = catchService;
        this.fishingSpotService = fishingSpotService;
    }

    /** 地図表示ページ (GET /catches/map) */
    @GetMapping("/catches/map")
    public String map() {
        return "catch/map";
    }

    /** 釣果の位置情報をJSONで返す (GET /catches/map/points) */
    @GetMapping("/catches/map/points")
    @ResponseBody
    public List<Map<String, Object>> catchPoints() {
        return catchService.findAllWithLocation().stream()
                .map(c -> Map.<String, Object>of(
                        "id", c.getId(),
                        "species", c.getSpecies(),
                        "quantity", c.getQuantity() == null ? 1 : c.getQuantity(),
                        "locationName", c.getLocationName(),
                        "latitude", c.getLatitude(),
                        "longitude", c.getLongitude(),
                        "caughtAt", c.getCaughtAt().toString(),
                        "sizeCm", c.getSizeCm() == null ? "" : c.getSizeCm(),
                        "photoFilename", c.getPhotoFilename() == null ? "" : c.getPhotoFilename()))
                .toList();
    }

    /** 漁港・釣り場の位置情報をJSONで返す (GET /spots/map/points) */
    @GetMapping("/spots/map/points")
    @ResponseBody
    public List<Map<String, Object>> spotPoints() {
        return fishingSpotService.findAll().stream()
                .map(s -> Map.<String, Object>of(
                        "id", s.getId(),
                        "name", s.getName(),
                        "prefecture", s.getPrefecture() == null ? "" : s.getPrefecture(),
                        "latitude", s.getLatitude(),
                        "longitude", s.getLongitude(),
                        "builtIn", s.isBuiltIn()))
                .toList();
    }
}
