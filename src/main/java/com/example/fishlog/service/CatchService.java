package com.example.fishlog.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fishlog.entity.Catch;
import com.example.fishlog.exception.ResourceNotFoundException;
import com.example.fishlog.form.CatchForm;
import com.example.fishlog.repository.CatchRepository;

/**
 * 釣果1件ごとの登録・検索・更新・削除を担当するサービス。
 * 複数魚種のまとめ登録は CatchBatchService、写真の保存・削除は PhotoStorageService に分けている。
 */
@Service
public class CatchService {

    private final CatchRepository catchRepository;
    private final PhotoStorageService photoStorageService;

    public CatchService(CatchRepository catchRepository, PhotoStorageService photoStorageService) {
        this.catchRepository = catchRepository;
        this.photoStorageService = photoStorageService;
    }

    /** 全件取得(登録日時の新しい順) */
    public List<Catch> findAll() {
        return catchRepository.findAll();
    }

    /** 魚種・場所・期間で絞り込み検索。各条件はnull/空の場合は無視される。 */
    public List<Catch> search(String species, String location, LocalDateTime from, LocalDateTime to) {
        String s = (species == null || species.isBlank()) ? null : species;
        String l = (location == null || location.isBlank()) ? null : location;
        return catchRepository.search(s, l, from, to);
    }

    /** 地図表示用。位置情報が登録されている釣果のみ返す。 */
    public List<Catch> findAllWithLocation() {
        return catchRepository.findAllWithLocation();
    }

    /** 絞り込みフォーム用の魚種候補一覧。 */
    public List<String> findDistinctSpecies() {
        return catchRepository.findDistinctSpecies();
    }

    /** IDで1件取得。存在しなければ例外。 */
    public Catch findById(Long id) {
        return catchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catch not found: id=" + id));
    }

    /** 新規登録。写真が添付されていれば保存する。 */
    public void save(CatchForm form) {
        Catch entity = new Catch();
        applyForm(entity, form);
        catchRepository.save(entity);
    }

    /** 更新。写真の差し替え・削除にも対応する。 */
    public void update(Long id, CatchForm form) {
        Catch entity = findById(id);
        String oldPhoto = entity.getPhotoFilename();
        applyForm(entity, form);
        if ((form.isRemovePhoto() || hasNewPhoto(form)) && oldPhoto != null && !oldPhoto.equals(entity.getPhotoFilename())) {
            photoStorageService.delete(oldPhoto);
        }
        catchRepository.save(entity);
    }

    /** 削除。写真ファイルも一緒に削除する。 */
    public void deleteById(Long id) {
        Catch entity = findById(id);
        if (entity.getPhotoFilename() != null) {
            photoStorageService.delete(entity.getPhotoFilename());
        }
        catchRepository.deleteById(id);
    }

    private void applyForm(Catch entity, CatchForm form) {
        entity.setSpecies(form.getSpecies());
        entity.setQuantity(form.getQuantity());
        entity.setCaughtAt(form.getCaughtAt());
        entity.setLocationName(form.getLocationName());
        entity.setLatitude(form.getLatitude());
        entity.setLongitude(form.getLongitude());
        entity.setSizeCm(form.getSizeCm());
        entity.setWeightG(form.getWeightG());
        entity.setMemo(form.getMemo());

        if (form.isRemovePhoto()) {
            entity.setPhotoFilename(null);
        }
        if (hasNewPhoto(form)) {
            entity.setPhotoFilename(photoStorageService.store(form.getPhoto()));
        }
    }

    private boolean hasNewPhoto(CatchForm form) {
        return form.getPhoto() != null && !form.getPhoto().isEmpty();
    }
}
