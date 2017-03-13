package biz.melo.movielovers.omdb;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Omdb Api Singleton
 *
 * Created by hotaviano on 3/12/17.
 */

public class OmdbService {

    private static OmdbApi instance;

    // lock instance creation
    private OmdbService() {}

    /**
     * Get singleton Omdb Api service
     *
     * @return Omdb Api service
     */
    public static OmdbApi getInstance() {

        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(OmdbApi.ENDPOINT)
                    .build();

            instance = retrofit.create(OmdbApi.class);
        }

        return instance;
    }

}
