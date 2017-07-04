package com.schneidernet.heating;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONArray[]> {

    final SimpleDateFormat dateISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final SimpleDateFormat dateAxis = new SimpleDateFormat("yyMMdd HHmm");
    public static final String EXTRA_MESSAGE = "Test";
    private JSONArray[] heatingData = new JSONArray[5];

    private String[] sensors = new String[]{
            "10-000800355f27",
            "10-0008019453e2",
            "10-0008019462b6",
            "10-00080194662f",
            "10-0008019481df"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setChartVisible(View.INVISIBLE);

        getLoaderManager().initLoader(0, null, this);
    }

    private void setChartVisible(int visibility) {
        LineChart chart = (LineChart) findViewById(R.id.chart);
        chart.setVisibility(visibility);
    }

    private void fillChart(JSONArray[] jsonArray) {
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        dataSets.add(fillSingleLine(jsonArray[1], "Warmwasser", Color.RED));
//        dataSets.add(fillSingleLine(jsonArray[0], "sensor1", Color.BLUE));
//        dataSets.add(fillSingleLine(jsonArray[2], "sensor2", Color.GREEN));
//        dataSets.add(fillSingleLine(jsonArray[3], "sensor3", Color.YELLOW));
//        dataSets.add(fillSingleLine(jsonArray[4], "sensor4", Color.BLACK));

        LineData lineData = new LineData(dataSets);

        LineChart chart = (LineChart) findViewById(R.id.chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Date date = new Date((long) value);
                return dateAxis.format(date);
            }
        });

        chart.setData(lineData);
    }


    @NonNull
    private LineDataSet fillSingleLine(JSONArray jsonArray, String desc, int color) {
        List<Entry> entries = new ArrayList<>();

        if (jsonArray.length() == 0) {
            entries.add(new Entry(0, 0));
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                String dateStr = jsonArray.getJSONObject(i).getString("Date");
                float date = ((float) dateISO8601.parse(dateStr).getTime());
                entries.add(new Entry(date, (float) jsonArray.getJSONObject(i).getDouble("Value")));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        LineDataSet line = new LineDataSet(entries, desc);
        line.setColor(color);
        line.setLineWidth(3);
        line.setCircleColor(color);
        return line;
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader(this) {

            // crazy semantic :(
            //https://stackoverflow.com/questions/10524667/android-asynctaskloader-doesnt-start-loadinbackground
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public JSONArray[] loadInBackground() {

                for (int i = 0; i < sensors.length; i++) {
                    heatingData[i] = Fetch.getJSON("/sensor/" + sensors[i] + "/lastvalue?count=3500");
                }
                return heatingData;
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<JSONArray[]> loader, JSONArray[] data) {
        fillChart(data);
        setChartVisible(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<JSONArray[]> loader) {

    }


}
