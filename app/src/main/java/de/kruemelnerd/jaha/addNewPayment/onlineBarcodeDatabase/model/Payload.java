package de.kruemelnerd.jaha.addNewPayment.onlineBarcodeDatabase.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Payload {

    @SerializedName("gtinType")
    private String gtinType;

    @SerializedName("gtinCode")
    private String gtinCode;

    @SerializedName("results")
    private List<ResultsItem> results;

    public void setGtinType(String gtinType) {
        this.gtinType = gtinType;
    }

    public String getGtinType() {
        return gtinType;
    }

    public void setGtinCode(String gtinCode) {
        this.gtinCode = gtinCode;
    }

    public String getGtinCode() {
        return gtinCode;
    }

    public void setResults(List<ResultsItem> results) {
        this.results = results;
    }

    public List<ResultsItem> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return
                "Payload{" +
                        "gtinType = '" + gtinType + '\'' +
                        ",gtinCode = '" + gtinCode + '\'' +
                        ",results = '" + results + '\'' +
                        "}";
    }
}