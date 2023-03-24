package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.ActivityMainBinding;
import kr.co.kiosk.databinding.ActivityPaymentBinding;
import kr.co.kiosk.model.Price;

public class PaymentActivity extends AppCompatActivity {

    ActivityPaymentBinding binding;

    ArrayList<Price> priceList= new ArrayList<>();
    String priceResult= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent= getIntent();
        priceList= intent.getParcelableArrayListExtra("select_menu");
        priceResult= intent.getStringExtra("price_result");

        //binding.home.setOnClickListener(v-> clickedHome());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PaymentActivity.this, "결제 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PaymentActivity.this, EndActivity.class);
                intent.putExtra("select_menu", priceList);
                intent.putExtra("price_result", priceResult);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    void clickedHome(){
        finish();
    }
}


























