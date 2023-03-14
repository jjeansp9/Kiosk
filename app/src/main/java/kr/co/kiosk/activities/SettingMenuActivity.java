package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.ActivitySettingMenuBinding;

public class SettingMenuActivity extends AppCompatActivity {

    ActivitySettingMenuBinding binding;
    int select= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySettingMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.coffee.setOnClickListener(v-> clickedCoffee());

    }

    void clickedCoffee(){
        if (select % 2==0){binding.coffee.setBackgroundColor(Color.parseColor("#000000"));
        }else{ binding.coffee.setBackgroundColor(Color.parseColor("#8A8A8A")); }
        select++;
    }
}