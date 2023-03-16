package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.ActivityHomeBinding;
import kr.co.kiosk.databinding.ActivityMyOrderBinding;
import kr.co.kiosk.model.Price;

public class MyOrderActivity extends AppCompatActivity {

    ActivityMyOrderBinding binding;

    private ArrayList<Price> priceItems= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMyOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.home.setOnClickListener(v-> clickedHome());
        binding.buyNow.setOnClickListener(v-> clickedBuyNow());
        binding.buyCancel.setOnClickListener(v-> clickedBuyCancel());

        Intent intent= getIntent();

        priceItems= intent.getParcelableArrayListExtra("select_menu");
    }

    private void clickedHome(){
        finish();
    }

    private void clickedBuyNow(){
        Toast.makeText(this, "주문 완료", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MyOrderActivity.this, PaymentActivity.class);
        startActivity(intent);
    }

    private void clickedBuyCancel(){
        Toast.makeText(this, "주문 취소", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setSaveText() {


    }


//    - 파일 경로 : /sdcard/Android/data/%Package%/cache/history/history.log
//
//    - 파일에 기록할 format : [yyyyMMdd HH:mm:ss][주문내역][결제금액]


}





























