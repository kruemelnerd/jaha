package de.kruemelnerd.jaha.addNewPayment.onlineBarcodeDatabase.model;

import com.google.gson.annotations.SerializedName;

public class GtinResponse {

    @SerializedName("payload")
    private Payload payload;

    @SerializedName("message")
    private Object message;

    @SerializedName("status")
    private String status;

    @SerializedName("tookMs")
    private int tookMs;

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getMessage() {
        return message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setTookMs(int tookMs) {
        this.tookMs = tookMs;
    }

    public int getTookMs() {
        return tookMs;
    }

    @Override
    public String toString() {
        return
                "GtinResponse{" +
                        "payload = '" + payload + '\'' +
                        ",message = '" + message + '\'' +
                        ",status = '" + status + '\'' +
                        ",tookMs = '" + tookMs + '\'' +
                        "}";
    }
}