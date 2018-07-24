package de.kruemelnerd.jaha.addNewPayment.onlineBarcodeDatabase.model;

import com.google.gson.annotations.SerializedName;

public class ResultsItem {

    @SerializedName("languageCode")
    private String languageCode;

    @SerializedName("brand")
    private Object brand;

    @SerializedName("productName")
    private String productName;

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setBrand(Object brand) {
        this.brand = brand;
    }

    public Object getBrand() {
        return brand;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    @Override
    public String toString() {
        return
                "ResultsItem{" +
                        "languageCode = '" + languageCode + '\'' +
                        ",brand = '" + brand + '\'' +
                        ",productName = '" + productName + '\'' +
                        "}";
    }
}