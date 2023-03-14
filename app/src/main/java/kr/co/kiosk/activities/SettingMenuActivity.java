package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import java.text.DecimalFormat;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.ActivitySettingMenuBinding;

public class SettingMenuActivity extends AppCompatActivity {

    ActivitySettingMenuBinding binding;
    int select= 0;

    DecimalFormat myFormatter= new DecimalFormat("###,###");
    String result= "";

//    TextWatcher watcher= new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
//            result= myFormatter.format(Double.parseDouble(s.toString().replaceAll(",","")));
//            binding.etMenuPrice.setText(result);
//            binding.etMenuPrice.setSelection(result.length());
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySettingMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.coffee.setOnClickListener(v-> clickedCoffee());

        //binding.etMenuPrice.addTextChangedListener(watcher);

    }

    void clickedCoffee(){
        if (select % 2==0){binding.coffee.setBackgroundColor(Color.parseColor("#000000"));
        }else{ binding.coffee.setBackgroundColor(Color.parseColor("#8A8A8A")); }
        select++;
    }
}