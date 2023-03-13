package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.ActivityMainBinding;
import kr.co.kiosk.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity {

    ActivityPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.home.setOnClickListener(v-> clickedHome());
    }

    void clickedHome(){
        finish();
    }
}


























