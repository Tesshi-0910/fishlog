package com.example.fishlog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fishlog.entity.FishingSpot;

public interface FishingSpotRepository extends JpaRepository<FishingSpot, Long> {

    List<FishingSpot> findAllByOrderByNameAsc();

    /** 初期データ(あらかじめ登録してある漁港)のみ削除する。ユーザーが自分で追加した釣り場は対象外。 */
    void deleteByBuiltInTrue();

    /** 同じ名前の釣り場がすでに登録されているか(大文字小文字を区別しない)。 */
    boolean existsByNameIgnoreCase(String name);
}
