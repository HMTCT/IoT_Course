package Demo.Android;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Light_graph extends AppCompatActivity {
    MQTTHelper mqttHelper;
    ArcGauge arcGauge;

    GraphView graphView;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[0]);
    DBHelper LightHelper;
    SQLiteDatabase sqLiteDatabase;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    Button Return,log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_graph);

        graphView = (GraphView) findViewById(R.id.lightgraoh);
        LightHelper = new DBHelper(this);
        Return = (Button) findViewById(R.id.Return);
        //Dòng này nên comment khi demo, dòng xóa hết dữ liệu của bảng
        //HumiHelper.deleteAllData();
        log = (Button) findViewById(R.id.log);
        graphView.getViewport().setScalable(true);

        graphView.getViewport().setScrollable(true);
        sqLiteDatabase = LightHelper.getWritableDatabase();
        graphView.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(100);
        series.resetData(getDataPoint());
        long xValues = new Date().getTime();
        graphView.getViewport().setMaxX(xValues);
        graphView.addSeries(series);
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double Value,boolean isValueX){
                if(isValueX){
                    return sdf.format(new Date((long)Value));
                }
                else{
                    return super.formatLabel(Value,isValueX);
                }
            }
        });
        series.resetData(getDataPoint());
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Return();
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log();
            }
        });
        //Gauge
        arcGauge = findViewById(R.id.lightgauge);
        Range range1 = new Range();
        range1.setFrom(0.0);
        range1.setTo(30.0);
        range1.setColor(Color.BLACK);

        Range range2 = new Range();
        range2.setFrom(31.0);
        range2.setTo(300.0);
        range2.setColor(Color.GREEN);

        Range range3 = new Range();
        range3.setFrom(300.0);
        range3.setTo(400.0);
        range3.setColor(Color.RED);

        arcGauge.addRange(range1);
        arcGauge.addRange(range2);
        arcGauge.addRange(range3);
        arcGauge.setValue(LightHelper.getLastYValue());

        startMQTT();
    }

    private DataPoint[] getDataPoint() {
        String [] comlumns = {"xValues","yValues"};
        Cursor cursor = sqLiteDatabase.query("light",comlumns,null,null,null,null,null);
        DataPoint[] dp = new DataPoint[cursor.getCount()];
        for(int i = 0;i < cursor.getCount();i++){
            cursor.moveToNext();
            dp[i] = new DataPoint(cursor.getLong(0),cursor.getDouble(1));
        }
        return dp;
    }
    public void sendDataMQTT(String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(false);
        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        }catch (MqttException e){
        }
    }

    public void startMQTT(){
        mqttHelper = new MQTTHelper(this    );


        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEST",topic + "---" + message.toString());


                if(topic.contains("light2")){
                    arcGauge = findViewById(R.id.lightgauge);
                    Range range1 = new Range();
                    range1.setFrom(0.0);
                    range1.setTo(40.0);
                    range1.setColor(Color.BLACK);

                    Range range2 = new Range();
                    range2.setFrom(40.0);
                    range2.setTo(299.0);
                    range2.setColor(Color.GREEN);

                    Range range3 = new Range();
                    range3.setFrom(299.0);
                    range3.setTo(400.0);
                    range3.setColor(Color.RED);

                    arcGauge.addRange(range1);
                    arcGauge.addRange(range2);
                    arcGauge.addRange(range3);
                    double val = Double.parseDouble(new String(message.toString()));
                    arcGauge.setValue(val);

                    //Graph
                    graphView.addSeries(series);
                    graphView.getViewport().setScalable(true);
//                    graphView.getViewport().setMaxY(100);
//                    graphView.getViewport().setMinY(0);

                    graphView.getViewport().setScrollable(true);
                    sqLiteDatabase = LightHelper.getWritableDatabase();
                    graphView.getGridLabelRenderer().setHorizontalLabelsAngle(90);
                    graphView.getViewport().setMinY(0);
                    graphView.getViewport().setMaxY(400);
                    graphView.getGridLabelRenderer().setHorizontalAxisTitle("Time");
                    graphView.getGridLabelRenderer().setVerticalAxisTitle("Light Value");
                    series.resetData(getDataPoint());
                    long xValues = new Date().getTime();
                    graphView.getViewport().setMaxX(xValues);
                    double yValues = Double.parseDouble(message.toString());
                    //graphView.getViewport().setMaxX(xValues);
                    LightHelper.InsertData(xValues,yValues);
                    series.resetData(getDataPoint());
                    graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                        @Override
                        public String formatLabel(double Value,boolean isValueX){
                            if(isValueX){
                                return sdf.format(new Date((long)Value));
                            }
                            else{
                                return super.formatLabel(Value,isValueX);
                            }
                        }
                    });



                }


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }
    public void Return() {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }
    public void Log() {
        Intent intent = new Intent(this, LightHistory.class);
        startActivity(intent);
    }
}