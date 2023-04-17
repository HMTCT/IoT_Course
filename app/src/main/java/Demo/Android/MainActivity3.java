package Demo.Android;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.DayNightSwitch;
import com.github.angads25.toggle.widget.LabeledSwitch;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity3 extends AppCompatActivity {
    MQTTHelper mqttHelper;
    TextView txtTemp,txtHumi,txtLight,tView,motion,motionview;
    SeekBar sBar;
    Button logout,tempgraph,humigraph,lightgraph,buttonlog;
    DayNightSwitch btnLight ;
    LabeledSwitch btnPump;
    DbTemp TempHelper;
    DbHumi HumiHelper;
    DbAI dbAI;
    DbButton ButtonHelper;
    DbButtonPump ButtonPump;
    DBHelper LightHelper;
    SQLiteDatabase sqLiteDatabase;
    Timer timer;

    ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(2);
    boolean btnLightTimeout  = true;
    boolean btnPUMPTimeout = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        motion = findViewById(R.id.motiondetect);
        txtTemp = findViewById(R.id.Temperature);
        txtHumi = findViewById(R.id.Humidity);
        motionview = findViewById(R.id.motionview);
        txtLight=findViewById(R.id.light);
        btnLight =  findViewById(R.id.lightswitch);
        btnPump = findViewById(R.id.buttonpump);
        tView = (TextView) findViewById(R.id.textview1);
        sBar = (SeekBar) findViewById(R.id.seekBar1);
        logout = (Button) findViewById(R.id.logout);
        tempgraph =(Button) findViewById(R.id.temp_button);
        humigraph =(Button) findViewById(R.id.humi_button);
        lightgraph = (Button) findViewById(R.id.light_button);
        buttonlog = (Button) findViewById(R.id.log);
        HumiHelper = new DbHumi(this);
        TempHelper = new DbTemp(this);
        ButtonHelper = new DbButton(this);
        ButtonPump = new DbButtonPump(this);
        LightHelper = new DBHelper(this);
        dbAI = new DbAI(this);
        sqLiteDatabase = dbAI.getWritableDatabase();

        txtHumi.setText(String.valueOf(HumiHelper.getLastYValue())+"%");
        txtTemp.setText(String.valueOf(TempHelper.getLastYValue()) + " °C");
        txtLight.setText(String.valueOf(LightHelper.getLastYValue()) + " lux");
        motion.setText(dbAI.getLastYValue());

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                TempHelper.deleteOldData(24 * 60 * 60);
                HumiHelper.deleteOldData(24 * 60 * 60);
                LightHelper.deleteOldData(24 * 60 * 60);
            }
        }, 100000);

        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tView.setText(pval + "/" + seekBar.getMax());
                String s = Integer.toString(pval);
                //sendDataMQTT("LamVinh/feeds/fan\n",s);

            }
        });
        sqLiteDatabase = ButtonHelper.getWritableDatabase();
        if(ButtonHelper.getLastYValue().equals("Button Light is ON")){
            btnLight.setOn(true);
        }
        if(ButtonPump.getLastYValue().equals("Button Pump is ON")){
            btnPump.setOn(true);
        }


        //Quay ve trang dang nhap
       logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });
        tempgraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TempGraph();
            }
        });

        humigraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HumiGraph();
            }
        });
        lightgraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LightGraph();
            }
        });
        buttonlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonLog();
            }
        });
        motionview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AI();
            }
        });
        btnLight.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn == true){
                    sendDataMQTT("triethoang/feeds/button1\n", "1");
                    long xValues = new Date().getTime();
                    ButtonHelper.InsertData(xValues,"Button Light is ON");
                }
                else{
                    sendDataMQTT("triethoang/feeds/button1\n", "0");
                    long xValues = new Date().getTime();
                    ButtonHelper.InsertData(xValues,"Button Light is OFF");

                }
                btnLight.setEnabled(false);
                btnLightTimeout = true;
                exec.schedule(new Runnable() {
                    public void run() {
                        if (!btnLightTimeout)
                            return;
                        btnLight.setEnabled(true);
                        btnLight.setOn(!btnLight.isOn());
                    }
                }, 10, TimeUnit.SECONDS);
            }
        });
        btnPump.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
//                if(isOn == true){
//                    sendDataMQTT("triethoang/feeds/button2\n", "1");
//                    long xValues = new Date().getTime();
//                    ButtonPump.InsertData(xValues,"Button Pump is ON");
//                }
//                else{
//                    sendDataMQTT("triethoang/feeds/button2\n", "0");
//                    long xValues = new Date().getTime();
//                    ButtonPump.InsertData(xValues,"Button Pump is OFF");
//
//                }

                btnPump.setEnabled(false);
                btnPUMPTimeout = true;
                exec.schedule(new Runnable() {
                    public void run() {
                        if (!btnPUMPTimeout)
                            return;
                        btnPump.setEnabled(true);
                        btnPump.setOn(!btnPump.isOn());
                    }
                }, 5, TimeUnit.SECONDS);
            }
        });
        startMQTT();
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
        dbAI = new DbAI(this);

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

                if(topic.contains("cambien3")){
                    long xValues = new Date().getTime();
                    HumiHelper.InsertData(xValues,Double.parseDouble(message.toString()));
                    txtHumi.setText(String.valueOf(HumiHelper.getLastYValue())+"%");
                }
                else if(topic.contains("cambien1")){
                    long xValues = new Date().getTime();
                    TempHelper.InsertData(xValues,Double.parseDouble(message.toString()));
                    txtTemp.setText(String.valueOf(TempHelper.getLastYValue())+"°C");
                }
                else if(topic.contains("cambien2")){
                    long xValues = new Date().getTime();
                    LightHelper.InsertData(xValues,Double.parseDouble(message.toString()));
                    txtLight.setText(String.valueOf(LightHelper.getLastYValue())+"Lux");
                }
                else if(topic.contains("ai")){
                    motion.setText(message.toString());
                    long xValues = new Date().getTime();
                    dbAI.InsertData(xValues,message.toString());

                }
                else if(topic.contains("button1")) {
                    if (message.toString().equals("1")) {
                        btnLight.setOn(true);
                        long xValues = new Date().getTime();
                        ButtonHelper.InsertData(xValues,"Button Light is ON");

                    } else {
                        btnLight.setOn(false);
                        long xValues = new Date().getTime();
                        ButtonHelper.InsertData(xValues,"Button Light is OFF");
                    }
                }
                else if(topic.contains("button2")){
                    if(message.toString().equals("1")){
                        btnPump.setOn(true);
                        long xValues = new Date().getTime();
                        ButtonPump.InsertData(xValues,"Button Pump is ON");

                    }
                    else {btnPump.setOn(false);
                        long xValues = new Date().getTime();
                        ButtonHelper.InsertData(xValues,"Button Light is OFF");}
                }
                else if(topic.contains("fan")){
                    sBar.setProgress(Integer.parseInt(message.toString()));
                    tView.setText((message.toString())+"/" + sBar.getMax());
                }

                else if (topic.contains("ack") || topic.contains("error")){
                    String[] msg = message.toString().split(":");
                    if (msg[0].equals("button1")){
                        btnLightTimeout = false;

                        if (topic.contains("error")){
                            btnLight.setEnabled(true);
                            btnLight.setOn(!btnLight.isOn());
                            return;
                        }

                        int isOn = btnLight.isOn()? 1:0;
                        if (isOn == Integer.parseInt(msg[1])){
                            btnLight.setEnabled(true);
                            btnLight.setOn(btnLight.isOn());
                        }
                    }
                    if (msg[0].equals("button2")){
                        btnPUMPTimeout = false;

                        if (topic.contains("error")){
                            btnPump.setEnabled(true);
                            btnPump.setOn(!btnPump.isOn());
                            return;
                        }

                        int isOn = btnPump.isOn()? 1:0;
                        if (isOn == Integer.parseInt(msg[1])) {
                            btnPump.setEnabled(true);
                            btnPump.setOn(btnPump.isOn());
                        }
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void LogOut() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        mqttHelper.disconnect();
    }
    public void TempGraph() {
        Intent intent = new Intent(this, TempGraph.class);
        startActivity(intent);
        mqttHelper.disconnect();
        finish();
    }
    public void HumiGraph() {
        Intent intent = new Intent(this, HumiGraph.class);
        startActivity(intent);
        mqttHelper.disconnect();
        finish();
    }
    public void AI() {
        Intent intent = new Intent(this, AI_History.class);
        startActivity(intent);
        mqttHelper.disconnect();
        finish();

    }
    public void LightGraph() {
        Intent intent = new Intent(this, Light_graph.class);
        startActivity(intent);
        mqttHelper.disconnect();
        finish();
    }
    public void buttonLog() {
        Intent intent = new Intent(this, Button_history.class);
        startActivity(intent);
        mqttHelper.disconnect();
        finish();
    }
}
