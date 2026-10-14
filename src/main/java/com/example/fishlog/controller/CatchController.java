package com.example.fishlog.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.fishlog.entity.Catch;
import com.example.fishlog.form.CatchBatchForm;
import com.example.fishlog.form.CatchForm;
import com.example.fishlog.service.CatchBatchService;
import com.example.fishlog.service.CatchService;
import com.example.fishlog.service.FishingSpotService;

import jakarta.validation.Valid;

/**
 * 釣果の一覧表示・新規登録(まとめ登録)・編集・削除を扱うコントローラー。
 * 地図表示は MapController、釣り場の管理は FishingSpotController に分けている。
 */
@Controller
@RequestMapping("/catches")
public class CatchController {

    private final CatchService catchService;
    private final CatchBatchService catchBatchService;
    private final FishingSpotService fishingSpotService;

    public CatchController(CatchService catchService, CatchBatchService catchBatchService,
            FishingSpotService fishingSpotService) {
        this.catchService = catchService;
        this.catchBatchService = catchBatchService;
        this.fishingSpotService = fishingSpotService;
    }

    /** 釣果一覧・検索 (GET /catches) */
    @GetMapping
    public String list(@RequestParam(required = false) String species,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to,
            Model model) {
        LocalDateTime fromDateTime = (from == null) ? null : from.atStartOfDay();
        LocalDateTime toDateTime = (to == null) ? null : LocalDateTime.of(to, LocalTime.of(23, 59, 59));
        List<Catch> catches = catchService.search(species, location, fromDateTime, toDateTime);
        model.addAttribute("catches", catches);
        model.addAttribute("speciesList", catchService.findDistinctSpecies());
        model.addAttribute("selectedSpecies", species);
        model.addAttribute("selectedLocation", location);
        model.addAttribute("selectedFrom", from);
        model.addAttribute("selectedTo", to);
        model.addAttribute("totalCount", catches.size());
        return "catch/list";
    }

    /** 釣果登録フォーム表示 (GET /catches/new)。1回の釣行で複数魚種をまとめて登録できる。 */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("catchBatchForm", new CatchBatchForm());
        model.addAttribute("spots", fishingSpotService.findAll());
        return "catch/new";
    }

    /** 釣果登録実行 (POST /catches)。入力された魚種の行数ぶん一括で登録する。 */
    @PostMapping
    public String create(@Valid @ModelAttribute("catchBatchForm") CatchBatchForm form, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("spots", fishingSpotService.findAll());
            return "catch/new";
        }
        catchBatchService.saveBatch(form);
        if (form.isSaveAsFavoriteSpot()) {
            fishingSpotService.addFavoriteIfMissing(form.getLocationName(), form.getLatitude(), form.getLongitude());
        }
        return "redirect:/catches";
    }

    /** 釣果編集フォーム表示 (GET /catches/{id}/edit) */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Catch entity = catchService.findById(id);

        CatchForm form = new CatchForm();
        form.setCatchId(entity.getId());
        form.setSpecies(entity.getSpecies());
        form.setQuantity(entity.getQuantity());
        form.setCaughtAt(entity.getCaughtAt());
        form.setLocationName(entity.getLocationName());
        form.setLatitude(entity.getLatitude());
        form.setLongitude(entity.getLongitude());
        form.setSizeCm(entity.getSizeCm());
        form.setWeightG(entity.getWeightG());
        form.setMemo(entity.getMemo());

        model.addAttribute("catchForm", form);
        model.addAttribute("existingPhotoFilename", entity.getPhotoFilename());
        return "catch/form";
    }

    /** 釣果更新実行 (POST /catches/{id}) */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("catchForm") CatchForm form, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            form.setCatchId(id);
            model.addAttribute("existingPhotoFilename", catchService.findById(id).getPhotoFilename());
            return "catch/form";
        }
        catchService.update(id, form);
        return "redirect:/catches";
    }

    /** 釣果削除実行 (POST /catches/{id}/delete) */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        catchService.deleteById(id);
        return "redirect:/catches";
    }
}
