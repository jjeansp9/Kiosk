package kr.co.kiosk.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Toast;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.category1.setOnClickListener(v-> clickedCoffe());
        binding.category2.setOnClickListener(v-> clickedParfait());
        binding.category3.setOnClickListener(v-> clickedMilkTea());
        binding.category4.setOnClickListener(v-> clickedDessert());
        binding.category5.setOnClickListener(v-> clickedDrink());
    }

    void clickedCoffe(){
        Toast.makeText(this, "커피 메뉴로 이동", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    void clickedParfait(){
        Toast.makeText(this, "파르페 메뉴로 이동", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    void clickedMilkTea(){
        Toast.makeText(this, "밀크티 메뉴로 이동", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    void clickedDessert(){
        Toast.makeText(this, "디저트 메뉴로 이동", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    void clickedDrink(){
        Toast.makeText(this, "음료 메뉴로 이동", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }


}