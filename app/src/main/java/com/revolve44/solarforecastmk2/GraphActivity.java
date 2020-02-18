package com.revolve44.solarforecastmk2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GraphActivity extends AppCompatActivity {
    public EditText enterCity;
    public EditText enterPower;
    public GraphView graph;
    public TextView output;
    public String city;
    public float power;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);
        output = findViewById(R.id.output);
        enterPower = findViewById(R.id.current_power);
        enterCity = findViewById(R.id.your_city2);
        graph = findViewById(R.id.graph);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(
                GraphActivity.this, new SimpleDateFormat("HH:mm")
        ));
    }

    public void OnClickListener1(View view) {
        try {
            city = enterCity.getText().toString();
            power = Float.parseFloat(enterPower.getText().toString());
            getCurrentData();

        }catch (Exception e){
            output.setText("nope");
        }
    }

    void getCurrentData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherForecastResponse> call = service.getDailyData(city, MainActivity.AppId);
        call.enqueue(new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherForecastResponse> call, @NonNull Response<WeatherForecastResponse> response) {
                Log.d("alfia", Integer.toString(response.code()));
                if (response.code() == 200) {
                    graph.removeAllSeries();
                    DataPoint[] dataPoints = new DataPoint[6];
                    WeatherForecastResponse weatherResponse = response.body();
                    for(int i=0;i<6;i++){
                        Date time=new Date((long)weatherResponse.list.get(i).dt*1000);
                        dataPoints[i] = new DataPoint(time, power*weatherResponse.list.get(i).clouds.all/100);
                    }
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
                    graph.addSeries(series);
                }
                else if(response.code() == 429){
                    output.setText("слишком много запросов");
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherForecastResponse> call, @NonNull Throwable t) {
                output.setText("nope");
            }
        });
    }

}
