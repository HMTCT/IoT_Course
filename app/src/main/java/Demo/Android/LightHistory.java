package Demo.Android;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class LightHistory extends AppCompatActivity {
    DBHelper LightHelper;
    Button Return;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_history);
        ListView list = (ListView) findViewById(R.id.listview_lightdata);
        Return = (Button) findViewById(R.id.Return);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Return();
            }
        });
        LightHelper = new DBHelper(this);
        ArrayList<String> listarray = new ArrayList<>();
        Cursor data = LightHelper.getListContent();
        while(data.moveToNext()){
            listarray.add("Date: "+ sdf.format(Long.parseLong(data.getString(0))) + ",  Value: " + data.getString(1)+" lux");
            ListAdapter ListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listarray);
            list.setAdapter(ListAdapter);
        }
    }
    public void Return() {
        Intent intent = new Intent(this, Light_graph.class);
        startActivity(intent);
    }
}