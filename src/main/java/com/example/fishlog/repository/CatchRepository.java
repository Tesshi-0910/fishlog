package com.example.fishlog.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fishlog.entity.Catch;

public interface CatchRepository extends JpaRepository<Catch, Long> {

    /**
     * 魚種・場所(部分一致)・期間で絞り込み検索する。
     * 各パラメータがnullの場合はその条件を無視する。
     */
    @Query("SELECT c FROM Catch c WHERE "
            + "(:species IS NULL OR c.species LIKE %:species%) AND "
            + "(:location IS NULL OR c.locationName LIKE %:location%) AND "
            + "(:from IS NULL OR c.caughtAt >= :from) AND "
            + "(:to IS NULL OR c.caughtAt <= :to) "
            + "ORDER BY c.caughtAt DESC")
    List<Catch> search(@Param("species") String species,
            @Param("location") String location,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    /** 地図表示用。緯度経度が登録されている釣果のみ取得する。 */
    @Query("SELECT c FROM Catch c WHERE c.latitude IS NOT NULL AND c.longitude IS NOT NULL ORDER BY c.caughtAt DESC")
    List<Catch> findAllWithLocation();

    /** 魚種の一覧(重複なし)。絞り込みフォームのプルダウン用。 */
    @Query("SELECT DISTINCT c.species FROM Catch c ORDER BY c.species")
    List<String> findDistinctSpecies();
}
