package com.example.fishlog.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FishingSpotForm {

    /** 編集時のみ値が入る(新規登録時はnull) */
    private Long spotId;

    @NotBlank(message = "釣り場名は必須です")
    @Size(max = 100, message = "釣り場名は100文字以内で入力してください")
    private String name;

    @Size(max = 20, message = "都道府県は20文字以内で入力してください")
    private String prefecture;

    @NotNull(message = "地図をクリックして位置を指定してください")
    private Double latitude;

    @NotNull(message = "地図をクリックして位置を指定してください")
    private Double longitude;

    @Size(max = 500, message = "メモは500文字以内で入力してください")
    private String memo;

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefecture() {
        return prefecture;
    }

    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
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
}
