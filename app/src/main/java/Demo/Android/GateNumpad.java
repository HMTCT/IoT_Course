package Demo.Android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class GateNumpad extends AppCompatActivity {
    MQTTHelper mqttHelper;
    EditText gatepass;
    Button button1,button2,button3,button4,button5,button6,button7,button8,button9,button0,buttondel,buttonok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_numpad);
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        buttondel = (Button) findViewById(R.id.buttondel);
        buttonok = (Button) findViewById(R.id.buttonok);
        gatepass = (EditText) findViewById(R.id.gatepass);
        gatepass.setActivated(true);
        gatepass.setPressed(true);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatepass.setText(gatepass.getText().insert(gatepass.getText().length(),"0"));
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatepass.setText(gatepass.getText().insert(gatepass.getText().length(),"1"));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatepass.setText(gatepass.getText().insert(gatepass.getText().length(),"2"));
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatepass.setText(gatepass.getText().insert(gatepass.getText().length(),"3"));
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatepass.setText(gatepass.getText().insert(gatepass.getText().length(),"4"));
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatepass.setText(gatepass.getText().insert(gatepass.getText().length(),"5"));
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatepass.setText(gatepass.getText().insert(gatepass.getText().length(),"6"));
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatepass.setText(gatepass.getText().insert(gatepass.getText().length(),"7"));
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatepass.setText(gatepass.getText().insert(gatepass.getText().length(),"8"));
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatepass.setText(gatepass.getText().insert(gatepass.getText().length(),"9"));
            }
        });
        buttondel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatepass.setText(gatepass.getText().delete(gatepass.getText().length()-1,gatepass.getText().length()));
            }
        });    }
}