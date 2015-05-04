package br.ufg.inf.dosador.entidades;

import java.io.Serializable;

/**
 * Created by Maycon on 26/03/2015.
 */
public class Alimento implements Serializable {

    private int food_id;
    private String food_name;
    private String food_type;
    private String food_url;
    private String brand_name;
    private String food_description;


    private String serving_id;
    private String serving_description;
    private String serving_url;
    private String metric_serving_amount;
    private String metric_serving_unit;
    private String number_of_units;
    private String measurement_description;

    private Double calories;
    private Double carbohydrate;
    private Double protein;
    private Double fat;
    private Double saturated_fat;
    private Double polyunsaturated_fat;
    private Double monounsaturated_fat;
    private Double trans_fat;
    private Double cholesterol;
    private Double sodium;
    private Double potassium;
    private Double fiber;
    private Double sugar;
    private Double vitamin_a;
    private Double vitamin_c;
    private Double calcium;
    private Double iron;
//    private int quantidade;
//
//    public int getQuantidade() {
//        return quantidade;
//    }
//
//    public void setQuantidade(int quantidade) {
//        this.quantidade = quantidade;
//    }

    public String getFood_description() {
        return food_description;
    }

    public void setFood_description(String food_description) {
        this.food_description = food_description;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getFood_url() {
        return food_url;
    }

    public void setFood_url(String food_url) {
        this.food_url = food_url;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getServing_id() {
        return serving_id;
    }

    public void setServing_id(String serving_id) {
        this.serving_id = serving_id;
    }

    public String getServing_description() {
        return serving_description;
    }

    public void setServing_description(String serving_description) {
        this.serving_description = serving_description;
    }

    public String getServing_url() {
        return serving_url;
    }

    public void setServing_url(String serving_url) {
        this.serving_url = serving_url;
    }

    public String getMetric_serving_amount() {
        return metric_serving_amount;
    }

    public void setMetric_serving_amount(String metric_serving_amount) {
        this.metric_serving_amount = metric_serving_amount;
    }

    public String getMetric_serving_unit() {
        return metric_serving_unit;
    }

    public void setMetric_serving_unit(String metric_serving_unit) {
        this.metric_serving_unit = metric_serving_unit;
    }

    public String getNumber_of_units() {
        return number_of_units;
    }

    public void setNumber_of_units(String number_of_units) {
        this.number_of_units = number_of_units;
    }

    public String getMeasurement_description() {
        return measurement_description;
    }

    public void setMeasurement_description(String measurement_description) {
        this.measurement_description = measurement_description;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(Double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getSaturated_fat() {
        return saturated_fat;
    }

    public void setSaturated_fat(Double saturated_fat) {
        this.saturated_fat = saturated_fat;
    }

    public Double getPolyunsaturated_fat() {
        return polyunsaturated_fat;
    }

    public void setPolyunsaturated_fat(Double polyunsaturated_fat) {
        this.polyunsaturated_fat = polyunsaturated_fat;
    }

    public Double getMonounsaturated_fat() {
        return monounsaturated_fat;
    }

    public void setMonounsaturated_fat(Double monounsaturated_fat) {
        this.monounsaturated_fat = monounsaturated_fat;
    }

    public Double getTrans_fat() {
        return trans_fat;
    }

    public void setTrans_fat(Double trans_fat) {
        this.trans_fat = trans_fat;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Double getPotassium() {
        return potassium;
    }

    public void setPotassium(Double potassium) {
        this.potassium = potassium;
    }

    public Double getSodium() {
        return sodium;
    }

    public void setSodium(Double sodium) {
        this.sodium = sodium;
    }

    public Double getFiber() {
        return fiber;
    }

    public void setFiber(Double fiber) {
        this.fiber = fiber;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public Double getVitamin_a() {
        return vitamin_a;
    }

    public void setVitamin_a(Double vitamin_a) {
        this.vitamin_a = vitamin_a;
    }

    public Double getCalcium() {
        return calcium;
    }

    public void setCalcium(Double calcium) {
        this.calcium = calcium;
    }

    public Double getVitamin_c() {
        return vitamin_c;
    }

    public void setVitamin_c(Double vitamin_c) {
        this.vitamin_c = vitamin_c;
    }

    public Double getIron() {
        return iron;
    }

    public void setIron(Double iron) {
        this.iron = iron;
    }

}
