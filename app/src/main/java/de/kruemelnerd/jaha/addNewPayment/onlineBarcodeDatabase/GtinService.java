package de.kruemelnerd.jaha.addNewPayment.onlineBarcodeDatabase;

import java.io.IOException;

import de.kruemelnerd.jaha.addNewPayment.onlineBarcodeDatabase.model.GtinResponse;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class GtinService {
    private final static String BASE_URL = "https://mignify.p.mashape.com/gtins/v1.0/";


    public interface GtinDatabaseClient {
        @GET("productsToGtin")
        Call<GtinResponse> getProductForGtin(@Query("gtin") String gtin);
    }


    public static GtinDatabaseClient getApi() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("X-Mashape-Key", "Zp9sBZ6tc7mshoE6sO0sddl2J9IXp1kcS0Bjsn33XMarlRlAUB")
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client);

        Retrofit retrofit = builder.build();
        return retrofit.create(GtinDatabaseClient.class);
    }
}
