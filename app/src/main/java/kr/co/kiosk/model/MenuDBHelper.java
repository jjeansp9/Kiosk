package kr.co.kiosk.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import kr.co.kiosk.activities.SettingMenuActivity;

public class MenuDBHelper extends SQLiteOpenHelper{

    SQLiteDatabase db= getWritableDatabase();

    // 데이터베이스 명
    //public static final String[] DATABASE_NAME = {"coffee_test_03.db", "parfait_test_03.db","milk_tea_test_03.db","dessert_test_03.db","drink_test_03.db", "my_order_test_03.db"};
    public static final String DATABASE_NAME = "test_03";

    // 테이블 명
    public static final String TABLE_NAME = "test_coffee";
    public int categoryNum;

    // 테이블 항목
    public static final String COL_1 = "category";
    public static final String COL_2 = "name";
    public static final String COL_3 = "price";
    public static final String COL_4 = "image";
    public static final String COL_5 = "info";

    public MenuDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블(test_coffee) 생성 [ 메뉴이름, 메뉴가격, 메뉴이미지 ]
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, category TEXT, name TEXT, price TEXT, image TEXT, info TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 데이터 추가하기
    public void insertData(String category, String name, String price, String image, String info){
        ContentValues contentValues= new ContentValues();
        contentValues.put(COL_1, category);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, price);
        contentValues.put(COL_4, image);
        contentValues.put(COL_5, info);

        db.insert(TABLE_NAME, null, contentValues);

        //db.execSQL("INSERT INTO " + TABLE_NAME + "(name, price, image) VALUES('" + name + "' , '" + price + "' , '" + image + "')");
    }

    // 데이터 수정하기
    public void updateData(String category, String name, String price, String image, String info){
        ContentValues contentValues= new ContentValues();
        contentValues.put(COL_1, category);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, price);
        contentValues.put(COL_4, image);
        contentValues.put(COL_5, info);

        db.update(TABLE_NAME, contentValues, "name = ?", new String[] {name});

        //db.execSQL("UPDATE " + TABLE_NAME + " SET price='"+price+"', image='"+image+"', info='"+info+"' WHERE name= '"+name+"'" );
    }

    // 데이터 삭제하기
    public void deleteData(String name){ db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE name=?", new String[]{name}); }

    // 모든 데이터 불러오기
    public Cursor getDataAll(){
        db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        return cursor;
    }
}














