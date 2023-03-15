package kr.co.kiosk.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MenuDBHelper extends SQLiteOpenHelper{

    SQLiteDatabase db;
    ContentValues contentValues = new ContentValues();

    public static final String DATABASE_NAME = "Test_menu.db"; // 데이터베이스 명
    public static final String TABLE_NAME = "test_coffee"; // 테이블 명

    // 테이블 항목
    public static final String COL_1 = "name";
    public static final String COL_2 = "price";
    public static final String COL_3 = "image";

    public MenuDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 테이블(test_coffee) 생성 [ 메뉴이름, 메뉴가격, 메뉴이미지 ]
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price TEXT, image TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 데이터 추가하기
    public void insertData(String name, String price, String image){
        db.execSQL("INSERT INTO " + TABLE_NAME + "(name, price, image) VALUES('" + name + "' , '" + price + "' , '" + image + "')");
    }

    // 데이터 수정하기
    public void updateData(String name, String price, String image){
        db.execSQL("UPDATE " + TABLE_NAME + " SET name='"+name+"', price='"+price+"', image='"+image+"'");
    }

    // 데이터 삭제하기
    public void deleteData(){
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE name=?");
    }

    // 모든 데이터 불러오기
    public Cursor getDataAll(){
        db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        return cursor;
    }

}














