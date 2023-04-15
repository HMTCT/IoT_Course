package Demo.Android;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbButtonPump extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 13;

    public DbButtonPump(Context context) {
        super(context, "demo", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE ButtonPump(xValues LONG,yValues STRING);";
        db.execSQL(createTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS ButtonPump";
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void InsertData(Long x,String y){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("xValues",x);
        contentValues.put("yValues",y);
        db.insert("ButtonPump",null,contentValues);
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ButtonPump", null, null);
        db.close();
    }

    public Cursor getListContent(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + "ButtonPump",null);
        return data;
    }
    @SuppressLint("Range")
    public String getLastYValue() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("ButtonPump", new String[]{"yValues"}, null, null, null, null, "xValues DESC", "1");
        String result = "0";
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex("yValues"));
            cursor.close();
        }
        db.close();
        return result;
    }
}