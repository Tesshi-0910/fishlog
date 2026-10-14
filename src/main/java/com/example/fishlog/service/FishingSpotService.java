package com.example.fishlog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fishlog.entity.FishingSpot;
import com.example.fishlog.exception.ResourceNotFoundException;
import com.example.fishlog.form.FishingSpotForm;
import com.example.fishlog.repository.FishingSpotRepository;

@Service
public class FishingSpotService {

    private final FishingSpotRepository fishingSpotRepository;

    public FishingSpotService(FishingSpotRepository fishingSpotRepository) {
        this.fishingSpotRepository = fishingSpotRepository;
    }

    /** 全件取得(名前順)。地図表示・新規登録時のプルダウンで使用。 */
    public List<FishingSpot> findAll() {
        return fishingSpotRepository.findAllByOrderByNameAsc();
    }

    /** IDで1件取得。存在しなければ例外。 */
    public FishingSpot findById(Long id) {
        return fishingSpotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FishingSpot not found: id=" + id));
    }

    /** 新規登録(ユーザーが自分で追加する釣り場) */
    public void save(FishingSpotForm form) {
        FishingSpot entity = new FishingSpot();
        entity.setBuiltIn(false);
        applyForm(entity, form);
        fishingSpotRepository.save(entity);
    }

    /** 更新 */
    public void update(Long id, FishingSpotForm form) {
        FishingSpot entity = findById(id);
        applyForm(entity, form);
        fishingSpotRepository.save(entity);
    }

    /** 削除 */
    public void deleteById(Long id) {
        fishingSpotRepository.deleteById(id);
    }

    /**
     * 釣果登録時に「お気に入りの釣り場として登録する」が選ばれた場合に呼び出す。
     * 同名の釣り場がすでに登録されていれば何もしない(重複登録を避ける)。
     */
    public void addFavoriteIfMissing(String name, Double latitude, Double longitude) {
        if (name == null || name.isBlank() || latitude == null || longitude == null) {
            return;
        }
        if (fishingSpotRepository.existsByNameIgnoreCase(name)) {
            return;
        }
        FishingSpot spot = new FishingSpot();
        spot.setName(name);
        spot.setLatitude(latitude);
        spot.setLongitude(longitude);
        spot.setBuiltIn(false);
        spot.setMemo("釣果登録時にお気に入りとして追加");
        fishingSpotRepository.save(spot);
    }

    private void applyForm(FishingSpot entity, FishingSpotForm form) {
        entity.setName(form.getName());
        entity.setPrefecture(form.getPrefecture());
        entity.setLatitude(form.getLatitude());
        entity.setLongitude(form.getLongitude());
        entity.setMemo(form.getMemo());
    }
}
