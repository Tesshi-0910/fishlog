package com.example.fishlog.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.fishlog.entity.FishingSpot;
import com.example.fishlog.repository.FishingSpotRepository;

/**
 * アプリ起動時、九州地方の有名な漁港を初期データとして登録する。
 * あらかじめ登録してあるデータ(builtIn=true)のみ毎回入れ替える。
 * ユーザーが自分で追加した釣り場(builtIn=false)は削除・変更しない。
 *
 * 緯度・経度は各漁港がある市区町村の代表的な位置を示す目安値であり、
 * 桟橋単位の正確な測量値ではない(サンプルの位置づけ)。
 */
@Component
public class FishingSpotSeeder implements CommandLineRunner {

    private final FishingSpotRepository fishingSpotRepository;

    public FishingSpotSeeder(FishingSpotRepository fishingSpotRepository) {
        this.fishingSpotRepository = fishingSpotRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        fishingSpotRepository.deleteByBuiltInTrue();

        // ---- 福岡県 ----
        addSpot("博多漁港", "福岡県", 33.6064, 130.4181);

        // ---- 佐賀県 ----
        addSpot("唐津漁港", "佐賀県", 33.4499, 129.9692);
        addSpot("呼子漁港", "佐賀県", 33.5508, 129.9183);

        // ---- 長崎県 ----
        addSpot("長崎漁港", "長崎県", 32.7503, 129.8779);
        addSpot("松浦漁港", "長崎県", 33.3597, 129.7156);
        addSpot("佐世保漁港", "長崎県", 33.1595, 129.7236);
        addSpot("五島(福江)漁港", "長崎県", 32.6964, 128.8406);
        addSpot("対馬(厳原)漁港", "長崎県", 34.2003, 129.2892);

        // ---- 熊本県 ----
        addSpot("牛深漁港", "熊本県", 32.1908, 130.0264);
        addSpot("天草漁港", "熊本県", 32.4578, 130.2017);
        addSpot("八代漁港", "熊本県", 32.5064, 130.6033);

        // ---- 大分県 ----
        addSpot("佐伯漁港", "大分県", 32.9597, 131.9008);
        addSpot("臼杵漁港", "大分県", 33.1225, 131.8075);
        addSpot("別府漁港", "大分県", 33.2846, 131.4914);

        // ---- 宮崎県 ----
        addSpot("細島漁港", "宮崎県", 32.4186, 131.6372);
        addSpot("油津漁港", "宮崎県", 31.5811, 131.3989);

        // ---- 鹿児島県 ----
        addSpot("鹿児島漁港", "鹿児島県", 31.5602, 130.5581);
        addSpot("串木野漁港", "鹿児島県", 31.7117, 130.3011);
        addSpot("枕崎漁港", "鹿児島県", 31.2719, 130.2975);
        addSpot("山川漁港", "鹿児島県", 31.1964, 130.6425);
    }

    private void addSpot(String name, String prefecture, double lat, double lng) {
        FishingSpot spot = new FishingSpot();
        spot.setName(name);
        spot.setPrefecture(prefecture);
        spot.setLatitude(lat);
        spot.setLongitude(lng);
        spot.setBuiltIn(true);
        spot.setMemo("初期データ(位置は市区町村レベルの目安)");
        fishingSpotRepository.save(spot);
    }
}
