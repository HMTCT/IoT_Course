package Demo.Android;

import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Temp_History extends AppCompatActivity {
    DbTemp TempHelper;
    Button Return;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_history);
        ListView list = (ListView) findViewById(R.id.listview_tempdata);
         Return = (Button) findViewById(R.id.Return);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Return();
            }
        });
        TempHelper = new DbTemp(this);
        ArrayList<String> listarray = new ArrayList<>();
        Cursor data = TempHelper.getListContent();
        while(data.moveToNext()){
            listarray.add("Date: "+ sdf.format(Long.parseLong(data.getString(0))) + ",  Value: " + data.getString(1)+"°C");
            ListAdapter ListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listarray);
            list.setAdapter(ListAdapter);
        }
    }
    public void Return() {
        Intent intent = new Intent(this, TempGraph.class);
        startActivity(intent);
    }
}