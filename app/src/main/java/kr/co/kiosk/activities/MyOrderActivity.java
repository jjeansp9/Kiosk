package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

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

    void clickedHome(){
        finish();
    }

    void clickedBuyNow(){
        Toast.makeText(this, "주문 완료", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MyOrderActivity.this, PaymentActivity.class);
        startActivity(intent);
    }

    void clickedBuyCancel(){
        Toast.makeText(this, "주문 취소", Toast.LENGTH_SHORT).show();
        finish();
    }
}