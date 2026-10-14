package com.example.fishlog.service;

import org.springframework.stereotype.Service;

import com.example.fishlog.entity.Catch;
import com.example.fishlog.form.CatchBatchForm;
import com.example.fishlog.form.SpeciesLine;
import com.example.fishlog.repository.CatchRepository;

/**
 * 1回の釣行(日時・場所・写真・メモは共通)で複数魚種をまとめて登録する処理を担当するサービス。
 * 釣果1件ごとの編集を扱う CatchService とは別クラスに分けている。
 */
@Service
public class CatchBatchService {

    private final CatchRepository catchRepository;
    private final PhotoStorageService photoStorageService;

    public CatchBatchService(CatchRepository catchRepository, PhotoStorageService photoStorageService) {
        this.catchRepository = catchRepository;
        this.photoStorageService = photoStorageService;
    }

    /** 写真は1回だけ保存し、入力された魚種の行数ぶんのCatchレコードを作成する。 */
    public void saveBatch(CatchBatchForm form) {
        String photoFilename = null;
        if (form.getPhoto() != null && !form.getPhoto().isEmpty()) {
            photoFilename = photoStorageService.store(form.getPhoto());
        }

        for (SpeciesLine line : form.getLines()) {
            Catch entity = new Catch();
            entity.setSpecies(line.getSpecies());
            entity.setQuantity(line.getQuantity());
            entity.setSizeCm(line.getSizeCm());
            entity.setWeightG(line.getWeightG());
            entity.setCaughtAt(form.getCaughtAt());
            entity.setLocationName(form.getLocationName());
            entity.setLatitude(form.getLatitude());
            entity.setLongitude(form.getLongitude());
            entity.setMemo(form.getMemo());
            entity.setPhotoFilename(photoFilename);
            catchRepository.save(entity);
        }
    }
}
