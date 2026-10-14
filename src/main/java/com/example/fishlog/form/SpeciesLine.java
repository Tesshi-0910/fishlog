package com.example.fishlog.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 1回の釣行でまとめて登録する、魚種ごとの明細行。
 * 「アジ 3匹、サバ 2匹」のように1回の登録で複数魚種を入力できるようにするための入れ子フォーム。
 */
public class SpeciesLine {

    @NotBlank(message = "魚種は必須です")
    @Size(max = 50, message = "魚種は50文字以内で入力してください")
    private String species;

    @NotNull(message = "尾数は必須です")
    @Min(value = 1, message = "尾数は1以上で入力してください")
    @Max(value = 9999, message = "尾数が大きすぎます")
    private Integer quantity = 1;

    /** 全長cm(任意入力。複数匹いる場合は代表的なサイズ) */
    private Double sizeCm;

    /** 重さg(任意入力。複数匹いる場合は代表的な重さ) */
    @Min(value = 0, message = "重さは0以上で入力してください")
    @Max(value = 1000000, message = "重さが大きすぎます")
    private Integer weightG;

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
}
