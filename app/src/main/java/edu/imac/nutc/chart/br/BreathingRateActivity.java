package edu.imac.nutc.chart.br;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import edu.imac.nutc.chart.R;
import edu.imac.nutc.chart.model.API;
import edu.imac.nutc.chart.model.SetWeek;

/**
 * Created by user on 2017/5/1.
 */

public class BreathingRateActivity extends AppCompatActivity {

    BreathingWeekendView breathingWeekendView;
    TextView avgHighBmp;
    TextView avgLowBmp;
    TextView avgBmp;
    API api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breathing_rate);
        findView();
        api = new API(BreathingRateActivity.this);
        api.getBR("Cherry");
        api.setOnBRFinish(getBRFinish);
    }

    private void findView() {
        avgHighBmp = (TextView) findViewById(R.id.breathing_rate_avg_high_num);
        avgBmp = (TextView) findViewById(R.id.breathing_rate_avg_bpm_num);
        avgLowBmp = (TextView) findViewById(R.id.breathing_rate_avg_low_num);
        breathingWeekendView = (BreathingWeekendView) findViewById(R.id.breathing_weekend);
    }

    API.GetBRFinish getBRFinish = new API.GetBRFinish() {
        @Override
        public void finish(String response) throws JSONException {
            String[] str = SetWeek.getWeek();
            int[] max = new int[7];
            int[] avg = new int[7];
            int[] min = new int[7];
            JSONObject jsonObject = new JSONObject(response);
            for (int i = 0; i < str.length; i++) {
                JSONObject jsonObject1 = jsonObject.getJSONObject(str[i]);
                max[i] = Integer.valueOf(jsonObject1.get("max").toString());
                min[i] = Integer.valueOf(jsonObject1.get("min").toString());
                if (!jsonObject1.get("avg").toString().equals("0")) {
                    String[] sp = jsonObject1.get("avg").toString().split("\\.");
                    avg[i] = Integer.valueOf(sp[0]);
                } else {
                    avg[i] = 0;
                }

            }
            breathingWeekendView.setLineData(avg);
            breathingWeekendView.setMaxData(max);
            breathingWeekendView.setMinData(min);
            breathingWeekendView.invalidate();
            avgHighBmp.setText(max[6] + "");
            avgBmp.setText(avg[6] + "");
            avgLowBmp.setText(min[6] + "");
        }
    };
}
