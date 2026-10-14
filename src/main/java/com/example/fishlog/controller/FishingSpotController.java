package com.example.fishlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.fishlog.entity.FishingSpot;
import com.example.fishlog.form.FishingSpotForm;
import com.example.fishlog.service.FishingSpotService;

import jakarta.validation.Valid;

/**
 * 漁港・釣り場の一覧表示・新規登録・編集・削除を扱うコントローラー。
 * 地図表示は MapController に分けている。
 */
@Controller
@RequestMapping("/spots")
public class FishingSpotController {

    private final FishingSpotService fishingSpotService;

    public FishingSpotController(FishingSpotService fishingSpotService) {
        this.fishingSpotService = fishingSpotService;
    }

    /** 釣り場一覧 (GET /spots) */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("spots", fishingSpotService.findAll());
        return "spot/list";
    }

    /** 釣り場登録フォーム表示 (GET /spots/new) */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("spotForm", new FishingSpotForm());
        return "spot/form";
    }

    /** 釣り場登録実行 (POST /spots) */
    @PostMapping
    public String create(@Valid @ModelAttribute("spotForm") FishingSpotForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "spot/form";
        }
        fishingSpotService.save(form);
        return "redirect:/spots";
    }

    /** 釣り場編集フォーム表示 (GET /spots/{id}/edit) */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        FishingSpot entity = fishingSpotService.findById(id);

        FishingSpotForm form = new FishingSpotForm();
        form.setSpotId(entity.getId());
        form.setName(entity.getName());
        form.setPrefecture(entity.getPrefecture());
        form.setLatitude(entity.getLatitude());
        form.setLongitude(entity.getLongitude());
        form.setMemo(entity.getMemo());

        model.addAttribute("spotForm", form);
        return "spot/form";
    }

    /** 釣り場更新実行 (POST /spots/{id}) */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("spotForm") FishingSpotForm form, BindingResult result) {
        if (result.hasErrors()) {
            form.setSpotId(id);
            return "spot/form";
        }
        fishingSpotService.update(id, form);
        return "redirect:/spots";
    }

    /** 釣り場削除実行 (POST /spots/{id}/delete) */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        fishingSpotService.deleteById(id);
        return "redirect:/spots";
    }
}
