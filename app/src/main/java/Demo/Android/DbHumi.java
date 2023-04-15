package Demo.Android;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHumi extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 13;

    public DbHumi(Context context) {
        super(context, "demo", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE humi(xValues LONG,yValues DOUBLE);";
        db.execSQL(createTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS humi";
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
    public void InsertData(Long x,double y){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("xValues",x);
        contentValues.put("yValues",y);
        db.insert("humi",null,contentValues);
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("humi", null, null);
        db.close();
    }
    @SuppressLint("Range")
    public double getLastYValue() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("humi", new String[]{"yValues"}, null, null, null, null, "xValues DESC", "1");
        double result = 0;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getDouble(cursor.getColumnIndex("yValues"));
            cursor.close();
        }
        db.close();
        return result;
    }
    public Cursor getListContent(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + "humi",null);
        return data;
    }

}