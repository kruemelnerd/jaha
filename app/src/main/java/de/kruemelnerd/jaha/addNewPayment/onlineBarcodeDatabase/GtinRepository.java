package de.kruemelnerd.jaha.addNewPayment.onlineBarcodeDatabase;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import de.kruemelnerd.jaha.addNewPayment.onlineBarcodeDatabase.model.GtinResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GtinRepository {


    private static GtinRepository INSTANCE;

    private GtinRepository() {
    }

    public static GtinRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GtinRepository();
        }
        return INSTANCE;
    }

    public LiveData<GtinResponse> getProductForGtin(String gtin) {
        final MutableLiveData<GtinResponse> data = new MutableLiveData<>();

        GtinService.getApi().getProductForGtin(gtin).enqueue(new Callback<GtinResponse>() {
            @Override
            public void onResponse(Call<GtinResponse> call, Response<GtinResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GtinResponse> call, Throwable t) {
                // Error handling will be explained in the next article …
            }


        });

        return data;
    }


}
