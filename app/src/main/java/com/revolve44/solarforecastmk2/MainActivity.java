package com.revolve44.solarforecastmk2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ajts.androidmads.fontutils.FontUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.androidmads.openweatherapi.WeatherResponse.main.temp_min;

public class MainActivity extends AppCompatActivity {

    //api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}

    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String CITY = "Moscow";
    public static String AppId = "5ddc6ac2a618d150efb8fd1ab29de6f4";
    public static String MC = "&units=metric&appid=";

    public static String lat = "80.75";
    public static String lon = "35.61";
    public static String metric = "metric";

    public TextView CurrentTemperature;
    public TextView CurrentOutput;
    public TextView Power;
    public TextView City;

    public Button MainGet;
    public Button Save_data;
    public Button ToSecondActivity;
    public Button GraphButton;
    //Edittext
    public EditText enterCity;
    public EditText enterNominal;
    //Variables
    public float NominalPower = 0;//????????????????????????????????
    public float CurrentPower;
    public float cloud;
    public float temp;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final float TEXT2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextView
        CurrentOutput = findViewById(R.id.current_output);
        Power = findViewById(R.id.power);
        City = findViewById(R.id.cIty);
        CurrentTemperature = findViewById(R.id.current_temperature);

        //Button
        MainGet = findViewById(R.id.MainGet);
        Save_data = findViewById(R.id.save_data);
        ToSecondActivity = findViewById(R.id.to_second_activity);
        GraphButton = findViewById(R.id.GraphButton);

        //EditText
        enterCity = findViewById(R.id.your_city);
        enterNominal = findViewById(R.id.nominal_output);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");
        FontUtils fontUtils = new FontUtils();
        fontUtils.applyFontToView(CurrentOutput, typeface);

//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getCurrentData();
//
//            }
//        });
        loadData();

        if (NominalPower>0){
            getCurrentData();
        }
    }


    public void OnClickListener1(View view) {
        try {
            if (NominalPower==0){
            CITY = enterCity.getText().toString(); //gets you the contents of edit text
            //CurrentPower = NominalPower;
            NominalPower = Float.parseFloat(enterNominal.getText().toString());}
            else{getCurrentData();}
        }catch (Exception e){
            CurrentOutput.setText("Error, please fill both form");
        }
    }

    void getCurrentData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        //Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, metric, AppId);
        Call<WeatherResponse> call = service.getCurrentWeatherData(CITY, metric, AppId);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;
                    //String temperature = ""+weatherResponse.main.temp;
                    cloud = weatherResponse.clouds.all;
                    temp = weatherResponse.main.temp;
//                            "Country: " +
//                            weatherResponse.sys.country +
//                            "\n" +
//                            "Temperature: " +
//                            weatherResponse.main.temp +
//                            "\n" +
//                            "Temperature(Min): " +
//                            weatherResponse.main.temp_min +
//                            "\n" +
//                            "Temperature(Max): " +
//                            weatherResponse.main.temp_max +
//                            "\n" +
//                            "Humidity: " +
//                            weatherResponse.main.humidity +
//                            "\n" +
//                            "Pressure: " +
//                            weatherResponse.main.pressure;
//                    weatherData.setText(temperature);
//                    //check2.setText(""+ cloud);
//                    //weatherData.setText(rain);
//                    a = weatherResponse.main.temp_max;
//                    b = weatherResponse.main.temp_min;
//                    c = weatherResponse.main.temp;
//
//                    spec = weatherResponse.sys.country;
                    //Print();//поместил ниже после обявления spec  и теперь спец заново меняется и он теперь не null
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                City.setText(t.getMessage());
            }
        });
        CurrentPower = (cloud/100)*NominalPower;

        CurrentOutput.setText("Real output: ~"+CurrentPower+ "kW");
        Power.setText("Nominal output: "+NominalPower+"kW");
        City.setText("My City: " + CITY);
        CurrentTemperature.setText("Current temp: "+temp);
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, enterCity.getText().toString());
        editor.putFloat("", Float.parseFloat(enterNominal.getText().toString()));
        //editor.putFloat(enterNominal.getText().toString(), TEXT2);
        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        CITY = sharedPreferences.getString(TEXT, "");
        NominalPower = sharedPreferences.getFloat("", TEXT2);
        getCurrentData();
    }

    public void toSecondActivity(View view) {
        //Log.d(LOG_TAG, "Button clicked!");

        Intent intent = new Intent(this, SecondActivity.class);
        //String message = mMessageEditText.getText().toString();

        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void toGraphActivity(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }

    public void OnClickListenerSave(View view) {
        saveData();
    }
}

