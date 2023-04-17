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

public class AI_History extends AppCompatActivity {
    DbAI DbAI;
    Button Return;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_history);
        ListView listlight = (ListView) findViewById(R.id.listview_ai);

        Return = (Button) findViewById(R.id.Return);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Return();
            }
        });
        DbAI = new DbAI(this);

        ArrayList<String> listarray = new ArrayList<>();

        Cursor data = DbAI.getListContent();
        while(data.moveToNext()){
            listarray.add("Date: "+ sdf.format(Long.parseLong(data.getString(0))) + ", " + data.getString(1));
            //listarray.add("Date: "+ sdf.format(Long.parseLong(dataPump.getString(0))) + ", " + dataPump.getString(1));
            ListAdapter ListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listarray);
            listlight.setAdapter(ListAdapter);

        }

    }
    public void Return() {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }

}