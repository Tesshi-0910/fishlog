package com.example.fishlog.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * 釣果記録(1回の釣りで釣った1匹分の記録)。
 */
@Entity
@Table(name = "catches")
public class Catch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 魚種 */
    @Column(name = "species", nullable = false, length = 50)
    private String species;

    /** 尾数(何匹釣れたか)。既存データへのカラム追加時にNOT NULL違反にならないようDB側デフォルトも設定。 */
    @Column(name = "quantity", nullable = false, columnDefinition = "integer default 1")
    private Integer quantity = 1;

    /** 釣れた日時 */
    @Column(name = "caught_at", nullable = false)
    private LocalDateTime caughtAt;

    /** 釣った場所の名称(例: 〇〇漁港、〇〇海岸) */
    @Column(name = "location_name", nullable = false, length = 100)
    private String locationName;

    /** 緯度(地図表示用。未入力可) */
    @Column(name = "latitude")
    private Double latitude;

    /** 経度(地図表示用。未入力可) */
    @Column(name = "longitude")
    private Double longitude;

    /** 全長(cm) */
    @Column(name = "size_cm")
    private Double sizeCm;

    /** 重さ(g) */
    @Column(name = "weight_g")
    private Integer weightG;

    /** 保存済み写真のファイル名(アップロードがない場合はnull) */
    @Column(name = "photo_filename", length = 255)
    private String photoFilename;

    /** メモ(仕掛け、ルアー、天候、潮など自由記述) */
    @Lob
    @Column(name = "memo")
    private String memo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ===== getter / setter =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhotoFilename() {
        return photoFilename;
    }

    public void setPhotoFilename(String photoFilename) {
        this.photoFilename = photoFilename;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean hasLocation() {
        return latitude != null && longitude != null;
    }
}
