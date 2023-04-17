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

public class Button_history extends AppCompatActivity {
    DbButton ButtonHelper;
    DbButtonPump ButtonPump;
    Button Return;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_history);
        ListView listlight = (ListView) findViewById(R.id.listview_buttondatalight);
        ListView listpump = (ListView) findViewById(R.id.listview_buttondatapump);

        Return = (Button) findViewById(R.id.Return);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Return();
            }
        });
        ButtonHelper = new DbButton(this);
        ButtonPump = new DbButtonPump(this);

        ArrayList<String> listarraylight = new ArrayList<>();
        ArrayList<String> listarraypump = new ArrayList<>();

        Cursor data = ButtonHelper.getListContent();
        Cursor dataPump = ButtonPump.getListContent();
        ListAdapter ListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listarraylight);
        while(data.moveToNext()){
            listarraylight.add("Date: "+ sdf.format(Long.parseLong(data.getString(0))) + ", " + data.getString(1));
            //listarray.add("Date: "+ sdf.format(Long.parseLong(dataPump.getString(0))) + ", " + dataPump.getString(1));
             ListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listarraylight);
            listlight.setAdapter(ListAdapter);

        }
        while(dataPump.moveToNext()){
          //  listarray.add("Date: "+ sdf.format(Long.parseLong(data.getString(0))) + ", " + data.getString(1));
            listarraypump.add("Date: "+ sdf.format(Long.parseLong(dataPump.getString(0))) + ", " + dataPump.getString(1));
             ListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listarraypump);
            listpump.setAdapter(ListAdapter);
        }
    }
    public void Return() {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }

}