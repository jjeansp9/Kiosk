package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.ActivityEndBinding;

public class EndActivity extends AppCompatActivity {

    ActivityEndBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityEndBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.home.setOnClickListener(v->clickedHome());
        binding.btnConfirm.setOnClickListener(v->clickedConfirm());
        binding.btnGoBack.setOnClickListener(v->clickedGoBack());
    }

    void clickedHome(){
        // 지금까지 쌓아놓은 모든 Activity 종료하고 MainActivity만 실행
        Intent intent= new Intent(EndActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    void clickedConfirm(){

    }

    void clickedGoBack(){
        // 지금까지 쌓아놓은 모든 Activity 종료하고 MainActivity만 실행
        Intent intent= new Intent(EndActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}