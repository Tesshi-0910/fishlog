package com.example.fishlog.form;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CatchForm {

    public static final DateTimeFormatter DATETIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /** 編集時のみ値が入る(新規登録時はnull) */
    private Long catchId;

    @NotBlank(message = "魚種は必須です")
    @Size(max = 50, message = "魚種は50文字以内で入力してください")
    private String species;

    @NotNull(message = "尾数は必須です")
    @Min(value = 1, message = "尾数は1以上で入力してください")
    @Max(value = 9999, message = "尾数が大きすぎます")
    private Integer quantity = 1;

    @NotNull(message = "釣れた日時は必須です")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime caughtAt;

    @NotBlank(message = "場所は必須です")
    @Size(max = 100, message = "場所は100文字以内で入力してください")
    private String locationName;

    /** 緯度(-90〜90の想定。任意入力) */
    private Double latitude;

    /** 経度(-180〜180の想定。任意入力) */
    private Double longitude;

    /** 全長cm(任意入力) */
    private Double sizeCm;

    @Min(value = 0, message = "重さは0以上で入力してください")
    @Max(value = 1000000, message = "重さが大きすぎます")
    private Integer weightG;

    @Size(max = 2000, message = "メモは2000文字以内で入力してください")
    private String memo;

    /** アップロードされた写真(未選択の場合は空ファイル) */
    private MultipartFile photo;

    /** 編集時に既存の写真を削除したい場合にtrue */
    private boolean removePhoto;

    // ===== getter / setter =====

    public Long getCatchId() {
        return catchId;
    }

    public void setCatchId(Long catchId) {
        this.catchId = catchId;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

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

    public Double getSizeCm() {
        return sizeCm;
    }

    public void setSizeCm(Double sizeCm) {
        this.sizeCm = sizeCm;
    }

    public Integer getWeightG() {
        return weightG;
    }

    public void setWeightG(Integer weightG) {
        this.weightG = weightG;
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

    public boolean isRemovePhoto() {
        return removePhoto;
    }

    public void setRemovePhoto(boolean removePhoto) {
        this.removePhoto = removePhoto;
    }
}
