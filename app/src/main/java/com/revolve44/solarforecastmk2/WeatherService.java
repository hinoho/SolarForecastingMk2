package com.revolve44.solarforecastmk2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mushtaq on 05-11-2018.
 */
//api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}
//https://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22

public interface WeatherService {
    @GET("data/2.5/weather?")
        // закомиченный запрос норм
//    Call<WeatherResponse> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("units") String metric,
//                                                @Query("APPID") String app_id);
    Call<WeatherResponse> getCurrentWeatherData(@Query("q") String CITY, @Query("units") String metric,
                                                @Query("APPID") String app_id);


    @GET("data/2.5/forecast?")
    Call<WeatherForecastResponse> getDailyData(@Query("q") String CITY, @Query("APPID") String app_id);
}