package com.example.fishlog.form;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 釣果の新規登録フォーム。
 * 1回の釣行(日時・場所・写真・メモは共通)につき、複数の魚種(species行)をまとめて登録できる。
 * 保存時には行ごとに1件ずつ Catch レコードが作成される。
 */
public class CatchBatchForm {

    @NotNull(message = "釣れた日時は必須です")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime caughtAt;

    @NotBlank(message = "場所は必須です")
    @Size(max = 100, message = "場所は100文字以内で入力してください")
    private String locationName;

    /** 緯度(地図クリックで自動入力。任意) */
    private Double latitude;

    /** 経度(地図クリックで自動入力。任意) */
    private Double longitude;

    @Size(max = 2000, message = "メモは2000文字以内で入力してください")
    private String memo;

    /** その釣行で撮った写真(全ての魚種行で共通) */
    private MultipartFile photo;

    /** 入力した場所をお気に入りの釣り場として登録するか(位置が設定されている場合のみ有効) */
    private boolean saveAsFavoriteSpot;

    @NotEmpty(message = "魚種を1件以上入力してください")
    @Valid
    private List<SpeciesLine> lines = new ArrayList<>(List.of(new SpeciesLine()));

    public LocalDateTime getCaughtAt() {
        return caughtAt;
    }

    public void setCaughtAt(LocalDateTime caughtAt) {
        this.caughtAt = caughtAt;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public boolean isSaveAsFavoriteSpot() {
        return saveAsFavoriteSpot;
    }

    public void setSaveAsFavoriteSpot(boolean saveAsFavoriteSpot) {
        this.saveAsFavoriteSpot = saveAsFavoriteSpot;
    }

    public List<SpeciesLine> getLines() {
        return lines;
    }

    public void setLines(List<SpeciesLine> lines) {
        this.lines = lines;
    }
}
